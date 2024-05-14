package com.github.astridalia.enchantments

import com.github.astridalia.utils.ItemRarity
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


object CustomEnchantment  : KoinComponent {
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

    private fun Int.getRarityColor(): String {
        return when (this) {
            in 1..3 -> ItemRarity.COMMON.color.toString()
            in 4..6 -> ItemRarity.UNIQUE.color.toString()
            in 7..9 -> ItemRarity.RARE.color.toString()
            in 10..15 -> ItemRarity.EPIC.color.toString()
            in 16..20 -> ItemRarity.LEGENDARY.color.toString()
            else -> ItemRarity.COMMON.color.toString()  // Default case for unsupported levels
        }
    }

    private fun Int.toColoredRoman(): String = this.getRarityColor() + this.toRoman()

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
