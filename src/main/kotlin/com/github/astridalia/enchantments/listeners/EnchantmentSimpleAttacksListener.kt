package com.github.astridalia.enchantments.listeners

import com.github.astridalia.enchantments.CustomEnchantment.getEnchantOf
import com.github.astridalia.enchantments.CustomEnchantments
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object EnchantmentSimpleAttacksListener : Listener {

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val damage = event.damager as? Player ?: return
        val itemInMainHand = damage.inventory.itemInMainHand
        val lightningStrikeLevel = itemInMainHand.getEnchantOf(CustomEnchantments.LIGHTNING_STRIKE)
        if (lightningStrikeLevel <= 0) return
        repeat(lightningStrikeLevel) {
            event.entity.location.world?.spawn(event.entity.location, LightningStrike::class.java)
        }
    }


    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        val itemOnCursor = event.player.itemOnCursor
        val cloakingLevel = itemOnCursor.getEnchantOf(CustomEnchantments.CLOAKING)
        if (cloakingLevel <= 0) return
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.LEFT_CLICK_AIR) {
            val potionEffect = PotionEffect(PotionEffectType.INVISIBILITY, 255 * cloakingLevel, 1)
            event.player.addPotionEffect(potionEffect)
        }

    }
}