package com.github.astridalia.enchantments

import com.github.astridalia.utils.ItemRarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


object CustomEnchantment : KoinComponent {
    private val plugin: JavaPlugin by inject()
    fun String.toNamespacedKey() = NamespacedKey(plugin, this)

    private fun ItemStack.updateMeta(action: ItemMeta.() -> Unit) {
        itemMeta = itemMeta?.apply(action)
    }

    fun ItemStack.setEnchantmentLevel(customEnchantments: CustomEnchantments, level: Int) {
        updateMeta {
            persistentDataContainer.set(
                customEnchantments.name.lowercase().toNamespacedKey(),
                PersistentDataType.INTEGER,
                level
            )
            updateItemLore(customEnchantments, this, level)
        }
    }

    fun ItemStack.removeEnchantment(customEnchantments: CustomEnchantments) {
        updateMeta {
            persistentDataContainer.remove(customEnchantments.name.lowercase().toNamespacedKey())
            lore = lore?.filterNot { it.startsWith(customEnchantments.displayNameWithColor) }?.toMutableList()
        }
    }

    fun ItemStack.applyEnchantment(customEnchantments: CustomEnchantments, level: Int = 1) {
        if (!customEnchantments.target.includes(this.type)) return
        updateMeta {
            val namespacedKey = customEnchantments.name.lowercase().toNamespacedKey()
            val currentLevel =
                persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.INTEGER, 0) + level
            persistentDataContainer.set(namespacedKey, PersistentDataType.INTEGER, currentLevel)
            updateItemLore(customEnchantments, this, currentLevel)
        }
    }


    private fun updateItemLore(customEnchantments: CustomEnchantments, itemMeta: ItemMeta, level: Int) {
        val enchantmentLore = "${customEnchantments.displayNameWithColor} ${level.toColoredRoman()}"
        itemMeta.lore = itemMeta.lore.orEmpty().toMutableList().apply {
            val index = indexOfFirst { it.startsWith(customEnchantments.displayNameWithColor) }
            if (index != -1) this[index] = enchantmentLore else add(enchantmentLore)
        }
    }


    private fun Int.getRarityColor(): TextColor {
        return when (this) {
            in 1..3 -> ItemRarity.COMMON.color
            in 4..6 -> ItemRarity.UNIQUE.color
            in 7..9 -> ItemRarity.RARE.color
            in 10..15 -> ItemRarity.EPIC.color
            in 16..20 -> ItemRarity.LEGENDARY.color
            else -> ItemRarity.COMMON.color
        }
    }

    fun String.convertToLegacy(textColor: TextColor): String {
        val component = Component.text(this).color(textColor)
        val legacyComponentSerializer = LegacyComponentSerializer.legacy('&').serialize(component)
        return legacyComponentSerializer.replace("&", "§")
    }

    private fun Int.toColoredRoman(): String = this.toRoman().convertToLegacy(this.getRarityColor())

    private fun Int.toRoman(): String {
        if (this <= 0) return ""
        val romanNumerals = listOf(
            1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
            100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
            10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
        )
        var num = this
        val roman = StringBuilder()
        for ((value, numeral) in romanNumerals) {
            while (num >= value) {
                roman.append(numeral)
                num -= value
            }
        }
        return roman.toString()
    }

    fun ItemStack.getEnchantmentLevel(customEnchantments: CustomEnchantments): Int =
        itemMeta?.persistentDataContainer?.getOrDefault(
            customEnchantments.name.lowercase().toNamespacedKey(),
            PersistentDataType.INTEGER,
            0
        ) ?: 0

}
