package com.github.astridalia

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import com.github.astridalia.character.CharacterClasses
import com.github.astridalia.character.CharacterProfile.applyStatistic
import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.removeEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.setEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.entity.Player

@CommandAlias("prisma|pa")
object MyCommands : BaseCommand() {


    enum class CommandOptions {
        REMOVE, ADD, SET
    }

    @CommandAlias("class")
    @CommandPermission("prisma.class")
    fun chooseClass(player: Player, characterClasses: CharacterClasses) {
        player.applyStatistic(characterClasses, 0)
        player.sendMessage("Applied statistics for ${characterClasses.name.lowercase()}")
    }

    @CommandAlias("enchant")
    @CommandPermission("prisma.enchant")
    fun enchant(player: Player, options: CommandOptions, customEnchantments: CustomEnchantments, level: Int) {
        val item = player.inventory.itemInMainHand
        when (options) {
            CommandOptions.ADD -> item.applyEnchantment(customEnchantments, level)
            CommandOptions.SET -> item.setEnchantmentLevel(customEnchantments, level)
            CommandOptions.REMOVE -> item.removeEnchantment(customEnchantments)
        }
    }
}