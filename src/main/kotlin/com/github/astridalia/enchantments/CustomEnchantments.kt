package com.github.astridalia.enchantments

import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.utils.Rarity
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

enum class CustomEnchantments(
    val onlyOn: List<Material> = listOf(Material.BOOK),
    val rarity: Rarity = Rarity.COMMON,
    val displayName: String,
    val maxLevel: Int = 5,
    val enchantmentPercentage: Double = 1.0
) {
    FROSTBITE(
        displayName = "Frostbite",
        rarity = Rarity.EPIC,
        onlyOn = listOf(
            Material.IRON_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
            Material.GOLDEN_SWORD
        ),
        enchantmentPercentage = 0.15
    ),
    VENOMOUS(
        displayName = "Venomous",
        rarity = Rarity.LEGENDARY,
        enchantmentPercentage = 0.35,
        onlyOn = listOf(
            Material.IRON_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
            Material.GOLDEN_SWORD
        ),
    ),
    GATHERING(
        displayName = "Gathering",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
        onlyOn = listOf(
            Material.IRON_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE,
            Material.GOLDEN_PICKAXE
        )
    ),
    PRECISION(
        displayName = "Precision",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
        onlyOn = listOf(
            Material.IRON_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
            Material.GOLDEN_SWORD
        ),
    ),
    REAPER(
        onlyOn = listOf(
            Material.IRON_HOE,
            Material.DIAMOND_HOE,
            Material.NETHERITE_HOE,
            Material.GOLDEN_HOE
        ),
        displayName = "Reaper's Scythe",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.000001
    ),
    SOULBOUND(
        rarity = Rarity.RARE,
        displayName = "Soulbound",
        enchantmentPercentage = 0.35
    ),
    LIGHTNING_STRIKE(
        rarity = Rarity.RARE,
        displayName = "ShockWave",
        enchantmentPercentage = 0.35
    ),
    JETPACK(
        onlyOn = listOf(
            Material.ELYTRA,
            Material.CHAINMAIL_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE,
            Material.NETHERITE_CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            Material.GOLDEN_CHESTPLATE,
            Material.IRON_CHESTPLATE
        ),
        rarity = Rarity.RARE,
        displayName = "Jetpack",
        enchantmentPercentage = 0.01
    ),
    LAVA_WALKER(
        onlyOn = listOf(
            Material.IRON_BOOTS,
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.LEATHER_BOOTS,
            Material.GOLDEN_BOOTS
        ),
        rarity = Rarity.RARE,
        displayName = "Lava Walker",
        enchantmentPercentage = 0.50
    ),
    CLOAKING(
        onlyOn = listOf(
            Material.STONE_SWORD,
            Material.WOODEN_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD
        ),
        rarity = Rarity.COMMON,
        displayName = "Cloaking",
        enchantmentPercentage = 0.65
    ),
    PANDORA_BOX(
        onlyOn = listOf(
            Material.BOW,
            Material.CROSSBOW
        ),
        rarity = Rarity.EPIC,
        displayName = "Pandora's Box",
        enchantmentPercentage = 0.000001
    ),
    ENDER_INSTINCT_DODGE(
        onlyOn = listOf(
            Material.DIAMOND_BOOTS,
            Material.NETHERITE_BOOTS,
            Material.CHAINMAIL_BOOTS,
            Material.LEATHER_BOOTS,
            Material.GOLDEN_BOOTS,
            Material.IRON_BOOTS
        ),
        rarity = Rarity.LEGENDARY,
        displayName = "Ender Instinct",
        enchantmentPercentage = 0.000001
    ),
    EXPLOSIVE_ARROW(
        onlyOn = listOf(
            Material.BOW,
            Material.CROSSBOW
        ),
        displayName = "ShatterShot",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.25
    ),
    MAGNET(
        onlyOn = listOf(
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
            Material.NETHERITE_AXE
        ),
        displayName = "Magnet",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50
    ),
    TELEPORT(
        onlyOn = listOf(
            Material.IRON_HOE,
            Material.DIAMOND_HOE,
            Material.NETHERITE_HOE,
            Material.GOLDEN_HOE,
            Material.STONE_SWORD,
            Material.WOODEN_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD
        ),
        displayName = "Teleport",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50
    ),
    TREE_CHOPPER(
        onlyOn = listOf(
            Material.STONE_AXE,
            Material.WOODEN_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE,
            Material.NETHERITE_AXE
        ),
        displayName = "Timber",
        rarity = Rarity.COMMON,
        enchantmentPercentage = 0.75
    ),
    AUTO_SMELT(
        onlyOn = listOf(
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
        ),
        rarity = Rarity.EPIC,
        displayName = "Refinery",
        enchantmentPercentage = 0.35
    ),
    VAMPIRE(
        onlyOn = listOf(
            Material.STONE_SWORD,
            Material.WOODEN_SWORD,
            Material.IRON_SWORD,
            Material.GOLDEN_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD
        ),
        displayName = "SoulLeech",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.25
    ),
    GRID_BREAKER(
        onlyOn = listOf(
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
        ),
        displayName = "GigaBreaker",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.25
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
                    val level = Random.nextInt(1, enchantment.maxLevel + 1)
                    itemStack.applyEnchantment(enchantment, level)
                    break
                }
            }
        }
    }
}