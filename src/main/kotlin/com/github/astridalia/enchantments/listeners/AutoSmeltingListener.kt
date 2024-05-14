package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantmentLevel
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.koin.java.KoinJavaComponent.inject

object AutoSmeltingListener : Listener {

    private val javaPlugin: JavaPlugin by inject(JavaPlugin::class.java)

    private val smeltableMaterials = mapOf(
        Material.IRON_ORE to Material.IRON_INGOT,
        Material.GOLD_ORE to Material.GOLD_INGOT,
        Material.COPPER_ORE to Material.COPPER_INGOT,
        Material.DEEPSLATE_COPPER_ORE to Material.COPPER_INGOT,
        Material.ANCIENT_DEBRIS to Material.NETHERITE_SCRAP,
        // Add more ores here as needed
        Material.DEEPSLATE_IRON_ORE to Material.IRON_INGOT,
        Material.DEEPSLATE_GOLD_ORE to Material.GOLD_INGOT,
        Material.NETHER_GOLD_ORE to Material.GOLD_INGOT,
        Material.NETHER_QUARTZ_ORE to Material.QUARTZ
    )

    private fun Material.toItemStack(amount: Int = 1) = ItemStack(this, amount)

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val itemInMainHand = player.inventory.itemInMainHand
        val autoSmeltLevel = itemInMainHand.getEnchantmentLevel(CustomEnchantments.REFINERY)
        if (autoSmeltLevel <= 0) return


        smeltableMaterials[event.block.type]?.let { smeltedMaterial ->
            if (!event.block.isPreferredTool(itemInMainHand)) return
            event.isCancelled = true
            event.block.type = Material.AIR


            val droppedItem =
                event.block.world.dropItemNaturally(event.block.location.add(0.5, 0.5, 0.5), ItemStack(smeltedMaterial))

            // Use an array to hold the task ID, so it can be accessed and modified within the lambda
            val taskId = IntArray(1)

            // Schedule a repeating task to create a lingering effect
            taskId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(javaPlugin, {
                if (!droppedItem.isValid || droppedItem.isDead) {
                    Bukkit.getScheduler().cancelTask(taskId[0]) // Use the task ID from the array to cancel the task
                } else {
                    // Play particle effect around the dropped item
                    droppedItem.world.spawnParticle(
                        Particle.FLAME,
                        droppedItem.location,
                        10, // Number of particles
                        0.1, 0.1, 0.1, // Spread of particles
                        0.02 // Speed of particles
                    )

                    // Play sound effect near the dropped item
                    droppedItem.world.playSound(
                        droppedItem.location,
                        Sound.BLOCK_FURNACE_FIRE_CRACKLE,
                        0.5f, // Volume
                        1f  // Pitch
                    )
                }
            }, 0L, 20L) // 0L for no delay in starting, 20L for a 1-second interval between runs
        }
    }
}