package gg.rsmod.plugins.content.combat

import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.attr.FACING_PAWN_ATTR
import gg.rsmod.game.model.attr.INTERACTING_PLAYER_ATTR
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.STUN_TIMER
import gg.rsmod.plugins.api.ext.pawn
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.content.inter.attack.AttackTab

set_combat_logic {
    pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get()?.let { target ->
        pawn.facePawn(target)
    }

    pawn.queue {
        while (true) {
            if (!cycle(this)) {
                break
            }
            wait(1)
        }
    }
}

suspend fun cycle(it: QueueTask): Boolean {
    val pawn = it.pawn
    val target = pawn.attr[COMBAT_TARGET_FOCUS_ATTR]?.get() ?: return false

    if (!pawn.lock.canAttack()) {
        Combat.reset(pawn)
        return false
    }

    pawn.facePawn(target)

    if (!Combat.canEngage(pawn, target)) {
        Combat.reset(pawn)
        pawn.resetFacePawn()
        pawn.faceTile(target.tile)
        return false
    }

    if (pawn is Player) {
        pawn.setVarp(Combat.PRIORITY_PID_VARP, target.index)

        if (!pawn.attr.has(Combat.CASTING_SPELL) && pawn.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) != 0) {
            val spell = CombatSpell.values.firstOrNull { it.autoCastId == pawn.getVarbit(Combat.SELECTED_AUTOCAST_VARBIT) }
            if (spell != null) {
                pawn.attr[Combat.CASTING_SPELL] = spell
            }
        }
    }

    val strategy = CombatConfigs.getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(pawn)
    var cancelPath: Boolean = false
    var pathFound: Boolean

    if(pawn is Npc) {
        pathFound = PawnPathAction.walkTo(it, pawn, target, interactionRange = attackRange, lineOfSight = false)
    } else {
        if(pawn.tile.getDistance(target.tile) < 1 && !pawn.isAttackDelayReady()) {
            pathFound = false
            cancelPath = true
        }
        else if(!pawn.isAttackDelayReady() && !target.isAttackDelayReady() && pawn.isAttacking() && target.isAttacking()) {
            pathFound = false
            cancelPath = true
        }
        else {
            pathFound = PawnPathAction.walkTo(it, pawn, target, interactionRange = attackRange, lineOfSight = false)
        }
    }

    if (target != pawn.attr[FACING_PAWN_ATTR]?.get()) {
        return false
    }



    if (!pathFound) {
        pawn.stopMovement()
        if (pawn.entityType.isNpc) {
            /**
             * Npcs will keep trying to find a path to engage in combat.
             */
            return true
        }
        if (pawn is Player) {
            if(!cancelPath) {
                when {
                    pawn.timers.has(FROZEN_TIMER) -> pawn.message(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
                    pawn.timers.has(STUN_TIMER) -> pawn.message(Entity.YOURE_STUNNED)
                    else -> pawn.message(Entity.YOU_CANT_REACH_THAT)
                }
            }
            pawn.clearMapFlag()
        }
        Combat.reset(pawn)
        pawn.resetFacePawn()
        pawn.faceTile(target.tile)
        return false
    }

    pawn.stopMovement()

    if (Combat.isAttackDelayReady(pawn) || ( pawn is Player && AttackTab.isSpecialEnabled(pawn) && pawn.getEquipment(EquipmentType.WEAPON) != null && SpecialAttacks.ignoreDelay(pawn))) {
        if (Combat.canAttack(pawn, target, strategy)) {

            if (pawn is Player && AttackTab.isSpecialEnabled(pawn) && pawn.getEquipment(EquipmentType.WEAPON) != null) {
                if (SpecialAttacks.execute(pawn, target, world,ExecutionType.EXECUTE_ON_ATTACK)) {
                    Combat.postAttack(pawn, target)
                    return true
                }
            }

            strategy.attack(pawn, target)
            Combat.postAttack(pawn, target)
        } else {
            Combat.reset(pawn)
            return false
        }
    }
    return true
}