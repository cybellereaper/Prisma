package com.github.astridalia.enchantments

import com.github.astridalia.Rarity
import org.bukkit.Material

enum class CustomEnchantments(
    onlyOn: List<Material> = mutableListOf(),
    rarity: Rarity = Rarity.COMMON,
    val displayName: String,
) {
//    NATURES_ALLY(displayName = "Nature's Ally", rarity = Rarity.LEGENDARY),
//    REAPER(displayName = "Reaper's Scythe", rarity = Rarity.EPIC),
//    HARVEST_MOON(displayName = "Harvest Moon", rarity = Rarity.EPIC),
//    SOULBOUND(rarity = Rarity.RARE, displayName = "Soulbound"),
    LIGHTNING_STRIKE(rarity = Rarity.RARE, displayName = "ShockWave"),
    JETPACK(
        rarity = Rarity.RARE,
        displayName = "Jetpack"
    ),
    LAVA_WALKER(rarity = Rarity.RARE, displayName = "Lava Walker"),
    CLOAKING(
        rarity = Rarity.COMMON,
        displayName = "Cloaking",
    ),
    EXPLOSIVE_ARROW(
        onlyOn = listOf(
            Material.BOW,
            Material.CROSSBOW
        ),
        displayName = "ShatterShot",
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
        displayName = "Refinery"
    ),
    VAMPIRE(
        displayName = "SoulLeech",
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
        displayName = "GigaBreaker",
        rarity = Rarity.EPIC
    );

    val displayNameWithColor: String = rarity.color.toString() + displayName

    val applicableMaterials: List<Material> = onlyOn
}