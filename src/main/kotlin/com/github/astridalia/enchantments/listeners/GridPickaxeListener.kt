package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent.inject

object GridPickaxeListener : KoinComponent, Listener {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val javaPlugin: JavaPlugin by inject(JavaPlugin::class.java)

    private val unbreakableMaterials = setOf(
        Material.BEDROCK, Material.BARRIER, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK,
        Material.REPEATING_COMMAND_BLOCK, Material.STRUCTURE_BLOCK, Material.STRUCTURE_VOID
    )

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block

        // Check if the player has the Cubic Mining enchantment
        val itemInMainHand = player.inventory.itemInMainHand
        val cubicMiningLevel = itemInMainHand.getEnchantOf(CustomEnchantments.GRID_BREAKER)
        if (cubicMiningLevel <= 0) return

        // Check if the player has the Auto Smelting enchantment
        val autoSmeltLevel = itemInMainHand.getEnchantOf(CustomEnchantments.AUTO_SMELT)
        if (autoSmeltLevel > 0) AutoSmeltingListener.onBlockBreak(event)

        val cubeBlocks = getCubicBlocks(block, cubicMiningLevel)

        // Delay the execution of breakCubicBlocks
        scope.launch {
            Bukkit.getScheduler().runTask(javaPlugin, Runnable {
                breakCubicBlocks(player, cubeBlocks)
            })
        }
    }

    private fun breakCubicBlocks(player: Player, cubeBlocks: List<Block>) {
        cubeBlocks.forEach { cubeBlock ->
            // Enhanced particle effect for breaking blocks
            player.world.spawnParticle(
                Particle.BLOCK_CRACK,
                cubeBlock.location.add(0.5, 0.5, 0.5), // Center the particles in the middle of the block
                30, // Reduced number for performance, adjust as needed
                0.25, 0.25, 0.25, // Slightly increase the spread to make the effect more visible
                cubeBlock.blockData
            )

            // Optional: Add a sound effect to accompany the block breaking
            player.world.playSound(
                cubeBlock.location,
                Sound.BLOCK_STONE_BREAK, // Choose an appropriate sound based on the block type or preference
                1f, // Volume
                1f  // Pitch
            )

            // Create a new BlockBreakEvent for the cubeBlock
            val event = BlockBreakEvent(cubeBlock, player)
            val itemInMainHand = player.inventory.itemInMainHand

            // Apply AutoSmelting if the player has the enchantment
            val autoSmeltLevel = itemInMainHand.getEnchantOf(CustomEnchantments.AUTO_SMELT)
            if (autoSmeltLevel > 0) AutoSmeltingListener.onBlockBreak(event)

            // If the event was not cancelled by AutoSmelting, break the block naturally
            if (!event.isCancelled) cubeBlock.breakNaturally()
        }
    }

    private fun getCubicBlocks(centerBlock: Block, enchantmentLevel: Int): List<Block> {
        val radius = enchantmentLevel // Adjust radius based on enchantment level
        val cubeBlocks = mutableListOf<Block>()
        for (dx in -radius..radius) for (dy in -radius..radius) for (dz in -radius..radius) {
            if (dx == 0 && dy == 0 && dz == 0) continue // Skip the center block
            val block = centerBlock.world.getBlockAt(centerBlock.x + dx, centerBlock.y + dy, centerBlock.z + dz)
            if (!block.type.isAir && !isUnbreakable(block.type)) cubeBlocks.add(block)
        }
        return cubeBlocks
    }

    private fun isUnbreakable(material: Material): Boolean {
        return material in unbreakableMaterials
    }

}