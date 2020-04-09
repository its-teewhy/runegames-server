package gg.rsmod.plugins.content.hungergames

import gg.rsmod.game.model.instance.InstancedChunkSet
import gg.rsmod.game.model.instance.InstancedMap
import gg.rsmod.game.model.instance.InstancedMapConfiguration
import gg.rsmod.plugins.content.inter.attack.AttackTab
import kotlin.random.*

object Constants {
    val IN_LOBBY = AttributeKey<Boolean>("in-lobby")
    val IN_GAME = AttributeKey<Boolean>("in-game")
    val WINS = AttributeKey<Int>("wins")
    val LOSSES = AttributeKey<Int>("losses")
    val KILLS = AttributeKey<Int>("kills")
    val DEATHS = AttributeKey<Int>("deaths")
    val PERK = AttributeKey<String>("perk")

    val MINIMUM_PLAYERS = 2
    val MAXIMUM_PLAYERS = 4

    var startingForest = arrayOf(
            intArrayOf(1278, 3025, 0), intArrayOf(1281, 3025, 0),
            intArrayOf(1290, 3021, 0), intArrayOf(1293, 3018, 0),
            intArrayOf(1297, 3009, 0), intArrayOf(1297, 3006, 0),
            intArrayOf(1293, 2997, 0), intArrayOf(1290, 2994, 0),
            intArrayOf(1281, 2990, 0), intArrayOf(1278, 2990, 0),
            intArrayOf(1269, 2994, 0), intArrayOf(1266, 2997, 0),
            intArrayOf(1262, 3006, 0), intArrayOf(1262, 3009, 0),
            intArrayOf(1266, 3018, 0), intArrayOf(1269, 3021, 0))

    var startingSnow = arrayOf(
            intArrayOf(1278, 2833, 0), intArrayOf(1281, 2833, 0),
            intArrayOf(1290, 2829, 0), intArrayOf(1293, 2826, 0),
            intArrayOf(1297, 2817, 0), intArrayOf(1297, 2814, 0),
            intArrayOf(1293, 2805, 0), intArrayOf(1290, 2802, 0),
            intArrayOf(1281, 2798, 0), intArrayOf(1278, 2798, 0),
            intArrayOf(1269, 2802, 0), intArrayOf(1266, 2805, 0),
            intArrayOf(1262, 2814, 0), intArrayOf(1262, 2817, 0),
            intArrayOf(1266, 2826, 0), intArrayOf(1269, 2829, 0))

    var startingDesert = arrayOf(
            intArrayOf(1278, 2641, 0), intArrayOf(1281, 2641, 0),
            intArrayOf(1290, 2637, 0), intArrayOf(1293, 2634, 0),
            intArrayOf(1297, 2625, 0), intArrayOf(1297, 2622, 0),
            intArrayOf(1293, 2613, 0), intArrayOf(1290, 2610, 0),
            intArrayOf(1281, 2606, 0), intArrayOf(1278, 2606, 0),
            intArrayOf(1269, 2610, 0), intArrayOf(1266, 2613, 0),
            intArrayOf(1262, 2622, 0), intArrayOf(1262, 2625, 0),
            intArrayOf(1266, 2634, 0), intArrayOf(1269, 2637, 0))

    var viewingForest = arrayOf(
            intArrayOf(1279, 3008, 0),
            intArrayOf(1260, 3027, 0),
            intArrayOf(1299, 3027, 0),
            intArrayOf(1299, 2988, 0),
            intArrayOf(1260, 2988, 0))

    var viewingSnow = arrayOf(
            intArrayOf(1279, 2816, 0),
            intArrayOf(1260, 2835, 0),
            intArrayOf(1299, 2835, 0),
            intArrayOf(1299, 3796, 0),
            intArrayOf(1260, 2796, 0))

    var viewingDesert = arrayOf(
            intArrayOf(1279, 2624, 0),
            intArrayOf(1260, 2643, 0),
            intArrayOf(1299, 2643, 0),
            intArrayOf(1299, 2604, 0),
            intArrayOf(1260, 2604, 0))
}

