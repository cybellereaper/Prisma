package com.github.astridalia.character.money

import org.bukkit.entity.Player

interface CurrencyManager {
    val name: String
    fun getBalance(player: Player): Double
    fun setBalance(player: Player, amount: Double)
    fun addBalance(player: Player, amount: Double)
    fun subtractBalance(player: Player, amount: Double): Boolean

    fun pay(player: Player, amount: Double): Boolean {
        if (subtractBalance(player, amount)) return true
        player.sendMessage("§cYou do not have enough coins to pay §6$amount §acoins.")
        return false
    }
}