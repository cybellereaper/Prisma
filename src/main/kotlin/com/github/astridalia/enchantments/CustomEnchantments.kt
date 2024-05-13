package com.github.astridalia.enchantments

import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.utils.Rarity
import org.bukkit.Material
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

enum class CustomEnchantments(
    val rarity: Rarity = Rarity.COMMON,
    val displayName: String,
    val maxLevel: Int = 5,
    val enchantmentPercentage: Double = 1.0,
    val target: EnchantmentTarget = EnchantmentTarget.ALL,
) {
    FROSTBITE(
        displayName = "Frostbite",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.15,
        target = EnchantmentTarget.WEAPON
    ),
    VENOMOUS(
        displayName = "Venomous",
        rarity = Rarity.LEGENDARY,
        enchantmentPercentage = 0.35,
        target = EnchantmentTarget.WEAPON
    ),
    GATHERING(
        displayName = "Gathering",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
        target = EnchantmentTarget.TOOL
    ),
    PRECISION(
        displayName = "Precision",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
        target = EnchantmentTarget.WEAPON
    ),
    REAPER(
        displayName = "Reaper's Scythe",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.000001,
        target = EnchantmentTarget.WEAPON
    ),
    SOULBOUND(
        rarity = Rarity.RARE,
        displayName = "Soulbound",
        enchantmentPercentage = 0.35,
    ),
    LIGHTNING_STRIKE(
        rarity = Rarity.RARE,
        displayName = "ShockWave",
        enchantmentPercentage = 0.35,
    ),
    JETPACK(
        rarity = Rarity.RARE,
        displayName = "Jetpack",
        enchantmentPercentage = 0.01,
        target = EnchantmentTarget.WEARABLE
    ),
    LAVA_WALKER(
        rarity = Rarity.RARE,
        displayName = "Lava Walker",
        enchantmentPercentage = 0.50,
        target = EnchantmentTarget.ARMOR_FEET
    ),
    CLOAKING(
        rarity = Rarity.COMMON,
        displayName = "Cloaking",
        enchantmentPercentage = 0.65,
        target = EnchantmentTarget.WEAPON
    ),
    PANDORA_BOX(
        rarity = Rarity.EPIC,
        displayName = "Pandora's Box",
        enchantmentPercentage = 0.000001,
        target = EnchantmentTarget.WEAPON
    ),
    ENDER_INSTINCT_DODGE(
        rarity = Rarity.LEGENDARY,
        displayName = "Ender Instinct",
        enchantmentPercentage = 0.000001,
        target = EnchantmentTarget.ARMOR_FEET
    ),
    EXPLOSIVE_ARROW(
        displayName = "ShatterShot",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.25,
        target = EnchantmentTarget.BOW
    ),
    MAGNET(
        displayName = "Magnet",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
        target = EnchantmentTarget.TOOL
    ),
    TELEPORT(
        displayName = "Teleport",
        rarity = Rarity.RARE,
        enchantmentPercentage = 0.50,
        target = EnchantmentTarget.ALL
    ),
    TREE_CHOPPER(
        displayName = "Timber",
        rarity = Rarity.COMMON,
        enchantmentPercentage = 0.75,
        target = EnchantmentTarget.TOOL
    ),
    AUTO_SMELT(
        rarity = Rarity.EPIC,
        displayName = "Refinery",
        enchantmentPercentage = 0.35,
        target = EnchantmentTarget.TOOL
    ),
    VAMPIRE(
        displayName = "SoulLeech",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.25,
        target = EnchantmentTarget.WEAPON
    ),
    GRID_BREAKER(
        displayName = "GigaBreaker",
        rarity = Rarity.EPIC,
        enchantmentPercentage = 0.25,
        target = EnchantmentTarget.TOOL
    );

    val displayNameWithColor: String = rarity.color.toString() + displayName

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