package gg.rsmod.plugins.content.hungergames.items

import gg.rsmod.game.model.timer.POTION_DELAY

val IN_GAME = AttributeKey<Boolean>("in-game")

fun canUse(player: Player): Boolean = player.attr[IN_GAME] == true
fun canDrink(player: Player): Boolean = !player.timers.has(POTION_DELAY)

data class Potions(val id: Int, val id2: Int, val id3: Int, val id4: Int, val name: String, val stat: Int, val boost: Int)

private val potions = setOf(
        Potions(Items.ATTACK_POTION4, Items.ATTACK_POTION3, Items.ATTACK_POTION2, Items.ATTACK_POTION1, "Attack_potion", Skills.ATTACK, 3),
        Potions(Items.STRENGTH_POTION4, Items.STRENGTH_POTION3, Items.STRENGTH_POTION2, Items.STRENGTH_POTION1, "Strength_potion", Skills.STRENGTH, 3),
        Potions(Items.DEFENCE_POTION4, Items.DEFENCE_POTION3, Items.DEFENCE_POTION2, Items.DEFENCE_POTION1, "Defence_potion", Skills.DEFENCE, 3),
        Potions(Items.RANGING_POTION4, Items.RANGING_POTION3, Items.RANGING_POTION2, Items.RANGING_POTION1, "Range_potion", Skills.RANGED, 4),
        Potions(Items.MAGIC_POTION4, Items.MAGIC_POTION3, Items.MAGIC_POTION2, Items.MAGIC_POTION1, "Magic_potion", Skills.MAGIC, 4),
        Potions(Items.PRAYER_POTION4, Items.PRAYER_POTION3, Items.PRAYER_POTION2, Items.PRAYER_POTION1, "Prayer_potion", Skills.PRAYER, 7),
        Potions(Items.SARADOMIN_BREW4, Items.SARADOMIN_BREW3, Items.SARADOMIN_BREW2, Items.SARADOMIN_BREW1, "Saradomin_brew", -1, 5))

potions.forEach { potion ->
    on_item_option(item = potion.id, option = "drink") {
        if(canUse(player)) {
            if(canDrink(player)) {
                val inventorySlot = player.getInteractingItemSlot()
                if (player.inventory.remove(item = potion.id, beginSlot = inventorySlot).hasSucceeded()) {
                    player.inventory.add(item = potion.id2, beginSlot = inventorySlot)
                }
                usePotion(player, potion)
            }
        } else player.message("You may only use potions in games..")
    }
    on_item_option(item = potion.id2, option = "drink") {
            if(canDrink(player)) {
                val inventorySlot = player.getInteractingItemSlot()
                if (player.inventory.remove(item = potion.id2, beginSlot = inventorySlot).hasSucceeded()) {
                    player.inventory.add(item = potion.id3, beginSlot = inventorySlot)
                }
                usePotion(player, potion)
            }
    }
    on_item_option(item = potion.id3, option = "drink") {
            if(canDrink(player)) {
                val inventorySlot = player.getInteractingItemSlot()
                if (player.inventory.remove(item = potion.id3, beginSlot = inventorySlot).hasSucceeded()) {
                    player.inventory.add(item = potion.id4, beginSlot = inventorySlot)
                }
                usePotion(player, potion)
            }
    }
    on_item_option(item = potion.id4, option = "drink") {
            if(canDrink(player)) {
                val inventorySlot = player.getInteractingItemSlot()
                if (player.inventory.remove(item = potion.id4, beginSlot = inventorySlot).hasSucceeded()) {
                    player.inventory.add(item = Items.VIAL, beginSlot = inventorySlot)
                }
                usePotion(player, potion)
            }
    }
}

fun usePotion(player: Player, potion: Potions) {
    val skill = player.getSkills()
    val attack = player.getSkills().getMaxLevel(Skills.ATTACK)
    val strength = player.getSkills().getMaxLevel(Skills.STRENGTH)
    val defence = player.getSkills().getMaxLevel(Skills.DEFENCE)
    val range = player.getSkills().getMaxLevel(Skills.RANGED)
    val magic = player.getSkills().getMaxLevel(Skills.MAGIC)
    val prayer = player.getSkills().getMaxLevel(Skills.PRAYER)
    val hitpoints = player.getSkills().getMaxLevel(Skills.HITPOINTS)
    player.animate(829)
    player.playSound(2401)
    player.timers[POTION_DELAY] = 3
    if(potion.stat == -1) {
        player.getSkills().alterCurrentLevel(Skills.ATTACK, -(attack * 0.10 + 2).toInt(), -(attack * 0.10 + 2).toInt())
        player.getSkills().alterCurrentLevel(Skills.STRENGTH, -(strength * 0.10 + 2).toInt(), -(strength * 0.10 + 2).toInt())
        player.getSkills().alterCurrentLevel(Skills.RANGED, -(range * 0.10 + 2).toInt(), -(range * 0.10 + 2).toInt())
        player.getSkills().alterCurrentLevel(Skills.MAGIC, -(magic * 0.10 + 2).toInt(), -(magic * 0.10 + 2).toInt())
        player.getSkills().alterCurrentLevel(Skills.DEFENCE, (defence * 0.20 + 2).toInt(), (defence * 0.20 + 2).toInt())
        player.getSkills().alterCurrentLevel(Skills.HITPOINTS, (hitpoints * 0.15 + 2).toInt(), (hitpoints * 0.15 + 2).toInt())
    } else if (potion.stat == Skills.PRAYER){
        player.getSkills().alterCurrentLevel(potion.stat, (prayer * 0.25 + potion.boost).toInt(), 0)
    } else if (potion.stat == Skills.MAGIC){
        player.getSkills().alterCurrentLevel(potion.stat, potion.boost, potion.boost)
    } else {
        player.getSkills().alterCurrentLevel(potion.stat, (skill.getMaxLevel(potion.stat) * 0.10 + potion.boost).toInt(), (skill.getMaxLevel(potion.stat) * 0.10 + potion.boost).toInt())
    }
    player.message("You drink some of your ${potion.name.replace("_", " ")}.")
    when(player.getInteractingItemId()) {
        potion.id -> player.message("You have 3 doses of potion left.")
        potion.id2 -> player.message("You have 2 doses of potion left.")
        potion.id3 -> player.message("You have 1 dose of potion left.")
        potion.id4 -> player.message("You have finished your potion.")
    }
}