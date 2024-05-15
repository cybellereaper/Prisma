package com.github.astridalia.enchantments

import com.github.astridalia.enchantments.CustomEnchantment.applyEnchantment
import com.github.astridalia.enchantments.CustomEnchantment.convertToLegacy
import com.github.astridalia.utils.ItemRarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

enum class CustomEnchantments(
    val rarity: ItemRarity = ItemRarity.COMMON,
    val displayName: String,
    val maxLevel: Int = 5,
    val target: EnchantmentTarget = EnchantmentTarget.ALL
) {
    FROSTBITE(
        rarity = ItemRarity.EPIC,
        displayName = "Frostbite",
        target = EnchantmentTarget.WEAPON
    ),
    VENOMOUS(
        rarity = ItemRarity.LEGENDARY,
        displayName = "Venomous",
        target = EnchantmentTarget.WEAPON
    ),
    GATHERING(
        rarity = ItemRarity.RARE,
        displayName = "Gathering",
        target = EnchantmentTarget.TOOL
    ),
    PRECISION(
        rarity = ItemRarity.RARE,
        displayName = "Precision",
        target = EnchantmentTarget.WEAPON
    ),
    REAPER(
        rarity = ItemRarity.EPIC,
        displayName = "Reaper's Scythe",
        target = EnchantmentTarget.WEAPON
    ),
    SOUL_BOUND(
        rarity = ItemRarity.RARE,
        displayName = "Soul bound",
        target = EnchantmentTarget.VANISHABLE
    ),
    LIGHTNING_STRIKE(
        rarity = ItemRarity.RARE,
        displayName = "ShockWave",
    ),
    JETPACK(
        rarity = ItemRarity.RARE,
        displayName = "Jetpack",
        target = EnchantmentTarget.WEARABLE
    ),
    LAVA_WALKER(
        rarity = ItemRarity.RARE,
        displayName = "Lava Walker",
        target = EnchantmentTarget.ARMOR_FEET
    ),
    CLOAKING(
        rarity = ItemRarity.COMMON,
        displayName = "Cloaking",
        target = EnchantmentTarget.WEAPON
    ),
    PANDORA_BOX(
        rarity = ItemRarity.EPIC,
        displayName = "Pandora's Box",
        target = EnchantmentTarget.WEAPON
    ),
    ENDER_INSTINCT_DODGE(
        rarity = ItemRarity.LEGENDARY,
        displayName = "Ender Instinct",
        target = EnchantmentTarget.ARMOR_FEET
    ),
    EXPLOSIVE_ARROW(
        rarity = ItemRarity.RARE,
        displayName = "ShatterShot",
        target = EnchantmentTarget.BOW
    ),
    MAGNET(
        rarity = ItemRarity.RARE,
        displayName = "Magnet",
        target = EnchantmentTarget.TOOL
    ),
    BACKDRAFT(
        rarity = ItemRarity.RARE,
        displayName = "Backdraft",
        target = EnchantmentTarget.WEAPON
    ),
    NULLIFY(
        rarity = ItemRarity.LEGENDARY,
        displayName = "Nullify",
        target = EnchantmentTarget.ARMOR_TORSO
    ),
    TELEPORT(
        rarity = ItemRarity.RARE,
        displayName = "Teleport",
        target = EnchantmentTarget.ALL
    ),
    TREE_CHOPPER(
        rarity = ItemRarity.COMMON,
        displayName = "Timber",
        target = EnchantmentTarget.TOOL
    ),
    REFINERY(
        rarity = ItemRarity.EPIC,
        displayName = "Refinery",
        target = EnchantmentTarget.TOOL
    ),
    VAMPIRE(
        rarity = ItemRarity.EPIC,
        displayName = "Soul Leech",
        target = EnchantmentTarget.WEAPON
    ),
    GRID_BREAKER(
        rarity = ItemRarity.EPIC,
        displayName = "Giga Breaker",
        target = EnchantmentTarget.TOOL
    );

    val displayNameWithColor: String = displayName.convertToLegacy(rarity.color)

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