enum class MapInformation(val area: String, val startTile: Tile, val endTile: Tile,  var startingCoords: Array<IntArray>, var viewingCoords: Array<IntArray>) {
    FOREST("Forest", Tile(1216, 2944), Tile(1343, 3071), Constants.startingForest, Constants.viewingForest),
    SNOW("Snow", Tile(1216, 2752), Tile(1343, 2879), Constants.startingSnow, Constants.viewingSnow),
    DESERT("Desert", Tile(1216, 2560), Tile(1343, 2687), Constants.startingDesert, Constants.viewingDesert)
}


val waiting = mutableListOf<Player>()

companion object CachedGames {

    val activeGames = mutableListOf<HungerGameInstance>()

    fun removeGame(instance: HungerGameInstance) {
        activeGames.remove(instance)
    }
}

on_obj_option(obj = Objs.LOBBY_PORTAL, option = "enter") {
    player.queue { dialogue() }
}

suspend fun QueueTask.dialogue() {
    if (!player.equipment.isEmpty) {
        chatNpc("You cannot wear anything into the Hunger Games..", npc = 7316, animation = 567)
    } else if (player.inventory.contains(Items.SURVIVAL_TOKEN)) {
        chatNpc("You cannot bring Survival Tokens into the Hunger Games..", npc = 7316, animation = 567)
    } else if (player.inventory.contains(Items.MYSTERY_BOX)) {
        chatNpc("You cannot bring Mystery Boxes into the Hunger Games..", npc = 7316, animation = 567)
    } else if (player.inventory.contains(Items.LAMP)) {
        chatNpc("You cannot bring Lamps into the Hunger Games..", npc = 7316, animation = 567)
    } else if (!player.inventory.isEmpty && player.inventory.occupiedSlotCount > 1) {
        chatNpc("You can only bring one item into the Hunger Games..", npc = 7316, animation = 567)
    } else {
        chatNpc("Would you like to enter the lobby?", npc = 7316, animation = 567)
        when (options("Yes, sign me up!", "No, nevermind..")) {
            1 -> enterLobby(player)
            2 -> chatPlayer("No, nevermind..", animation = 588)
        }
    }
}

fun enterLobby(player: Player) {
    waiting.add(player)
    player.moveTo(2143, 5534, 3)
    player.attr[Constants.IN_LOBBY] = true
    player.openInterface(interfaceId = 333, dest = InterfaceDestination.WALKABLE)
    updateWaitingInterface()
    if (waiting.size >= Constants.MINIMUM_PLAYERS) {
        world.queue {
            wait(5)
            if(waiting.size < Constants.MINIMUM_PLAYERS)
                terminate()
            world.players.forEach { players -> players.message("A Hunger Games match is about to begin, get in or get out now!") }
            wait(10)
            if(waiting.size < Constants.MINIMUM_PLAYERS)
                terminate()
            when(Random.nextInt(0..2)) {
                0 -> startInstance(MapInformation.FOREST)
                1 -> startInstance(MapInformation.SNOW)
                2 -> startInstance(MapInformation.DESERT)
            }

        }
    }
}

fun updateWaitingInterface() {
    waiting.forEach { player ->
        player.setComponentText(333, 6, "Waiting: ${waiting.size}")
    }
}

fun leaveLobby(player: Player) {
    waiting.remove(player)
    player.moveTo(1247, 3160, 0)
    player.attr[Constants.IN_LOBBY] = false
    player.closeInterface(333)
    updateWaitingInterface()
}

fun startInstance(environment: MapInformation) {
    val startTile = environment.startTile
    val endTile = environment.endTile
    val exitTile = Tile(1247, 3160, 0)
    val instance = generateInstance(startTile, endTile)
    val instanceConfig = InstancedMapConfiguration.Builder()
    instanceConfig.setExitTile(exitTile)

    world.instanceAllocator.allocate(world, environment.ordinal, instance, instanceConfig.build())?.let { map ->
        val gameInstance = HungerGameInstance(map, world, environment)
        CachedGames.activeGames.add(gameInstance)
        waiting.shuffle()
        if(waiting.size >= Constants.MAXIMUM_PLAYERS) {
            waiting.take(Constants.MAXIMUM_PLAYERS).forEach { player ->
                waiting.remove(player)
                gameInstance.addPlayer(player)
            }
        } else if(waiting.size >= Constants.MINIMUM_PLAYERS) {
            waiting.take(waiting.size).forEach { player ->
                waiting.remove(player)
                gameInstance.addPlayer(player)
            }
        }
    }


    updateWaitingInterface()
}

