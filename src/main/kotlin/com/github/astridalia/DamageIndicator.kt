package com.github.astridalia

import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DamageIndicator : Listener, KoinComponent {
    private val javaPlugin: JavaPlugin by inject()

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val livingEntity = event.entity as? LivingEntity ?: return

        // Create an armor stand at the location where the entity was damaged
        val world = livingEntity.world
        val location = livingEntity.location.clone().add(0.0, 1.5, 0.0) // Position it slightly above the entity


        // Determine color based on damage percentage
        val remainingHealth = livingEntity.health - event.finalDamage
        val damagePercent = event.finalDamage / livingEntity.maxHealth
        val colorCode = when {
            damagePercent < 0.25 -> "§a" // Green for low damage
            damagePercent < 0.50 -> "§e" // Yellow for moderate damage
            else -> "§c" // Red for high damage
        }

        val armorStand = world.spawn(location, ArmorStand::class.java) {
            it.isVisible = false
            it.isCustomNameVisible = true
            it.isSmall = true
            it.isMarker = true
            it.customName = "${colorCode}Damage: %.2f, Health Left: %.2f".format(event.finalDamage, remainingHealth)
        }

        // Apply a small upward motion to create a "bobbing" effect
        val vector = Vector(0.0, 0.5, 0.0)
        armorStand.velocity = vector

        // Play sound effect at the location
        world.playSound(location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f)

        // Schedule the armor stand to disappear after 20 ticks (1 second)
        object : BukkitRunnable() {
            override fun run() {
                armorStand.remove()
            }
        }.runTaskLater(javaPlugin, 20L)
    }
}
