package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.koin.core.component.KoinComponent

object ExplosiveArrowListener : Listener, KoinComponent {
    private const val EXPLOSION_POWER = 2.5f
    private val arrowShooters: MutableMap<Arrow, Player> = mutableMapOf()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onProjectileHit(event: ProjectileHitEvent) {
        (event.entity as? Arrow)?.let { arrow ->
            arrowShooters[arrow]?.let { shooter ->
                shooter.inventory.itemInMainHand.getEnchantOf(CustomEnchantments.EXPLOSIVE_ARROW).takeIf { it > 0 }
                    ?.also {
                        explodeArrow(arrow, EXPLOSION_POWER * it)
                        arrowShooters.remove(arrow)
                    }
            }
        }
    }

    private fun explodeArrow(arrow: Arrow, explosionPower: Float) {
        arrow.location.world?.let { world ->
            world.createExplosion(arrow.location, explosionPower)
            generateParticleTrail(arrow, world)
            arrow.remove()
        }
    }

    private fun generateParticleTrail(arrow: Arrow, world: World) {
        val particleCount = 100
        val normalizedVelocity = arrow.velocity.normalize().multiply(0.1)
        repeat(particleCount) {
            val spawnLocation = arrow.location.clone().add(normalizedVelocity.multiply(it.toDouble()))
            world.spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 1)
            world.playSound(spawnLocation, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityShootBow(event: EntityShootBowEvent) {
        (event.entity as? Player)?.let { shooter ->
            event.bow?.getEnchantOf(CustomEnchantments.EXPLOSIVE_ARROW)?.takeIf { it > 0 }?.also {
                (event.projectile as? Arrow)?.let { arrow -> arrowShooters[arrow] = shooter }
            }
        }
    }
}