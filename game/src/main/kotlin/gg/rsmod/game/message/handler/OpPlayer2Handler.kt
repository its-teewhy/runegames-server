package gg.rsmod.game.message.handler

import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.OpPlayer2Message
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.attr.INTERACTING_PLAYER_ATTR
import gg.rsmod.game.model.entity.Client
import java.lang.ref.WeakReference

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer2Handler : MessageHandler<OpPlayer2Message> {

    override fun handle(client: Client, world: World, message: OpPlayer2Message) {
        val index = message.index
        // The interaction option id.
        val option = 2
        // The index of the option in the player's option array.
        val optionIndex = option - 1

        if (!client.lock.canPlayerInteract()) {
            return
        }

        val other = world.players[index] ?: return

        if (client.options[optionIndex] == null || other == client) {
            return
        }

        log(client, "Player option: name=%s, opt=%d", other.username, option)

        client.closeInterfaceModal()
        client.interruptQueues()
        client.resetInteractions()

        client.attr[INTERACTING_PLAYER_ATTR] = WeakReference(other)
        client.attr[INTERACTING_OPT_ATTR] = option
        client.attack(other)
    }
}