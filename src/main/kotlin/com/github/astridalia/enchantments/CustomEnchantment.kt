package com.github.astridalia.enchantments

import com.github.astridalia.Rarity
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CustomEnchantment : KoinComponent {
    private val plugin: JavaPlugin by inject()
    private fun namespacedKey(name: String) = NamespacedKey(plugin, name)

    fun ItemStack.setEnchantmentLevel(customEnchantments: CustomEnchantments, level: Int = 1) {
        val enchantmentKey = namespacedKey(customEnchantments.name.lowercase())
        val itemMeta = itemMeta ?: return
        itemMeta.persistentDataContainer.set(enchantmentKey, PersistentDataType.INTEGER, level)
        updateItemLore(customEnchantments,itemMeta, level)
        this.itemMeta = itemMeta
    }

    fun ItemStack.applyEnchantment(customEnchantments: CustomEnchantments, level: Int = 1) {
        val enchantmentKey = namespacedKey(customEnchantments.name.lowercase())
        val itemMeta = itemMeta ?: return
        if (!customEnchantments.applicableMaterials.any { it == this.type }) return
        val currentLevel = itemMeta.persistentDataContainer.getOrDefault(enchantmentKey, PersistentDataType.INTEGER, 0) + level
        itemMeta.persistentDataContainer.set(enchantmentKey, PersistentDataType.INTEGER, currentLevel)
        updateItemLore(customEnchantments, itemMeta, currentLevel)
        this.itemMeta = itemMeta
    }

    private fun updateItemLore(customEnchantments: CustomEnchantments, itemMeta: ItemMeta, level: Int) {
        val enchantmentName = customEnchantments.displayNameWithColor
        val enchantmentLore = "$enchantmentName ${level.toRoman()}"
        val loreList = itemMeta.lore ?: mutableListOf()
        val existingIndex = loreList.indexOfFirst { it.startsWith(enchantmentName) }
        if (existingIndex != -1) loreList[existingIndex] = enchantmentLore else loreList.add(enchantmentLore)
        itemMeta.lore = loreList
    }

    private fun Int.toRoman(): String {
        val rarity = when (this) {
            in 1..3 -> Rarity.COMMON.color
            in 4..6 -> Rarity.UNIQUE.color
            in 7..9 -> Rarity.RARE.color
            10 -> Rarity.EPIC.color
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
            else -> "" // This will never be reached due to the earlier check
        }

        return "$rarity$numeral"
    }

    fun ItemStack.getEnchantOf(enchantments: CustomEnchantments): Int {
        val itemMeta = itemMeta ?: return 0
        val namespacedKey = namespacedKey(enchantments.name.lowercase())
        return itemMeta.persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.INTEGER, 0)
    }
}
