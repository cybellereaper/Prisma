package com.github.astridalia

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent

object CreativeProtection : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        if (damage.isOp) return
        if (damage.gameMode != GameMode.CREATIVE) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemDrag(event: InventoryOpenEvent) {
        val player = event.player
        if (player.isOp) return
        if (player.gameMode != GameMode.CREATIVE) return
        if (event.inventory.type.name.contains("CHEST") || event.inventory.type.name.contains("SHULKER") || event.inventory.type.name.contains(
                "HOPPER"
            )
        ) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.player
        if (player.isOp) return
        if (player.gameMode != GameMode.CREATIVE) return
        if (event.block.type == Material.TNT || event.block.type == Material.TNT_MINECART || event.block.type.name.contains(
                "_ORE"
            )
        ) event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerQuitEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) return
        event.player.gameMode = GameMode.SURVIVAL
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onSpawn(event: PlayerInteractEvent) {
        if (event.player.isOp) return
        if (event.player.gameMode != GameMode.CREATIVE) return
        val item = event.item ?: return
        if (!item.type.name.contains("SPAWN")) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onProjectile(event: PlayerLaunchProjectileEvent) {
        val entity = event.player
        if (entity.isOp) return
        if (entity.gameMode != GameMode.CREATIVE) return
        event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemDrop(event: PlayerDropItemEvent) {
        val player = event.player
        if (player.isOp) return
        if (player.gameMode != GameMode.CREATIVE) return
        event.isCancelled = true
    }
}