package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

object VampireHitListener : Listener {
    private val processingVampireHits = HashSet<UUID>()
    private val vampireCooldowns = mutableMapOf<UUID, Long>()

    @EventHandler
    fun onVampireHit(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        if (damager.uniqueId in processingVampireHits) return // Prevent recursion

        // Cooldown check
        val now = System.currentTimeMillis()
        val cooldownEnd = vampireCooldowns.getOrDefault(damager.uniqueId, 0L)
        if (now < cooldownEnd) {
            return // Still on cooldown, so don't proceed
        }

        val itemInMainHand = damager.inventory.itemInMainHand
        val vampireLevel = itemInMainHand.getEnchantOf(CustomEnchantments.VAMPIRE)
        if (vampireLevel <= 0) return

        val entity = event.entity as? LivingEntity ?: return
        val baseHealthSteal = 2.0
        val healthSteal = baseHealthSteal * vampireLevel
        val damagerNewHealth = (damager.health + healthSteal).coerceAtMost(
            damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        )

        try {
            processingVampireHits.add(damager.uniqueId) // Mark as processing
            damager.health = damagerNewHealth
            val additionalDamage = 0.5 * vampireLevel
            entity.damage(additionalDamage, damager)
        } finally {
            processingVampireHits.remove(damager.uniqueId) // Always remove after processing
        }

        // Update the cooldown (2.5 seconds)
        vampireCooldowns[damager.uniqueId] = now + 2500 // 2.5 seconds in milliseconds

        // Play a particle effect at the entity's location
        entity.world.spawnParticle(Particle.HEART, entity.location.add(0.0, 1.0, 0.0), 10)

    }
}