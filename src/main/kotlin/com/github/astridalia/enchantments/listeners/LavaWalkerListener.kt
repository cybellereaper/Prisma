package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.Levelled
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

object LavaWalkerListener : Listener, KoinComponent {
    private val javaPlugin: JavaPlugin by inject()
    private val playerTasks: MutableMap<UUID, BukkitTask> = mutableMapOf()

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val world = player.world

        val boots = player.inventory.boots ?: return

        val enchantOf = boots.getEnchantmentLevel(CustomEnchantments.LAVA_WALKER)
        if (enchantOf <= 0) return

        playerTasks.computeIfAbsent(player.uniqueId) { startPlatformTask(player) }
    }

    private fun startPlatformTask(player: Player): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(javaPlugin, Runnable {
            maintainPlatform(player)
            player.fireTicks = 0
        }, 0L, 1L) // Run task every tick
    }

    private fun maintainPlatform(player: Player) {
        val world = player.world
        val boots = player.inventory.boots ?: return

        val lavaWalker = boots.getEnchantmentLevel(CustomEnchantments.LAVA_WALKER)
        if (lavaWalker <= 0) {
            playerTasks.remove(player.uniqueId)?.cancel()
            return
        }

        val radius = 1  // This creates a 3x3 platform (player in the center)
        for (dx in -radius..radius) {
            for (dz in -radius..radius) {
                val block = world.getBlockAt(player.location.add(dx.toDouble(), -1.0, dz.toDouble()))
                if (block.type == Material.LAVA && block.blockData is Levelled) {
                    val levelled = block.blockData as Levelled
                    if (levelled.level == 0) {  // Check if it's a full lava block (source block)
                        block.type = Material.MAGMA_BLOCK
                        world.spawnParticle(Particle.LAVA, block.location.add(0.5, 0.5, 0.5), 30, 0.5, 0.5, 0.5, 0.05)
                        world.playSound(block.location, Sound.BLOCK_LAVA_POP, 1f, 1f)
                        Material.LAVA.scheduleBlockReset(block)
                    }
                }
            }
        }
    }

    private fun Material.scheduleBlockReset(block: Block) {
        object : BukkitRunnable() {
            override fun run() {
                if (block.type == Material.MAGMA_BLOCK) {
                    block.type = this@scheduleBlockReset
                    block.world.spawnParticle(
                        Particle.SMOKE_LARGE,
                        block.location.add(0.5, 0.5, 0.5),
                        20,
                        0.5,
                        0.5,
                        0.5,
                        0.1
                    )
                    block.world.playSound(block.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f)
                }
            }
        }.runTaskLater(javaPlugin, 100L)  // Reset after 5 seconds
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        val boots = player.inventory.boots ?: return
        val enchantOf = boots.getEnchantmentLevel(CustomEnchantments.LAVA_WALKER)
        if (enchantOf <= 0) return
        when (event.cause) {
            EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.HOT_FLOOR, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.FIRE -> event.isCancelled =
                true
            else -> return
        }
    }
}
