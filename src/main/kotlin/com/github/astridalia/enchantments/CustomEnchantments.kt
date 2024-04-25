package com.github.astridalia.enchantments

import com.github.astridalia.Rarity
import org.bukkit.Material

enum class CustomEnchantments(
    onlyOn: List<Material> = mutableListOf(),
    rarity: Rarity = Rarity.COMMON,
    val displayName: String,
    ) {

    MAGNET(displayName = "Magnet"),

    AUTO_SMELT(
        onlyOn = listOf(
            Material.STONE_PICKAXE,
            Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
        ),
        displayName = "Auto Smelter"
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
        displayName = "Grid Breaker",
        rarity = Rarity.LEGENDARY
    );

    val displayNameWithColor: String = rarity.color.toString() + displayName

    val applicableMaterials: List<Material> = onlyOn
}