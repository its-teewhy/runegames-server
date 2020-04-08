package gg.rsmod.plugins.content.hungergames.items

val PERK = AttributeKey<String>("perk")
val IN_GAME = AttributeKey<Boolean>("in-game")

fun canUse(player: Player): Boolean = player.attr[IN_GAME] == true
fun canStillUse(player: Player): Boolean = player.attr[PERK] == ""

data class PerkScrolls(val id: Int, val name: String, val message: String)

private val scrolls = setOf(
        PerkScrolls(id = Items.ATTACK_SCROLL, name = "Attack_scroll", message = "15% accuracy bonus"),
        PerkScrolls(id = Items.STRENGTH_SCROLL, name = "Strength_scroll", message = "15% damage bonus"),
        PerkScrolls(id = Items.DEFENCE_SCROLL, name = "Defence_scroll", message = "15% defence bonus"),
        PerkScrolls(id = Items.RANGE_SCROLL, name = "Range_scroll", message = "10% accuracy and 10% damage bonus"),
        PerkScrolls(id = Items.MAGIC_SCROLL, name = "Magic_scroll", message = "10% accuracy and 10% damage bonus"),
        PerkScrolls(id = Items.PRAYER_SCROLL, name = "Prayer_scroll", message = "prayer regeneration and a prayer potion"),
        PerkScrolls(id = Items.HITPOINTS_SCROLL, name = "Hitpoints_scroll", message = "hitpoints regeneration and a bandage"))

scrolls.forEach { scroll ->
    on_item_option(item = scroll.id, option = "read") {
        if(canUse(player)) {
            if(canStillUse(player)) {
                usePerk(player, scroll)
            }
        } else player.message("You may only use perk scrolls in games..")
    }
}

fun usePerk(player: Player, scroll: PerkScrolls) {
    player.inventory.remove(item = scroll.id, beginSlot = player.getInteractingItemSlot())
    player.attr[PERK] = scroll.name
    player.message("You have used the ${scroll.name.replace("_", " ")}, you get a ${scroll.message}.")
    if(scroll.id == Items.PRAYER_SCROLL) {
        player.inventory.add(item = Items.PRAYER_POTION4, beginSlot = player.getInteractingItemSlot())
        player.message("A prayer potion has been added to your inventory!")
    } else if(scroll.id == Items.HITPOINTS_SCROLL) {
        player.inventory.add(item = Items.BANDAGES, beginSlot = player.getInteractingItemSlot())
        player.message("A bandage has been added to your inventory!")
    }
}