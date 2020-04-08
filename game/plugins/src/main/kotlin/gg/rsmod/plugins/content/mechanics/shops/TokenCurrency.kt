package gg.rsmod.plugins.content.mechanics.shops

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Tom <rspsmods@gmail.com>
 */
class TokenCurrency : ItemCurrency(Items.SURVIVAL_TOKEN, singularCurrency = "token", pluralCurrency = "tokens")