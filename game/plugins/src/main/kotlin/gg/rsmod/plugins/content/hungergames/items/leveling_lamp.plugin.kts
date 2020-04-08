package gg.rsmod.plugins.content.hungergames.items

on_item_option(item = Items.LAMP, option = "rub") {
    rub(player)
}

fun rub(player: Player) {
    player.openInterface(134, InterfaceDestination.MAIN_SCREEN)
    player.focusTab(GameframeTab.SKILLS)
}

on_button(134, 3) {
    val skill = Skills.ATTACK
    message(player, skill,"Attack")
}

on_button(134, 4) {
    val skill = Skills.STRENGTH
    message(player, skill,"Strength")
}

on_button(134, 5) {
    val skill = Skills.DEFENCE
    message(player, skill,"Defence")
}

on_button(134, 6) {
    val skill = Skills.RANGED
    message(player, skill,"Range")
}

on_button(134, 7) {
    val skill = Skills.MAGIC
    message(player, skill,"Magic")
}

on_button(134, 8) {
    val skill = Skills.PRAYER
    message(player, skill,"Prayer")
}

on_button(134, 9) {
    player.closeInterface(134)
}

fun message(player: Player, skill: Int, name: String) {
    if(player.getSkills().getCurrentLevel(skill) == 99) {
        player.message("Your $name level is already 99, please select another skill..")
        return
    }
    player.closeInterface(134)
    player.inventory.remove(item = Items.LAMP, beginSlot = player.getInteractingItemSlot())
    val level = player.getSkills().getMaxLevel(skill)+1
    player.getSkills().setBaseLevel(skill, level)
    player.calculateAndSetCombatLevel()
    player.message("You rub the lamp and increase your $name by one level. Your $name level is now $level!")
}