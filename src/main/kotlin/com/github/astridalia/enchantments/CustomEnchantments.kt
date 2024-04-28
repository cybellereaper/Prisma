package com.github.astridalia.enchantments

import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.utils.Rarity
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

enum class CustomEnchantments(
    onlyOn: List<Material> = mutableListOf(Material.BOOK),
    rarity: Rarity = Rarity.COMMON,
    val displayName: String,
    val maxLevel: Int = 5,
    val enchantmentPercentage: Double = 1.0,
) {
//    NATURES_ALLY(displayName = "Nature's Ally", rarity = Rarity.LEGENDARY),
    REAPER(
    onlyOn = listOf(
        Material.BOOK,
        Material.IRON_HOE,
        Material.DIAMOND_HOE,
        Material.NETHERITE_HOE,
        Material.GOLDEN_HOE,
        Material.IRON_HOE,
    ),
    displayName = "Reaper's Scythe",
    rarity = Rarity.EPIC,
    enchantmentPercentage = 0.000_001),
//    HARVEST_MOON(displayName = "Harvest Moon", rarity = Rarity.EPIC),
    SOULBOUND(rarity = Rarity.RARE, displayName = "Soulbound", enchantmentPercentage = 0.35),
    LIGHTNING_STRIKE(rarity = Rarity.RARE, displayName = "ShockWave", enchantmentPercentage = 0.35),
    JETPACK(
        listOf(
            Material.BOOK,
            Material.ELYTRA,
            Material.CHAINMAIL_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE,
            Material.NETHERITE_CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE,
            Material.IRON_CHESTPLATE,
        ),
        rarity = Rarity.RARE,
        displayName = "Jetpack",
        enchantmentPercentage = 0.01,
    ),

    LAVA_WALKER(
        listOf(
            Material.BOOK,
            Material.IRON_BOOTS,
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.LEATHER_BOOTS,
            Material.GOLDEN_BOOTS,
            Material.IRON_BOOTS,
        ),
        rarity = Rarity.RARE,
        displayName = "Lava Walker",
        enchantmentPercentage = 0.50,
        ),
    CLOAKING(
        listOf(
            Material.BOOK,
            Material.STONE_SWORD,
            Material.WOODEN_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
        ),
        rarity = Rarity.COMMON,
        displayName = "Cloaking",
        enchantmentPercentage = 0.65,
    ),
    PANDORA_BOX(
        onlyOn = listOf(
            Material.BOOK,
            Material.BOW,
            Material.CROSSBOW
        ),
        rarity = Rarity.EPIC,
        displayName = "Pandora's Box",
        enchantmentPercentage = 0.000_001),
    ENDER_INSTINCT_DODGE(
        rarity = Rarity.LEGENDARY, displayName = "Ender Instinct",
        onlyOn = listOf(
            Material.BOOK,
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.LEATHER_BOOTS,
            Material.GOLDEN_BOOTS,
            Material.IRON_BOOTS,
        ),
        enchantmentPercentage = 0.000_001,
    ),
    EXPLOSIVE_ARROW(
        onlyOn = listOf(
            Material.BOW,
            Material.CROSSBOW
        ),
        displayName = "ShatterShot",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.25,
    ),
    MAGNET(
        listOf(
            Material.BOOK,
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE,
            Material.STONE_AXE,
            Material.WOODEN_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE,
        ),
        displayName = "Magnet",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
    ),
    TELEPORT(
        listOf(
            Material.BOOK,
            Material.IRON_HOE,
            Material.DIAMOND_HOE,
            Material.NETHERITE_HOE,
            Material.GOLDEN_HOE,
            Material.IRON_HOE,
            Material.STONE_SWORD,
            Material.WOODEN_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
        ),
        displayName = "Teleport",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
    ),
    TREE_CHOPPER(
        onlyOn = listOf(
            Material.BOOK,
            Material.STONE_AXE,
            Material.WOODEN_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
        ),
        displayName = "Timber",
        rarity = Rarity.COMMON,
        enchantmentPercentage = 0.75,
        ),
    AUTO_SMELT(
        listOf(
            Material.BOOK,
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
        ),
        rarity = Rarity.EPIC,
        displayName = "Refinery",
        enchantmentPercentage = 0.35,
    ),
    VAMPIRE(
        listOf(
            Material.BOOK,
            Material.STONE_SWORD,
            Material.WOODEN_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
        ),
        displayName = "SoulLeech",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.25,
    ),
    GRID_BREAKER(
        onlyOn = listOf(
            Material.BOOK,
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
        ),
        displayName = "GigaBreaker",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.25,
    );

    val displayNameWithColor: String = rarity.color.toString() + displayName

    val applicableMaterials: List<Material> = onlyOn

    companion object {
        fun getRandomEnchantment(itemStack: ItemStack) {
            val totalWeight = entries.toTypedArray().sumOf { it.enchantmentPercentage }
            var random = Math.random() * totalWeight
            for (enchantment in entries) {
                random -= enchantment.enchantmentPercentage
                if (random <= 0) {
                    val toInt = Random.nextInt(1, enchantment.maxLevel + 1)
                    itemStack.applyEnchantment(enchantment, toInt)
                    break
                }
            }
        }
    }
}