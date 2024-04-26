package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

// Super work in progress
object PandoraListener : Listener {

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val item = damager.inventory.itemInMainHand
        val enchantOf = item.getEnchantOf(CustomEnchantments.PANDORA_BOX)
        if(enchantOf <= 0) return
        activatePortalEffect(damager, enchantOf)
    }

    private fun displayEffectLaunchParticles(location: Location, effectType: Int) {
        val world = location.world
        when (effectType) {
            0 -> world?.spawnParticle(Particle.VILLAGER_ANGRY, location, 20, 0.5, 0.5, 0.5, 0.1)
            1 -> world?.spawnParticle(Particle.FLAME, location, 50, 0.5, 0.5, 0.5, 0.05)
            2 -> world?.spawnParticle(Particle.CRIT, location, 100, 0.5, 0.5, 0.5, 0.1)
        }
    }

    private fun activatePortalEffect(player: Player, level: Int) {
        val targetLocation = player.getTargetBlock(null, 50).location.add(0.0, 3.0, 0.0)
        displayPortalEffects(targetLocation)
        playPortalSound(targetLocation)
        val world = player.world

        when (Random.nextInt(3)) {
            0 -> {
                repeat(level) { world.spawnEntity(targetLocation, EntityType.ZOMBIE) }
                playEffectSound(targetLocation, 0)
            }
            1 -> {
                repeat(level) { world.spawnEntity(targetLocation, EntityType.FIREBALL) }
                playEffectSound(targetLocation, 1)
            }
            2 -> {
                repeat(level * 5) { world.spawnArrow(targetLocation, player.location.direction, 2.0f, 12.0f) }
                playEffectSound(targetLocation, 2)
            }
        }
        displayEffectLaunchParticles(targetLocation, Random.nextInt(3))
    }

    private fun displayPortalEffects(location: Location) {
        val world = location.world
        world?.spawnParticle(Particle.PORTAL, location, 300, 1.0, 1.0, 1.0, 1.0)
        world?.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 100, 1.0, 1.0, 1.0, 1.0)
        world?.spawnParticle(Particle.DRAGON_BREATH, location.add(0.0, 1.0, 0.0), 50, 0.5, 0.5, 0.5, 0.1)
    }

    private fun playEffectSound(location: Location, effectType: Int) {
        val sound = when(effectType) {
            0 -> Sound.ENTITY_ZOMBIE_AMBIENT
            1 -> Sound.ENTITY_BLAZE_SHOOT
            2 -> Sound.ENTITY_ARROW_SHOOT
            else -> Sound.BLOCK_NOTE_BLOCK_BASS
        }
        location.world?.playSound(location,sound,1.0f,1.0f)
    }

    private fun playPortalSound(location: Location) {
        location.world?.playSound(location, Sound.BLOCK_PORTAL_TRIGGER, 1.0f, 1.0f)
    }
}