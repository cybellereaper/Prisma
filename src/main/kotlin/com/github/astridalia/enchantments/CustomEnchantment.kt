package com.github.astridalia.enchantments

import com.github.astridalia.utils.Rarity
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
        if (customEnchantments.applicableMaterials.contains(type)) {
            updateMeta {
                val namespacedKey = customEnchantments.name.lowercase().toNamespacedKey()
                val currentLevel =
                    persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.INTEGER, 0) + level
                persistentDataContainer.set(namespacedKey, PersistentDataType.INTEGER, currentLevel)
                updateItemLore(customEnchantments, this, currentLevel)
            }
        }
    }

    private fun updateItemLore(customEnchantments: CustomEnchantments, itemMeta: ItemMeta, level: Int) {
        val enchantmentLore = "${customEnchantments.displayNameWithColor} ${level.toRoman()}"
        itemMeta.lore = itemMeta.lore.orEmpty().toMutableList().apply {
            val index = indexOfFirst { it.startsWith(customEnchantments.displayNameWithColor) }
            if (index != -1) this[index] = enchantmentLore else add(enchantmentLore)
        }
    }

    private fun Int.toRoman(): String {
        val rarityColor = when (this) {
            in 1..3 -> Rarity.COMMON.color.toString()
            in 4..6 -> Rarity.UNIQUE.color.toString()
            in 7..9 -> Rarity.RARE.color.toString()
            in 10..15 -> Rarity.EPIC.color.toString()  // Adjusted for numbers up to 15
            in 16..20 -> Rarity.LEGENDARY.color.toString()  // New category for 16-20
            else -> throw IllegalArgumentException("Unsupported level: $this")
        }

        val numeral = when (this) {
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            6 -> "VI"
            7 -> "VII"
            8 -> "VIII"
            9 -> "IX"
            10 -> "X"
            11 -> "XI"
            12 -> "XII"
            13 -> "XIII"
            14 -> "XIV"
            15 -> "XV"
            16 -> "XVI"
            17 -> "XVII"
            18 -> "XVIII"
            19 -> "XIX"
            20 -> "XX"
            else -> "" // This will never be reached due to the earlier check
        }
        return rarityColor + numeral
    }

    fun ItemStack.getEnchantmentLevel(customEnchantments: CustomEnchantments): Int {
        return itemMeta?.persistentDataContainer?.getOrDefault(
            customEnchantments.name.lowercase().toNamespacedKey(),
            PersistentDataType.INTEGER,
            0
        ) ?: 0
    }
}
