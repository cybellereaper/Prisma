package com.github.astridalia

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import com.github.astridalia.enchantments.CustomEnchantment.setEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.entity.Player

@CommandAlias("prisma|pa")
object MyCommands : BaseCommand(){
    @CommandAlias("enchant")
    fun enchant(player: Player, customEnchantments: CustomEnchantments, level: Int) {
        val item = player.inventory.itemInMainHand
        item.setEnchantmentLevel(customEnchantments, level)
    }
}