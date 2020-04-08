package gg.rsmod.plugins.content.hungergames.objects

import gg.rsmod.plugins.api.cfg.Objs
import kotlin.random.Random
import kotlin.random.nextInt

private val CRATES = setOf(Objs.CRATE_30810, Objs.CRATE_29302, Objs.CRATE_15707)
private val CHESTS = setOf(Objs.CRATE_1)

var offensive = arrayOf(Items.ARMADYL_GODSWORD, Items.SARADOMIN_GODSWORD, Items.ZAMORAK_GODSWORD, Items.BANDOS_GODSWORD,
        Items.DRAGON_CLAWS, Items.DRAGON_SCIMITAR, Items.DRAGON_DAGGER, Items.DRAGON_LONGSWORD, Items.DRAGON_HALBERD,
        Items.RUNE_SCIMITAR, Items.RUNE_2H_SWORD, Items.GRANITE_MAUL, Items.ELDER_MAUL, Items.ABYSSAL_WHIP)

var defensive = arrayOf(Items.BANDOS_CHESTPLATE, Items.BANDOS_TASSETS, Items.FIGHTER_TORSO, Items.DRAGON_PLATELEGS,
        Items.RUNE_FULL_HELM, Items.RUNE_PLATEBODY, Items.RUNE_PLATELEGS, Items.RUNE_KITESHIELD, Items.DHAROKS_HELM,
        Items.DHAROKS_PLATEBODY, Items.DHAROKS_PLATELEGS, Items.BARROWS_GLOVES, Items.DRAGONFIRE_SHIELD,
        Items.SPIRIT_SHIELD, Items.BLESSED_SPIRIT_SHIELD, Items.ELYSIAN_SPIRIT_SHIELD, Items.ARCANE_SPIRIT_SHIELD,
        Items.BANDOS_BOOTS, Items.DRAGON_BOOTS, Items.DRAGON_GLOVES, Items.RUNE_BOOTS, Items.RUNE_GLOVES,
        Items.WARRIOR_HELM, Items.BERSERKER_HELM, Items.HELM_OF_NEITIZNOT, Items.DRAGON_FULL_HELM)

var consumables = arrayOf(Items.DARK_CRAB, Items.MANTA_RAY, Items.SHARK, Items.COOKED_KARAMBWAN)

CRATES.forEach { crate ->
    on_obj_option(obj = crate, option = "search") {
        searchCrate(player, player.getInteractingGameObj())
    }
}

CHESTS.forEach { crate ->
    on_obj_option(obj = crate, option = "search") {
        searchChest(player, player.getInteractingGameObj())
    }
}

fun searchCrate(player: Player, obj: GameObject) {
    player.queue() {
        player.lock()
        player.animate(832)
        wait(2)
        world.spawn(DynamicObject(obj, obj.id.plus(1)))
        player.message("You search the crate and find something.")
        val random = Random.nextInt(0..3)
        if(random in 0..1) {
            player.inventory.add(offensive.random(), 1)
            player.inventory.add(Items.BANDAGES, 5)
        } else if (random in 2..3) {
            player.inventory.add(defensive.random(), 1)
            player.inventory.add(Items.BANDAGES, 5)
        }
        player.unlock()
    }
}

fun searchChest(player: Player, obj: GameObject) {
    player.queue() {
        player.lock()
        player.animate(832)
        wait(2)
        world.spawn(DynamicObject(obj, obj.id.plus(1)))
        player.message("You search the chest and find something.")
        val random = Random.nextInt(0..5)
        if (random in 0..1) {
            player.inventory.add(offensive.random(), 1)
        } else if (random in 2..3) {
            player.inventory.add(defensive.random(), 1)
        } else if (random in 4..5) {
            player.inventory.add(consumables.random(), 3)
        }
        player.unlock()
    }
}