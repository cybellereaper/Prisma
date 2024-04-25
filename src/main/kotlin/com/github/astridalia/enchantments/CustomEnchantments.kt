package com.github.astridalia.enchantments

import com.github.astridalia.Rarity
import org.bukkit.Material

enum class CustomEnchantments(
    onlyOn: List<Material> = mutableListOf(),
    rarity: Rarity = Rarity.COMMON,
    val displayName: String,
) {
    LIGHTNING_STRIKE(rarity = Rarity.LEGENDARY, displayName = "Lightning Strike"),

    CLOAKING(
        displayName = "Cloaking",
        rarity = Rarity.LEGENDARY
    ),

    EXPLOSIVE_ARROW(
        onlyOn = listOf(
            Material.BOW,
            Material.CROSSBOW
        ),
        displayName = "Explosive Arrow"
    ),

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


    VAMPIRE(
        displayName = "Life-steal",
        rarity = Rarity.LEGENDARY
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