fun generateInstance(startTile: Tile, endTile: Tile): InstancedChunkSet {
    val numChunksX = (endTile.x - startTile.x) / 8
    val numChunksZ = (endTile.z - startTile.z) / 8
    val instanceChunkSet = InstancedChunkSet.Builder()

    for (z in (0 until (numChunksZ - 1))) {
        for (x in (0 until (numChunksX - 1))) {
            instanceChunkSet.set(chunkX = (x), chunkZ = (z), height = 0, rot = 0, copy = Tile(x = (startTile.x + (x * 8)), z = (startTile.z + (z * 8)), height = 0))
        }
    }
    return instanceChunkSet.build()
}

class HungerGameInstance(val instancedMap: InstancedMap, val world: World, val environment: MapInformation) {
    val players = mutableListOf<Player>()
    val spectating = mutableListOf<Player>()

    fun updateFightingInterface() {
        players.forEach { sendFightingInterface(it) }
        spectating.forEach { sendFightingInterface(it) }
    }

    fun sendFightingInterface(player: Player) {
        player.setComponentText(333, 6, "Fighting: ${players.size}")
        player.setComponentText(333, 7, "Spectating: ${spectating.size}")
        player.setComponentText(333, 8, "Fight area: ${environment.area}")
    }


    fun finish() {
        if (players.size > 1) {
            return
        }
        spectating.forEach { player ->
            stop(player)
        }
        players.forEach { player ->
            reset(player)
            reward(player)
            stop(player)
        }
        spectating.clear()
        players.clear()
        world.instanceAllocator.deallocate(world, instancedMap)
        CachedGames.removeGame(this)
    }

    fun stop(player: Player) {
        if(spectating.contains(player)) {
            player.invisible = false
            player.message("You have been defeated, better luck next time!")
            player.message("You have received 1 Survival Token, thank you for participating.")
            player.inventory.add(Items.SURVIVAL_TOKEN, 1)
            player.closeInterface(interfaceId = 374)
            player.closeInterface(interfaceId = 231)
        }
        player.unlock()
        player.closeInterface(interfaceId = 333)
        player.moveTo(instancedMap.exitTile)
    }

    fun reward(player: Player) {
        player.attr[Constants.WINS] = +1
        player.inventory.add(Items.SURVIVAL_TOKEN, 10)
        if(player.attr[Constants.WINS] == null) { player.attr[Constants.WINS] = 0 }
        if(player.attr[Constants.LOSSES] == null) { player.attr[Constants.LOSSES] = 0 }
        if(player.attr[Constants.KILLS] == null) { player.attr[Constants.KILLS] = 0 }
        if(player.attr[Constants.DEATHS] == null) { player.attr[Constants.DEATHS] = 0 }
        player.openInterface(interfaceId = 645, dest = InterfaceDestination.MAIN_SCREEN)
        player.setComponentText(interfaceId = 645, component = 6, text = "Kills: ${player.attr[Constants.KILLS].toString()}")
        player.setComponentText(interfaceId = 645, component = 7, text = "Deaths: ${player.attr[Constants.DEATHS].toString()}")
        player.setComponentText(interfaceId = 645, component = 8, text = "Wins: ${player.attr[Constants.WINS].toString()}")
        player.setComponentText(interfaceId = 645, component = 9, text = "Losses: ${player.attr[Constants.LOSSES].toString()}")
        world.players.forEach { players -> players.message("[<col=801700>Announcement</col>]: ${player.username} has just won the Hunger Games!") }
        player.animate(862)
    }

    fun move(player: Player, component: Int) {
        val target = Tile(environment.viewingCoords[component][0], environment.viewingCoords[component][1], 0)
        val entry = Tile(instancedMap.area.bottomLeft.x + (target.x - environment.startTile.x), instancedMap.area.bottomLeft.z + (target.z - environment.startTile.z), 0)
        player.moveTo(entry)
    }

