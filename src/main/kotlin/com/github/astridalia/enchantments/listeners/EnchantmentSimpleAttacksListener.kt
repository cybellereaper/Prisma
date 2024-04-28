package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.cos
import kotlin.math.sin

object EnchantmentSimpleAttacksListener : Listener, KoinComponent {
    private val javaPlugin: JavaPlugin by inject()

    @EventHandler
    fun onReaperDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val itemInMainHand = damager.inventory.itemInMainHand
        val reaperLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.REAPER)
        if (reaperLevel <= 0) return
        val entity = event.entity as? LivingEntity ?: return
        val baseChance = 0.001
        val levelEffect = 0.05 * reaperLevel
        val finalChance = baseChance + levelEffect
        if (Math.random() < finalChance) {
            spawnHelixEffect(entity)
            event.damage = entity.health + 1.0
        }
    }

    private fun spawnHelixEffect(entity: LivingEntity) {
        val location = entity.location
        val world = entity.world
        val height = 3.0
        val particlesPerLoop = 20
        object : BukkitRunnable() {
            var y = 0.0
            var t = 0.0
            override fun run() {
                if (y >= height) {
                    world.spawnParticle(Particle.SOUL, location, 100, 0.0, 0.0, 0.0, 0.1)
                    world.playSound(location, Sound.PARTICLE_SOUL_ESCAPE, 1.0f, 1.0f)
                    cancel()
                    return
                }
                for (i in 0 until particlesPerLoop) {
                    val angle = 2 * Math.PI * i / particlesPerLoop + t
                    val x = cos(angle) * 1.5
                    val z = sin(angle) * 1.5
                    world.spawnParticle(Particle.ASH, location.clone().add(x, y, z), 1)
                }
                y += 0.1
                t += Math.PI / 16
            }
        }.runTaskTimer(javaPlugin, 0L, 1L)
    }


    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand
        val lightningStrikeLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.LIGHTNING_STRIKE)
        if (lightningStrikeLevel <= 0) return
        repeat(lightningStrikeLevel) {
            event.entity.location.world?.spawn(event.entity.location, LightningStrike::class.java)
        }
    }


    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val itemOnCursor = event.player.itemOnCursor
        val cloakingLevel = itemOnCursor.getEnchantmentLevel(CustomEnchantments.CLOAKING)
        if (cloakingLevel <= 0) return
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.LEFT_CLICK_AIR) {
            val potionEffect = PotionEffect(PotionEffectType.INVISIBILITY, 255 * cloakingLevel, 1)
            event.player.addPotionEffect(potionEffect)
        }

    }
}