package com.github.astridalia.character.money

import com.github.astridalia.enchantments.CustomEnchantment.toNamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

data class PersistentCurrencyManager(override val name: String) : CurrencyManager {
    override fun getBalance(player: Player): Double =
        player.persistentDataContainer.get(name.toNamespacedKey(), PersistentDataType.DOUBLE) ?: 0.0

    override fun setBalance(player: Player, amount: Double) {
        player.persistentDataContainer.set(name.toNamespacedKey(), PersistentDataType.DOUBLE, amount)
    }

    override fun addBalance(player: Player, amount: Double) {
        val newBalance = getBalance(player) + amount
        setBalance(player, newBalance)
    }

    override fun subtractBalance(player: Player, amount: Double): Boolean {
        val currentBalance = getBalance(player)
        if (currentBalance >= amount) {
            setBalance(player, currentBalance - amount)
            return true
        }
        return false
    }

}