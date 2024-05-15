package com.github.astridalia.character.currency

import com.github.astridalia.enchantments.CustomEnchantment.toNamespacedKey
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.persistence.PersistentDataType

object CharacterCurrency : Listener {

    @EventHandler
    fun onMob(event: EntityDamageByEntityEvent) {
        val entity = event.entity as? LivingEntity ?: return  // Ensure the entity is a LivingEntity

        // Check if the damage dealt is fatal
        if (entity.health - event.finalDamage <= 0) {
            val damager = event.damager as? Player ?: return  // Ensure the damager is a Player

            val moneyToDrop = entity.maxHealth * 0.25
            damager.addBalance(moneyToDrop)
        }
    }

    @EventHandler
    fun onPlayerKill(event: PlayerDeathEvent) {
        val killer = event.entity.killer ?: return
        val victim = event.entity
        val amountToTransfer = victim.getBalance() * 0.12
        killer.addBalance(amountToTransfer)
        victim.subtractBalance(amountToTransfer)
        killer.sendMessage("You have earned $amountToTransfer currency from defeating ${victim.name}.")
        victim.sendMessage("You have lost $amountToTransfer currency.")
    }


    fun Player.subtractBalance(amount: Double): Boolean {
        val currentBalance = getBalance()
        if (currentBalance >= amount) {
            setBalance(currentBalance - amount)
            sendMessage("§aYou have removed §6$amount §acoins.")
            return true
        }
        sendMessage("§cYou do not have enough coins to remove §6$amount §acoins.")
        return false
    }

    fun Player.addBalance(amount: Double) {
        val newBalance = getBalance() + amount
        setBalance(newBalance)
        sendMessage("§aYou have received §6$amount §acoins.")
    }

    fun Player.setBalance(amount: Double) {
        persistentDataContainer.set("coins".toNamespacedKey(), PersistentDataType.DOUBLE, amount)
    }

    fun Player.getBalance(): Double =
        persistentDataContainer.get("coins".toNamespacedKey(), PersistentDataType.DOUBLE) ?: run {
            persistentDataContainer.set("coins".toNamespacedKey(), PersistentDataType.DOUBLE, 0.0)
            0.0
        }
}