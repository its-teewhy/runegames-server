package gg.rsmod.plugins.content.hungergames.items

import gg.rsmod.game.fs.DefinitionSet
import kotlin.random.Random
import kotlin.random.nextInt

var common = arrayOf(Items.AMULET_OF_STRENGTH, Items.AMULET_OF_DEFENCE, Items.AMULET_OF_ACCURACY, Items.AMULET_OF_MAGIC,
        Items.AMULET_OF_POWER, Items.WARRIOR_RING, Items.BERSERKER_RING, Items.ARCHERS_RING, Items.SEERS_RING, Items.AVAS_ACCUMULATOR,
        Items.CAPE_OF_LEGENDS, Items.BANDAGES, Items.ATTACK_POTION4, Items.STRENGTH_POTION4, Items.DEFENCE_POTION4,
        Items.RANGING_POTION4, Items.MAGIC_POTION4)

var uncommon = arrayOf(Items.AMULET_OF_FURY, Items.TREASONOUS_RING, Items.RING_OF_THE_GODS, Items.OBSIDIAN_CAPE, Items.SARADOMIN_CAPE,
        Items.ZAMORAK_CAPE, Items.GUTHIX_CAPE, Items.PRAYER_POTION4, Items.ATTACK_SCROLL, Items.STRENGTH_SCROLL, Items.DEFENCE_SCROLL,
        Items.RANGE_SCROLL, Items.MAGIC_SCROLL)


var rare = arrayOf(Items.AMULET_OF_TORTURE, Items.RING_OF_SUFFERING, Items.BRIMSTONE_RING, Items.FIRE_CAPE, Items.INFERNAL_CAPE,
        Items.SARADOMIN_BREW4, Items.LAMP, Items.PRAYER_SCROLL, Items.HITPOINTS_SCROLL)

on_item_option(Items.MYSTERY_BOX, "open") {
    useMysteryBox(player, player.getInteractingItem())
}

fun useMysteryBox(player: Player, item: Item) {
    player.inventory.remove(item = item, beginSlot = player.getInteractingItemSlot())
    val list = Random.nextInt(0..14)
    val common = common.get(Random.nextInt(0..common.size.minus(1)))
    val uncommon = uncommon.get(Random.nextInt(0..uncommon.size.minus(1)))
    val rare = rare.get(Random.nextInt(0..rare.size.minus(1)))
    when(list) {
        in 0..9 -> {
            player.inventory.add(item = common, beginSlot = player.getInteractingSlot())
            player.message("You have received a common item from the mystery box.")
        }
        in 10..13 -> {
            player.inventory.add(item = uncommon, beginSlot = player.getInteractingSlot())
            player.message("You have received a uncommon item from the mystery box.")
        }
        14 -> {
            player.inventory.add(item = rare, beginSlot = player.getInteractingSlot())
            player.message("You have received a rare item from the mystery box.")
            world.players.forEach { players -> players.message("[<col=801700>Announcement</col>]: ${player.username} has just received a rare item from a mystery box!") }
        }
    }
}