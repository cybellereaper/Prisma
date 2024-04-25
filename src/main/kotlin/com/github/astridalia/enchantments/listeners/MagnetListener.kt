package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


object MagnetListener : Listener, KoinComponent {
    private val javaPlugin: JavaPlugin by inject()
    private const val baseRadius = 8.0 // Base radius for item attraction

    @EventHandler
    fun onItemSpawn(event: ItemSpawnEvent) {
        event.entity.getNearbyEntities(baseRadius, baseRadius, baseRadius).forEach { entity ->
            if (entity is Player) {
                val itemInHand = entity.inventory.itemInMainHand
                val magnetLevel = itemInHand.getEnchantOf(CustomEnchantments.MAGNET)
                if (magnetLevel > 0) pullItemToPlayer(event.entity, entity)
            }
        }
    }

    private fun pullItemToPlayer(item: Item, player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                if (!item.isDead && player.isOnline) {
                    if (player.inventory.firstEmpty() == -1) {
                        cancel()
                        return
                    }
                    val itemLocation = item.location
                    val playerLocation = player.location.add(0.0, 1.0, 0.0)
                    val direction = playerLocation.toVector().subtract(itemLocation.toVector()).normalize().multiply(0.1)
                    item.velocity = direction
                } else cancel()
            }
        }.runTaskTimer(javaPlugin, 0L, 1L) // Run this task every second (20 ticks)
    }


}