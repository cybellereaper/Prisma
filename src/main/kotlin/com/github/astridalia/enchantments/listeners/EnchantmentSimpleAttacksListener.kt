package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantment.removeEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.setEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import com.github.astridalia.utils.CooldownManager
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
import org.bukkit.event.enchantment.EnchantItemEvent
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
import kotlin.math.max
import kotlin.math.sin
import kotlin.random.Random


object EnchantmentSimpleAttacksListener : Listener, KoinComponent {
    private val javaPlugin: JavaPlugin by inject()

    @EventHandler
    fun onReaperDamage(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand

        if (event.entity is Player) {
            val itemStack = (event.entity as Player).inventory.chestplate
            val nullLevel = itemStack?.getEnchantmentLevel(CustomEnchantments.NULLIFY) ?: 0
            if (nullLevel > 0 && chanceGenerator(nullLevel)) {
                return
            }
        }

        val reaperLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.REAPER)
        if (reaperLevel <= 0) return
        val entity = event.entity as? LivingEntity ?: return
        if (!chanceGenerator(reaperLevel)) return
        reaperEffect(entity)
        event.damage = entity.health + 1.0
        applyDurability(itemInMainHand, reaperLevel, damage)
        damage.damage(12.5)
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
            it.getEnchantmentLevel(CustomEnchantments.SOUL_BOUND) > 0
        }
        event.drops.removeIf { drop -> soulboundItems.any { it.isSimilar(drop) } }
        soulboundItems.forEach { item ->
            val currentLevel = item.getEnchantmentLevel(CustomEnchantments.SOUL_BOUND)
            if (currentLevel > 1)
                item.setEnchantmentLevel(CustomEnchantments.SOUL_BOUND, currentLevel - 1) else item.removeEnchantment(
                CustomEnchantments.SOUL_BOUND
            )
        }
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
    fun onBlockBreakWithGathering(event: BlockBreakEvent) {
        val player = event.player
        val itemInMainHand = player.inventory.itemInMainHand
        val gatheringLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.GATHERING)
        if (gatheringLevel <= 0) return
        val block = event.block
        if (!block.isPreferredTool(itemInMainHand)) return

        // Prevent the block from dropping items normally
        event.isDropItems = false

        // Calculate drops based on the enchantment level
        val drops = block.getDrops(itemInMainHand, player)
        val multipliedDrops = ArrayList<ItemStack>()

        // Multiply each drop by the enchantment level
        for (item in drops) {
            val multipliedItem = item.clone()
            multipliedItem.amount *= gatheringLevel
            multipliedDrops.add(multipliedItem)
        }

        // Drop the items in the world
        for (item in multipliedDrops) {
            block.world.dropItemNaturally(block.location, item)
        }
    }

    private val lastSneakTimes: MutableMap<UUID, Long> = HashMap()

    private fun isDoubleSneak(player: Player): Boolean {
        val now = System.currentTimeMillis()
        val lastSneakTime = lastSneakTimes.getOrDefault(player.uniqueId, 0L)
        lastSneakTimes[player.uniqueId] = now

        // Check if the current sneak is within 500 milliseconds of the last sneak
        return now - lastSneakTime < 500
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
        applyDurability(itemInMainHand, treeChopperLevel, player)
    }

    private fun applyDurability(item: ItemStack, blocksBroken: Int, player: Player) {
        val damage = blocksBroken * 4 // You can adjust this if you want different durability loss rates
        item.damage(damage, player)
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
    fun onPlayerDamageWithPrecision(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand
        val precisionLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.PRECISION)
        if (precisionLevel <= 0) return
        if (!chanceGenerator(precisionLevel)) return
        event.damage *= 2
        damage.world.playSound(damage.location, Sound.ENTITY_GENERIC_HURT, 1.0f, 1.0f)
    }

    @EventHandler
    fun onPlayerDamageWithVenom(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand
        val venomLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.VENOMOUS)
        if (venomLevel <= 0) return

        event.damage *= 2
        val target = event.entity as? LivingEntity ?: return
        if (!chanceGenerator(venomLevel)) return
        target.world.playSound(target.location, Sound.ENTITY_GENERIC_HURT, 1.0f, 1.0f)
        target.addPotionEffect(PotionEffect(PotionEffectType.POISON, 20 * 3, venomLevel - 1))
    }

    @EventHandler
    fun onPlayerDamageWithFrostbite(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand
        val frostbiteLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.FROSTBITE)
        if (frostbiteLevel <= 0) return

        if (chanceGenerator(frostbiteLevel)) {
            event.damage *= 2
            val target = event.entity as? LivingEntity ?: return
            target.freezeTicks = 20 * 5
            target.world.playSound(target.location, Sound.BLOCK_SNOW_HIT, 1.0f, 1.0f)
            target.world.spawnParticle(Particle.SNOW_SHOVEL, target.location, 10, 0.5, 0.5, 0.5, 0.1)
            target.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20 * 5, frostbiteLevel - 1))
            target.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, frostbiteLevel - 1))
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
                val remainingTime =
                    CooldownManager.getRemainingCooldown(player.uniqueId, CooldownManager.CooldownCause.ENCHANTMENT)
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
            CooldownManager.setCooldown(
                player.uniqueId,
                CooldownManager.CooldownCause.ENCHANTMENT,
                10000
            ) // 10 seconds cooldown
        }
    }

    @EventHandler
    fun onPlayerEnchant(event: EnchantItemEvent) {
        val itemInHand = event.item
        CustomEnchantments.getRandomEnchantment(itemInHand)
    }

    @EventHandler
    fun onPlayerNullify(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val itemInMainHand = damager.inventory.chestplate ?: return
        val nullifyLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.NULLIFY)
        if (nullifyLevel <= 0) return
        if (!chanceGenerator(nullifyLevel)) return

        val negativeEffects = setOf(
            PotionEffectType.POISON,
            PotionEffectType.SLOW,
            PotionEffectType.WEAKNESS,
            PotionEffectType.BLINDNESS,
            PotionEffectType.HUNGER,
            PotionEffectType.WITHER
        )

        var effectsRemoved = false
        for (effect in negativeEffects) {
            if (damager.hasPotionEffect(effect)) {
                damager.removePotionEffect(effect)
                effectsRemoved = true
            }
        }

        // Play sound and particle effects if any negative effects were removed
        if (effectsRemoved) {
            damager.location.world?.spawnParticle(Particle.SPELL_MOB, damager.location, 30, 1.0, 1.0, 1.0, 0.5)
            damager.location.world?.playSound(damager.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f)
        }
    }

    private fun chanceGenerator(level: Int): Boolean = Random.nextDouble() < 0.2 + 0.05 * level

    @EventHandler
    fun onPlayerBackdraft(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val itemInMainHand = damager.inventory.itemInMainHand
        val backdraftLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.BACKDRAFT)
        if (backdraftLevel <= 0) return
        val target = event.entity as? LivingEntity ?: return
        val negativeEffects = setOf(
            PotionEffectType.POISON,
            PotionEffectType.SLOW,
            PotionEffectType.WEAKNESS,
            PotionEffectType.BLINDNESS,
            PotionEffectType.HUNGER,
            PotionEffectType.WITHER
        )
        var effectsTransferred = false
        damager.activePotionEffects.forEach { effect ->
            if (effect.type in negativeEffects && chanceGenerator(backdraftLevel)) {
                target.addPotionEffect(
                    PotionEffect(
                        effect.type,
                        max(effect.duration * backdraftLevel, 128),
                        effect.amplifier * backdraftLevel
                    ),
                    true
                )

                damager.removePotionEffect(effect.type)
                effectsTransferred = true
            }
        }
        if (effectsTransferred) {
            target.location.world?.spawnParticle(Particle.SPELL_WITCH, target.location, 30, 0.5, 0.5, 0.5, 0.5)
            target.location.world?.playSound(target.location, Sound.ENTITY_WITCH_THROW, 1.0f, 1.0f)
            damager.location.world?.playSound(damager.location, Sound.ENTITY_WITCH_DRINK, 1.0f, 1.0f)
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