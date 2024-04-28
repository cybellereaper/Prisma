package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import com.github.astridalia.utils.CooldownManager
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


object EnchantmentSimpleAttacksListener : Listener, KoinComponent {
    private val javaPlugin: JavaPlugin by inject()

    @EventHandler
    fun onReaperDamage(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand
        val reaperLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.REAPER)
        if (reaperLevel <= 0) return
        val entity = event.entity as? LivingEntity ?: return
        val baseChance = 0.001
        val levelEffect = 0.05 * reaperLevel
        val finalChance = baseChance + levelEffect
        if (Math.random() < finalChance) {
            reaperEffect(entity)
            event.damage = entity.health + 1.0
        }
    }

    private fun reaperEffect(entity: LivingEntity) {
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

    private val keepItems = mutableMapOf<Player, List<ItemStack>>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity as? Player ?: return
        val soulboundItems = player.inventory.contents.filterNotNull().filter {
            it.getEnchantmentLevel(CustomEnchantments.SOULBOUND) > 0
        }
        event.drops.removeIf { drop -> soulboundItems.any { it.isSimilar(drop) } }
        keepItems[player] = soulboundItems
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        keepItems[player]?.forEach { player.inventory.addItem(it) }
        keepItems.remove(player)
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
    fun onTreeChopper(event: BlockBreakEvent) {
        val player = event.player
        val itemInMainHand = player.inventory.itemInMainHand
        val treeChopperLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.TREE_CHOPPER)
        if (treeChopperLevel <= 0) return
        val block = event.block
        if (!block.type.name.contains("_LOG")) return

        // Prevent default block break to handle it manually
        event.isCancelled = true
        chopTree(block, player, itemInMainHand)
    }

    private fun chopTree(startBlock: Block, player: Player, itemStack: ItemStack) {
        val blocksToCheck: MutableSet<Block> = mutableSetOf(startBlock)
        val checkedBlocks: MutableSet<Block> = mutableSetOf()

        while (blocksToCheck.isNotEmpty()) {
            val block = blocksToCheck.first()
            blocksToCheck.remove(block)
            checkedBlocks.add(block)

            // Break the block and add a particle effect
            block.breakNaturally(itemStack)
            player.world.spawnParticle(Particle.BLOCK_CRACK, block.location, 20, 0.3, 0.3, 0.3, block.blockData)

            // Check all adjacent blocks (6 directions)
            BlockFace.entries.forEach { face ->
                val rel = block.getRelative(face)
                if (rel.type.name.contains("_LOG") && !checkedBlocks.contains(rel)) {
                    blocksToCheck.add(rel)
                }
            }
        }
    }


    @EventHandler
    fun onPlayerTeleport(event: PlayerInteractEvent) {
        val player = event.player
        val itemInHand = player.inventory.itemInMainHand
        val teleportLevel = itemInHand.getEnchantmentLevel(CustomEnchantments.TELEPORT)

        if (teleportLevel <= 0) return

        if (event.action.name.contains("RIGHT_CLICK")) {
            // Check for cooldown using CooldownManager
            if (CooldownManager.isCooldownActive(player.uniqueId, CooldownManager.CooldownCause.ENCHANTMENT)) {
                val remainingTime = CooldownManager.getRemainingCooldown(player.uniqueId, CooldownManager.CooldownCause.ENCHANTMENT)
                val remainingSeconds = remainingTime / 1000
                player.sendMessage("§cTeleport is on cooldown. Please wait $remainingSeconds seconds.")
                return
            }

            val distance = 2.0 * teleportLevel
            val direction = player.location.direction.normalize()
            val teleportVector = direction.multiply(distance)
            val newLocation = player.location.add(teleportVector)

            // Perform the teleport
            player.teleport(newLocation)
            player.world.spawnParticle(Particle.END_ROD, newLocation, 30, 0.5, 0.5, 0.5, 0.1)
            player.world.playSound(newLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f)

            // Set cooldown using CooldownManager
            CooldownManager.setCooldown(player.uniqueId, CooldownManager.CooldownCause.ENCHANTMENT, 10000) // 10 seconds cooldown
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val itemOnCursor = event.player.itemInHand
        val cloakingLevel = itemOnCursor.getEnchantmentLevel(CustomEnchantments.CLOAKING)
        if (cloakingLevel <= 0) return

        if (event.action.name.contains("LEFT_CLICK") || event.action.name.contains("RIGHT_CLICK")) {
            val potionEffect = PotionEffect(PotionEffectType.INVISIBILITY, 255 * cloakingLevel, 1)
            event.player.addPotionEffect(potionEffect)
        }

    }
}