package com.github.astridalia

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import com.github.astridalia.character.CharacterClasses
import com.github.astridalia.character.CharacterProfile.applyStatistic
import com.github.astridalia.character.CharacterProfile.getStatistic
import com.github.astridalia.character.CharacterProfile.setStatistic
import com.github.astridalia.character.Statistics
import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.removeEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.setEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

@CommandAlias("prisma|pa")
object MyCommands : BaseCommand() {


    enum class CommandOptions {
        REMOVE, ADD, SET
    }

    @CommandAlias("class")
    fun chooseClass(player: Player, characterClasses: CharacterClasses) {
       player.applyStatistic(characterClasses,0)
        player.sendMessage("Applied statistics for ${characterClasses.name.lowercase()}")
    }

    @CommandAlias("enchant")
    fun enchant(player: Player, options: CommandOptions, customEnchantments: CustomEnchantments, level: Int) {
        val item = player.inventory.itemInMainHand
        when (options) {
            CommandOptions.ADD -> item.applyEnchantment(customEnchantments, level)
            CommandOptions.SET -> item.setEnchantmentLevel(customEnchantments, level)
            CommandOptions.REMOVE -> item.removeEnchantment(customEnchantments)
        }
    }
}