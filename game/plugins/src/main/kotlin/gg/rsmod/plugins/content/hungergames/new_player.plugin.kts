package gg.rsmod.plugins.content.hungergames

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.game.sync.block.UpdateBlockType
import gg.rsmod.plugins.api.ext.calculateAndSetCombatLevel
import gg.rsmod.plugins.api.ext.chatNpc
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.selectAppearance

on_login {
    val newAccount = player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login
    if (newAccount) {
        player.queue {
            for (i in 0 until 7) {
                player.getSkills().setBaseLevel(i, 50)
            }
            player.getSkills().setBaseLevel(Skills.PRAYER, 43)
            player.getSkills().setBaseLevel(Skills.HITPOINTS, 99)
            player.calculateAndSetCombatLevel()
            this.chatNpc("Welcome to RunesGames.<br>May the odds be in your favor!", npc = 7316, animation = 567)
            player.queue(TaskPriority.WEAK) {
                val appearance = selectAppearance() ?: return@queue
                player.appearance = appearance
                player.addBlock(UpdateBlockType.APPEARANCE)
            }
        }
    }
}