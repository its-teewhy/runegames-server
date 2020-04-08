package gg.rsmod.plugins.content.hungergames.items

import gg.rsmod.game.model.timer.ATTACK_DELAY
import gg.rsmod.game.model.timer.FOOD_DELAY

val IN_GAME = AttributeKey<Boolean>("in-game")

fun canUse(player: Player): Boolean = player.attr[IN_GAME] == true
fun canHeal(player: Player): Boolean = !player.timers.has(FOOD_DELAY)

on_item_option(Items.BANDAGES, "heal") {
    if(canUse(player)) {
        if (canHeal(player)) {
            useBandage(player, player.getInteractingItem())
        }
    } else player.message("You may only use bandages in games..")
}

fun useBandage(player: Player, item: Item) {
    val oldHp = player.getSkills().getCurrentLevel(Skills.HITPOINTS)
    player.inventory.remove(item = item, beginSlot = player.getInteractingItemSlot())
    player.playSound(829)
    player.heal(10)
    player.resetFacePawn()
    player.timers[FOOD_DELAY] = 3
    player.timers[ATTACK_DELAY] = 5
    player.message("You use the bandages.")
    if (player.getSkills().getCurrentLevel(Skills.HITPOINTS) > oldHp) {
        player.message("It heals some health.")
    }
}