package com.github.astridalia.enchantments

import com.github.astridalia.Rarity
import org.bukkit.Material

enum class CustomEnchantments(
    onlyOn: List<Material> = mutableListOf(),
    rarity: Rarity = Rarity.COMMON,
    val displayName: String,
) {
    NATURES_ALLY(displayName = "Nature's Ally", rarity = Rarity.LEGENDARY),
    REAPER(displayName = "Reaper's Scythe", rarity = Rarity.EPIC),
    HARVEST_MOON(displayName = "Harvest Moon", rarity = Rarity.EPIC),
    SOULBOUND(rarity = Rarity.EPIC, displayName = "Soulbound"),
    LIGHTNING_STRIKE(rarity = Rarity.RARE, displayName = "Lightning Strike"),
    JETPACK(
        rarity = Rarity.RARE,
        displayName = "Jetpack"
    ),
    CLOAKING(
        rarity = Rarity.COMMON,
        displayName = "Cloaking",
    ),
    EXPLOSIVE_ARROW(
        onlyOn = listOf(
            Material.BOW,
            Material.CROSSBOW
        ),
        displayName = "Explosive Arrow",
        rarity = Rarity.RARE
    ),
    MAGNET(
        displayName = "Magnet",
        rarity = Rarity.RARE
    ),
    TELEPORT(
        displayName = "Teleport",
        rarity = Rarity.RARE
    ),
    AUTO_SMELT(
        rarity = Rarity.EPIC,
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
        rarity = Rarity.EPIC
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
        rarity = Rarity.EPIC
    );

    val displayNameWithColor: String = rarity.color.toString() + displayName

    val applicableMaterials: List<Material> = onlyOn
}