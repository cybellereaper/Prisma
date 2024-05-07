package com.github.astridalia

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Flags
import com.github.astridalia.character.CharacterClasses
import com.github.astridalia.character.CharacterProfile.applyStatistic
import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.removeEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.setEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent

@CommandAlias("prisma|pa")
object MyCommands : BaseCommand(), KoinComponent {
    enum class CommandOptions {
        REMOVE, ADD, SET
    }

    @CommandAlias("heal")
    @CommandPermission("prisma.heal.other")
    fun heal(target: Player) {
        target.health = target.maxHealth
        target.foodLevel = 20
        target.sendMessage("You have been healed")
    }

    @CommandAlias("worldtp")
    @CommandPermission("prisma.worldtp")
    fun worldTeleport(player: Player, world: String) {
        val world1 = Bukkit.getWorld(world) ?: run {
            player.sendMessage("World $world does not exist")
            return
        }
        player.sendMessage("Teleported to $world")
        player.teleport(world1.spawnLocation)
    }

    @CommandAlias("enderchest")
    @CommandPermission("prisma.enderchest.other")
    fun enderChest(player: Player, @Flags("other") target: Player) {
        player.openInventory(target.enderChest)
    }

    @CommandAlias("invsee")
    @CommandPermission("prisma.invsee")
    fun invSee(player: Player, @Flags("other") target: Player) {
        player.openInventory(target.inventory)
        player.sendMessage("Opened inventory of ${target.name}")
    }

    @CommandAlias("god")
    @CommandPermission("prisma.god")
    fun godMode(player: Player) {
        player.isInvulnerable = !player.isInvulnerable
        toggleFlight(player)
        heal(player)
        player.sendMessage("God ${if (player.isInvulnerable) "enabled" else "disabled"}")
    }

    @CommandAlias("home")
    @CommandPermission("prisma.home")
    fun home(player: Player) {
        player.sendMessage("Teleported to home")
        player.teleport(player.world.spawnLocation)
    }

    @CommandAlias("flight|fly")
    @CommandPermission("prisma.flight")
    fun toggleFlight(target: Player) {
        // Toggle the allowFlight status
        target.allowFlight = !target.allowFlight

        // Ensure isFlying matches the updated allowFlight status
        if (target.allowFlight) {
            target.isFlying = true
            target.sendMessage("Flight enabled")
        } else {
            target.isFlying = false
            target.sendMessage("Flight disabled")
        }
    }



    @CommandAlias("class")
    @CommandPermission("prisma.class")
    fun chooseClass(characterClasses: CharacterClasses, @Flags("other") target: Player) {
        target.applyStatistic(characterClasses, 0)
        target.sendMessage("Applied statistics for ${characterClasses.name.lowercase()}")
    }

    @CommandAlias("ride")
    @CommandPermission("prisma.ride")
    fun ride(player: Player, @Flags("other") target: Player) {
        if (player === target) {
            player.sendMessage("You cannot ride yourself")
            return
        }
        target.sendMessage("You have been ride by ${player.name}")
        player.sendMessage("You have ride ${target.name}")
        target.addPassenger(player)
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