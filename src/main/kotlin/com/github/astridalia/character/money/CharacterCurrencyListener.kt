package com.github.astridalia.character.money

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent

class CharacterCurrencyListener : Listener {
    private val currencyManager: CurrencyManager = PersistentCurrencyManager("coins")

    @EventHandler
    fun onMobDeath(event: EntityDamageByEntityEvent) {
        val entity = event.entity as? LivingEntity ?: return
        if (entity.health - event.finalDamage <= 0) {
            val damager = event.damager as? Player ?: return
            val moneyToDrop = entity.maxHealth * 0.25
            currencyManager.addBalance(damager, moneyToDrop)
            damager.sendMessage("You have earned $moneyToDrop for defeating a ${entity.type}.")
        }
    }

    @EventHandler
    fun onPlayerKill(event: PlayerDeathEvent) {
        val killer = event.entity.killer ?: return
        val victim = event.entity
        val amountToTransfer = currencyManager.getBalance(victim) * 0.12
        if (currencyManager.subtractBalance(victim, amountToTransfer)) {
            currencyManager.addBalance(killer, amountToTransfer)
            killer.sendMessage("You have earned $amountToTransfer currency from defeating ${victim.name}.")
            victim.sendMessage("You have lost $amountToTransfer currency.")
        }
    }
}