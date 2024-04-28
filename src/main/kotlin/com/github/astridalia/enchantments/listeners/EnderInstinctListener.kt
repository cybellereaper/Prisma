package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.util.Vector

object EnderInstinctListener : Listener {


    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        val boots = player.inventory.boots ?: return
        val enchantOf = boots.getEnchantmentLevel(CustomEnchantments.ENDER_INSTINCT_DODGE)
        if (enchantOf <= 0) return
        val chance = 0.25 * enchantOf
        if (Math.random() < chance) {
            event.isCancelled = true
            teleportWithEffect(player)
        }
    }

    private fun isSafeLocation(location: Location): Boolean {
        val feet = location.block
        val head = feet.getRelative(BlockFace.UP)
        val ground = feet.getRelative(BlockFace.DOWN)
        return isTransparent(feet) && isTransparent(head) && ground.type.isSolid
    }

    private fun isTransparent(block: Block): Boolean {
        return !block.type.isOccluding && !block.type.isSolid
    }

    private fun teleportWithEffect(player: Player) {


        val originalLocation = player.location.clone()
        val direction = originalLocation.direction.normalize()
        val potentialLocations = listOf(
            originalLocation.add(direction.multiply(2)),
            originalLocation.add(direction.multiply(-2)),
            originalLocation.add(Vector(0.0, 0.0, 2.0)),
            originalLocation.add(Vector(0.0, 0.0, -2.0)),
            originalLocation.add(Vector(2.0, 0.0, 0.0)),
            originalLocation.add(Vector(-2.0, 0.0, 0.0))
        )
        val safeLocation = potentialLocations.find { isSafeLocation(it) }
            ?: originalLocation // Fallback to original if no safe location
        player.teleport(safeLocation)
        player.world.spawnParticle(Particle.EXPLOSION_NORMAL, safeLocation, 10) // Visual effect
        player.world.playSound(safeLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F) // Sound effect
    }
}