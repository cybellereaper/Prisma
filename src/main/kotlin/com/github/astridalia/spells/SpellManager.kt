package com.github.astridalia.spells

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SpellManager : Listener {
    private val spells: MutableList<Spell> = mutableListOf()
    private val playerClicks: MutableMap<Player, PlayerClickData> = mutableMapOf()
    private const val TIMEOUT = 5000

    private fun Player.spellAoe(range: Double): List<Entity> =
        this.world.getNearbyEntities(this.location, range, range, range)
            .filterIsInstance<LivingEntity>()
            .filter { it != this }

    private fun registerSpell(spell: Spell) {
        spells.add(spell)
    }

    fun testSpells() {
        registerSpell(Spell(
            name = "Fireball",
            sequence = listOf("LEFT", "RIGHT", "LEFT"),
            mode = SpellMode.SINGLE,
            action = { player, _ ->
                val location = player.eyeLocation
                val fireball = player.world.spawnEntity(location, EntityType.FIREBALL) as Fireball
                fireball.direction = location.direction
                fireball.yield = 2f
                fireball.shooter = player
                player.sendMessage("You have launched a Fireball!")
            }
        ))
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (player.inventory.itemInMainHand.type != Material.STICK) return

        val clickType = when (event.action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> "LEFT"
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> "RIGHT"
            else -> return
        }

        val clickData = playerClicks.getOrPut(player) { PlayerClickData() }
        if (System.currentTimeMillis() - clickData.lastClickTime > TIMEOUT) {
            clickData.clicks.clear()
        }
        clickData.clicks.add(clickType)
        clickData.lastClickTime = System.currentTimeMillis()

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("Clicks: ${clickData.clicks.joinToString(" ")}"))
        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)

        spells.forEach { spell ->
            if (clickData.clicks.takeLast(spell.sequence.size) == spell.sequence) {
                when (spell.mode) {
                    SpellMode.SINGLE -> spell.action(player, player.getTargetEntity(50))
                    SpellMode.AOE -> player.spellAoe(10.0).forEach { target -> spell.action(player, target) }
                    SpellMode.SELF -> spell.action(player, player)
                }
                clickData.clicks.clear()
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(""))
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            }
        }
    }
}