    fun spectate(player: Player) {
        player.lock()
        player.invisible = true
        player.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = 374)
        player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 231)
        player.setComponentNpcHead(interfaceId = 231, component = 1, npc = 7316)
        player.setComponentAnim(interfaceId = 231, component = 1, anim = 567)
        player.setComponentText(interfaceId = 231, component = 2, text = player.world.definitions.get(NpcDef::class.java, 7316).name)
        player.setComponentText(interfaceId = 231, component = 4, text = "You are currently spectating the Hunger Games.<br>Please stop viewing to be returned to the home area.")
        player.runClientScript(600, 1, 1, 16, 15138820)
        move(player, 0)
    }

    fun reset(player: Player) {
        player.attr[Constants.IN_GAME] = false
        player.attr[Constants.PERK] = ""
        player.closeInterface(dest = InterfaceDestination.PVP_OVERLAY)
        player.removeOption(2)
        player.inventory.removeAll()
        player.equipment.removeAll()
        player.sendWeaponComponentInformation()
        player.getSkills().restoreAll()
        player.clearHits()
        player.attr.removeIf { it.resetOnDeath }
        player.timers.removeIf { it.resetOnDeath }
        player.runEnergy = 100.0
        player.sendRunEnergy(player.runEnergy.toInt())
        AttackTab.setEnergy(player, 100)
    }

    fun remove(player: Player, logout: Boolean) {
        reset(player)
        players.remove(player)
        if(logout) { stop(player) }
        if(!logout && players.size > 0) {
            spectating.add(player)
            spectate(player)
        }
        if (players.size < 2) {
            world.queue {
                wait(3)
                finish()
            }
        }
        updateFightingInterface()
    }

    fun death(player: Player) {
        remove(player, false)
        player.damageMap.getMostDamage()?.let { killer ->
            killer.attr[Constants.KILLS] = +1
        }
        player.attr[Constants.DEATHS] = +1
        player.attr[Constants.LOSSES] = +1
    }

    fun logout(player: Player) {
        remove(player, true)
        player.attr[Constants.LOSSES] = +1
    }

    fun teleport(player: Player) {
        val randomArray = (Math.random() * environment.startingCoords.size).toInt()
        val target = Tile(environment.startingCoords[randomArray][0], environment.startingCoords[randomArray][1],0)
        val entry = Tile(instancedMap.area.bottomLeft.x + (target.x - environment.startTile.x), instancedMap.area.bottomLeft.z + (target.z - environment.startTile.z),0)
        player.moveTo(entry)
    }

    fun addPlayer(player: Player) {
        players.add(player)
        player.attr[Constants.IN_LOBBY] = false
        player.attr[Constants.IN_GAME] = true
        player.queue {
            teleport(player)
            player.lock()
            player.openInterface(dest = InterfaceDestination.PVP_OVERLAY, interfaceId = 90)
            player.sendOption("Attack", 2)
            wait(2)
            player.forceChat("5..")
            wait(2)
            player.forceChat("4..")
            wait(2)
            player.forceChat("3..")
            wait(2)
            player.forceChat("2..")
            wait(2)
            player.forceChat("1..")
            wait(2)
            player.forceChat("Let the games begin!")

            sendFightingInterface(player)
            player.unlock()
        }
    }
}

fun getGameInstance(player: Player): HungerGameInstance? = CachedGames.activeGames.find { game -> game.players.contains(player) }
fun getSpectatingInstance(player: Player): HungerGameInstance? = CachedGames.activeGames.find { game -> game.spectating.contains(player) }

on_logout {
    if (player.attr[Constants.IN_LOBBY] == true) {
        leaveLobby(player)
    } else if (player.attr[Constants.IN_GAME] == true) {
        getGameInstance(player)?.logout(player)
    }
}

on_player_death {
    if (player.attr[Constants.IN_GAME] == true) {
        getGameInstance(player)?.death(player)
    }
}

on_button(interfaceId = 374, component = 5) {
    getSpectatingInstance(player)?.stop(player)
    getSpectatingInstance(player)?.updateFightingInterface()
}

on_button(interfaceId = 374, component = 11) {
    getSpectatingInstance(player)?.move(player, 0)
}

on_button(interfaceId = 374, component = 12) {
    getSpectatingInstance(player)?.move(player, 1)
}

on_button(interfaceId = 374, component = 13) {
    getSpectatingInstance(player)?.move(player, 2)
}

on_button(interfaceId = 374, component = 14) {
    getSpectatingInstance(player)?.move(player, 3)
}

on_button(interfaceId = 374, component = 15) {
    getSpectatingInstance(player)?.move(player, 4)
}