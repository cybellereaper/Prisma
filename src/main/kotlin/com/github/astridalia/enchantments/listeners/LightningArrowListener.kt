package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.entity.Arrow
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.koin.core.component.KoinComponent

object LightningArrowListener : Listener, KoinComponent {
    private val arrowShooters: MutableMap<Arrow, Player> = mutableMapOf()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onProjectileHit(event: ProjectileHitEvent) {
        (event.entity as? Arrow)?.let { arrow ->
            arrowShooters[arrow]?.let { shooter ->
                shooter.inventory.itemInMainHand.getEnchantOf(CustomEnchantments.LIGHTNING_STRIKE).takeIf { it > 0 }
                    ?.also {
                        explodeArrow(arrow, it)
                        arrowShooters.remove(arrow)
                    }
            }
        }
    }

    private fun explodeArrow(arrow: Arrow, explosionPower: Int) {
        arrow.location.world?.let { world ->
            repeat(explosionPower) {
                world.spawn(arrow.location, LightningStrike::class.java)
            }
            arrow.remove()
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityShootBow(event: EntityShootBowEvent) {
        (event.entity as? Player)?.let { shooter ->
            event.bow?.getEnchantOf(CustomEnchantments.LIGHTNING_STRIKE)?.takeIf { it > 0 }?.also {
                (event.projectile as? Arrow)?.let { arrow -> arrowShooters[arrow] = shooter }
            }
        }
    }
}