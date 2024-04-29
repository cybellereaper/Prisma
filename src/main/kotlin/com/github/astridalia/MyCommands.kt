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

    @CommandAlias("heal")
    @CommandPermission("prisma.heal")
    fun heal(player: Player) {
        player.health = player.maxHealth
        player.foodLevel = 20
        player.sendMessage("You have been healed")
    }

    @CommandAlias("god")
    @CommandPermission("prisma.god")
    fun godMode(player: Player) {
        player.isInvulnerable = !player.isInvulnerable
        toggleFlight(player)
        heal(player)
        player.sendMessage("God ${if (player.isInvulnerable) "enabled" else "disabled"}")
    }

    @CommandAlias("flight|fly")
    @CommandPermission("prisma.flight")
    fun toggleFlight(player: Player) {
        // Toggle the allowFlight status
        player.allowFlight = !player.allowFlight

        // Ensure isFlying matches the updated allowFlight status
        if (player.allowFlight) {
            player.isFlying = true
            player.sendMessage("Flight enabled")
        } else {
            player.isFlying = false
            player.sendMessage("Flight disabled")
        }
    }


    @CommandAlias("class")
    @CommandPermission("prisma.class")
    fun chooseClass(player: Player, characterClasses: CharacterClasses) {
        player.applyStatistic(characterClasses, 0)
        player.sendMessage("Applied statistics for ${characterClasses.name.lowercase()}")
    }

    @CommandAlias("enchantments")
    @CommandPermission("prisma.enchant")
    fun enchant(player: Player, options: CommandOptions, customEnchantments: CustomEnchantments, level: Int) {
        val item = player.inventory.itemInMainHand
        when (options) {
            CommandOptions.ADD -> item.applyEnchantment(customEnchantments, level)
            CommandOptions.SET -> item.setEnchantmentLevel(customEnchantments, level)
            CommandOptions.REMOVE -> item.removeEnchantment(customEnchantments)
        }
        player.sendMessage("Applied enchantment ${customEnchantments.displayNameWithColor} with level $level")
    }
}