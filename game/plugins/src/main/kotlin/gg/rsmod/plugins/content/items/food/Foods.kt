package gg.rsmod.plugins.content.items.food

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.ATTACK_DELAY
import gg.rsmod.game.model.timer.COMBO_FOOD_DELAY
import gg.rsmod.game.model.timer.FOOD_DELAY
import gg.rsmod.game.model.timer.POTION_DELAY
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.hasEquipped
import gg.rsmod.plugins.api.ext.heal
import gg.rsmod.plugins.api.ext.message
import gg.rsmod.plugins.api.ext.playSound

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Foods {

    private const val EAT_FOOD_ANIM = 829
    private const val EAT_FOOD_ON_SLED_ANIM = 1469
    private const val EAT_FOOD_SOUND = 2393

    fun canEat(p: Player, food: Food): Boolean = !p.timers.has(if (food.comboFood) COMBO_FOOD_DELAY else FOOD_DELAY)

    fun eat(p: Player, food: Food) {
        val delay = if (food.comboFood) COMBO_FOOD_DELAY else FOOD_DELAY
        val anim = if (p.hasEquipped(EquipmentType.WEAPON, Items.SLED)) EAT_FOOD_ON_SLED_ANIM else EAT_FOOD_ANIM

        val heal = when (food) {
            Food.ANGLERFISH -> {
                when (p.getSkills().getMaxLevel(Skills.HITPOINTS)) {
                    in 50..59 -> 11
                    in 60..69 -> 12
                    in 70..74 -> 13
                    in 75..79 -> 15
                    in 80..89 -> 16
                    in 90..92 -> 17
                    in 93..99 -> 22
                    else -> 0
                }
            }
            else -> food.heal
        }

        val oldHp = p.getSkills().getCurrentLevel(Skills.HITPOINTS)
        val foodName = p.world.definitions.get(ItemDef::class.java, food.item).name

        p.animate(anim)
        p.playSound(EAT_FOOD_SOUND)
        if (heal > 0) {
            p.heal(heal, if (food.overheal) heal else 0)
        }

        p.resetFacePawn()

        p.timers[delay] = food.tickDelay
        p.timers[ATTACK_DELAY] = 5

        if (food == Food.KARAMBWAN) {
            // Eating Karambwans also blocks drinking potions.
            p.timers[POTION_DELAY] = 3
        }

        when (food) {
            else -> {
                p.message("You eat the ${foodName.toLowerCase()}.")
                if (p.getSkills().getCurrentLevel(Skills.HITPOINTS) > oldHp) {
                    p.message("It heals some health.")
                }
            }
        }
    }

}