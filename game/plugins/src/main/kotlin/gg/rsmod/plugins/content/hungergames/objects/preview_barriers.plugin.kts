package gg.rsmod.plugins.content.hungergames.objects

on_obj_option(obj = Objs.FOREST_BARRIER, option = "use") {
    player.queue {
        this.chatNpc("This is a preview of what the forest area looks like." +
                "<br>Use the Lobby Portal to enter the lobby for a game.", npc = 7316, animation = 567)
        return@queue
    }
}

on_obj_option(obj = Objs.SNOW_BARRIER, option = "use") {
    player.queue {
        this.chatNpc("This is a preview of what the snow area looks like." +
                "<br>Use the Lobby Portal to enter the lobby for a game.", npc = 7316, animation = 567)
        return@queue
    }
}

on_obj_option(obj = Objs.DESERT_BARRIER, option = "use") {
    player.queue {
        this.chatNpc("This is a preview of what the desert area looks like." +
                "<br>Use the Lobby Portal to enter the lobby for a game.", npc = 7316, animation = 567)
        return@queue
    }
}