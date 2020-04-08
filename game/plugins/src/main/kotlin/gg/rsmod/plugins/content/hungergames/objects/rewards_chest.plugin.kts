package gg.rsmod.plugins.content.hungergames.objects

import gg.rsmod.game.model.shop.Shop
import gg.rsmod.plugins.content.mechanics.shops.TokenCurrency

on_obj_option(obj = Objs.REWARD_CHEST, option = "open") {
    open_shop(player)
}

fun open_shop(player: Player) {
    player.openShop("Survival Store")
}

create_shop("Survival Store", TokenCurrency(), StockType.NORMAL, Shop.DEFAULT_STOCK_SIZE, PurchasePolicy.BUY_STOCK) {
    items[0] = ShopItem(Items.MYSTERY_BOX, 100, sellPrice = 100, buyPrice = null)
    items[1] = ShopItem(Items.AMULET_OF_STRENGTH, 100, sellPrice = 100, buyPrice = null)
    items[2] = ShopItem(Items.AMULET_OF_DEFENCE, 100, sellPrice = 100, buyPrice = null)
    items[3] = ShopItem(Items.AMULET_OF_ACCURACY, 100, sellPrice = 100, buyPrice = null)
    items[4] = ShopItem(Items.AMULET_OF_MAGIC, 100, sellPrice = 100, buyPrice = null)
    items[5] = ShopItem(Items.AMULET_OF_POWER, 100, sellPrice = 100, buyPrice = null)
    items[6] = ShopItem(Items.AMULET_OF_FURY, 100, sellPrice = 100, buyPrice = null)
    items[7] = ShopItem(Items.AMULET_OF_TORTURE, 100, sellPrice = 100, buyPrice = null)

    items[8] = ShopItem(Items.WARRIOR_RING, 100, sellPrice = 100, buyPrice = null)
    items[9] = ShopItem(Items.BERSERKER_RING, 100, sellPrice = 100, buyPrice = null)
    items[10] = ShopItem(Items.ARCHERS_RING, 100, sellPrice = 100, buyPrice = null)
    items[11] = ShopItem(Items.SEERS_RING, 100, sellPrice = 100, buyPrice = null)
    items[12] = ShopItem(Items.TREASONOUS_RING, 100, sellPrice = 100, buyPrice = null)
    items[13] = ShopItem(Items.RING_OF_THE_GODS, 100, sellPrice = 100, buyPrice = null)
    items[14] = ShopItem(Items.BRIMSTONE_RING, 100, sellPrice = 100, buyPrice = null)
    items[15] = ShopItem(Items.RING_OF_SUFFERING, 100, sellPrice = 100, buyPrice = null)

    items[16] = ShopItem(Items.AVAS_ACCUMULATOR, 100, sellPrice = 100, buyPrice = null)
    items[17] = ShopItem(Items.CAPE_OF_LEGENDS, 100, sellPrice = 100, buyPrice = null)
    items[18] = ShopItem(Items.OBSIDIAN_CAPE, 100, sellPrice = 100, buyPrice = null)
    items[19] = ShopItem(Items.SARADOMIN_CAPE, 100, sellPrice = 100, buyPrice = null)
    items[20] = ShopItem(Items.ZAMORAK_CAPE, 100, sellPrice = 100, buyPrice = null)
    items[21] = ShopItem(Items.GUTHIX_CAPE, 100, sellPrice = 100, buyPrice = null)
    items[22] = ShopItem(Items.FIRE_CAPE, 100, sellPrice = 100, buyPrice = null)
    items[23] = ShopItem(Items.INFERNAL_CAPE, 100, sellPrice = 100, buyPrice = null)

    items[24] = ShopItem(Items.BANDAGES, 100, sellPrice = 100, buyPrice = null)
    items[25] = ShopItem(Items.ATTACK_POTION4, 100, sellPrice = 100, buyPrice = null)
    items[26] = ShopItem(Items.STRENGTH_POTION4, 100, sellPrice = 100, buyPrice = null)
    items[27] = ShopItem(Items.DEFENCE_POTION4, 100, sellPrice = 100, buyPrice = null)
    items[28] = ShopItem(Items.RANGING_POTION4, 100, sellPrice = 100, buyPrice = null)
    items[29] = ShopItem(Items.MAGIC_POTION4, 100, sellPrice = 100, buyPrice = null)
    items[30] = ShopItem(Items.PRAYER_POTION4, 100, sellPrice = 100, buyPrice = null)
    items[31] = ShopItem(Items.SARADOMIN_BREW4, 100, sellPrice = 100, buyPrice = null)

    items[32] = ShopItem(Items.LAMP, 100, sellPrice = 100, buyPrice = null)
    items[33] = ShopItem(Items.ATTACK_SCROLL, 100, sellPrice = 100, buyPrice = null)
    items[34] = ShopItem(Items.STRENGTH_SCROLL, 100, sellPrice = 100, buyPrice = null)
    items[35] = ShopItem(Items.DEFENCE_SCROLL, 100, sellPrice = 100, buyPrice = null)
    items[36] = ShopItem(Items.RANGE_SCROLL, 100, sellPrice = 100, buyPrice = null)
    items[37] = ShopItem(Items.MAGIC_SCROLL, 100, sellPrice = 100, buyPrice = null)
    items[38] = ShopItem(Items.PRAYER_SCROLL, 100, sellPrice = 100, buyPrice = null)
    items[39] = ShopItem(Items.HITPOINTS_SCROLL, 100, sellPrice = 100, buyPrice = null)
}