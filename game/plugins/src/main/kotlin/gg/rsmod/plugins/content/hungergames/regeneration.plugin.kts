package gg.rsmod.plugins.content.hungergames

import gg.rsmod.plugins.content.inter.attack.AttackTab

val PERK = AttributeKey<String>("perk")
val SCROLLS = TimerKey()
val SPECIAL = TimerKey()


on_login {
    player.timers[SCROLLS] = 10
    player.timers[SPECIAL] = 30
}

on_timer(SCROLLS) {
    player.timers[SCROLLS] = 10
    if(player.attr[PERK] == "Prayer_scroll") { player.getSkills().alterCurrentLevel(Skills.PRAYER, 1) }
    if(player.attr[PERK] == "Hitpoints_scroll") { player.heal(1) }
}

on_timer(SPECIAL) {
    player.timers[SPECIAL] = 30
    val energy = AttackTab.getEnergy(player)
    if(energy <= 90) { AttackTab.setEnergy(player, energy+10) }
}