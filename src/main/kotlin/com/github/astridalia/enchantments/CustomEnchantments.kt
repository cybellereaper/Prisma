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
    val target: EnchantmentTarget = EnchantmentTarget.ALL
) {
    FROSTBITE(
        displayName = "Frostbite",
        rarity = Rarity.EPIC,
        target = EnchantmentTarget.WEAPON
    ),
    VENOMOUS(
        displayName = "Venomous",
        rarity = Rarity.LEGENDARY,
        target = EnchantmentTarget.WEAPON
    ),
    GATHERING(
        displayName = "Gathering",
        rarity = Rarity.RARE,
        target = EnchantmentTarget.TOOL
    ),
    PRECISION(
        displayName = "Precision",
        rarity = Rarity.RARE,
        target = EnchantmentTarget.WEAPON
    ),
    REAPER(
        displayName = "Reaper's Scythe",
        rarity = Rarity.EPIC,
        target = EnchantmentTarget.WEAPON
    ),
    SOULBOUND(
        rarity = Rarity.RARE,
        displayName = "Soul bound",
        target = EnchantmentTarget.VANISHABLE
    ),
    LIGHTNING_STRIKE(
        rarity = Rarity.RARE,
        displayName = "ShockWave",
    ),
    JETPACK(
        rarity = Rarity.RARE,
        displayName = "Jetpack",
        target = EnchantmentTarget.WEARABLE
    ),
    LAVA_WALKER(
        rarity = Rarity.RARE,
        displayName = "Lava Walker",
        target = EnchantmentTarget.ARMOR_FEET
    ),
    CLOAKING(
        rarity = Rarity.COMMON,
        displayName = "Cloaking",
        target = EnchantmentTarget.WEAPON
    ),
    PANDORA_BOX(
        rarity = Rarity.EPIC,
        displayName = "Pandora's Box",
        target = EnchantmentTarget.WEAPON
    ),
    ENDER_INSTINCT_DODGE(
        rarity = Rarity.LEGENDARY,
        displayName = "Ender Instinct",
        target = EnchantmentTarget.ARMOR_FEET
    ),
    EXPLOSIVE_ARROW(
        displayName = "ShatterShot",
        rarity = Rarity.RARE,
        target = EnchantmentTarget.BOW
    ),
    MAGNET(
        displayName = "Magnet",
        rarity = Rarity.RARE,
        target = EnchantmentTarget.TOOL
    ),
    TELEPORT(
        displayName = "Teleport",
        rarity = Rarity.RARE,
        target = EnchantmentTarget.ALL
    ),
    TREE_CHOPPER(
        displayName = "Timber",
        rarity = Rarity.COMMON,
        target = EnchantmentTarget.TOOL
    ),
    AUTO_SMELT(
        rarity = Rarity.EPIC,
        displayName = "Refinery",
        target = EnchantmentTarget.TOOL
    ),
    VAMPIRE(
        displayName = "Soul Leech",
        rarity = Rarity.EPIC,
        target = EnchantmentTarget.WEAPON
    ),
    GRID_BREAKER(
        displayName = "GigaBreaker",
        rarity = Rarity.EPIC,
        target = EnchantmentTarget.TOOL
    );

    val displayNameWithColor: String = rarity.color.toString() + displayName

    companion object {
        fun getRandomEnchantment(itemStack: ItemStack) {
            val totalWeight = entries.toTypedArray().sumOf { it.rarity.weight }
            var random = Math.random() * totalWeight
            for (enchantment in entries) {
                random -= enchantment.rarity.weight
                if (random <= 0) {
                    val level = Random.nextInt(1, enchantment.maxLevel + 1)
                    itemStack.applyEnchantment(enchantment, level)
                    break
                }
            }
        }
    }
}