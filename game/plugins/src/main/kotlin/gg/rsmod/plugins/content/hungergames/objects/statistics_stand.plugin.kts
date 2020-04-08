package gg.rsmod.plugins.content.hungergames.objects

val WINS = AttributeKey<Int>("wins")
val LOSSES = AttributeKey<Int>("losses")
val KILLS = AttributeKey<Int>("kills")
val DEATHS = AttributeKey<Int>("deaths")

on_obj_option(obj = Objs.STAND_23632, option = "check") {
    check_stats(player)
}

fun check_stats(player: Player) {

    if(player.attr[WINS] == null) { player.attr[WINS] = 0 }
    if(player.attr[LOSSES] == null) { player.attr[LOSSES] = 0 }
    if(player.attr[KILLS] == null) { player.attr[KILLS] = 0 }
    if(player.attr[DEATHS] == null) { player.attr[DEATHS] = 0 }

    player.message("Wins: <col=8B0000>" +player.attr[WINS].toString()+ "</col> Losses: <col=8B0000>" +player.attr[LOSSES].toString()+
            "</col> Kills: <col=8B0000>"+player.attr[KILLS].toString()+ "</col> Deaths: <col=8B0000>" +player.attr[DEATHS].toString()+ "</col>")
}