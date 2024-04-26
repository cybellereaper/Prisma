package com.github.astridalia.spells

import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object SpellManager : Listener {
    private val spells: MutableList<Spell> = mutableListOf()
    private val playerClicks: MutableMap<Player, PlayerClickData> = mutableMapOf()
    private const val sequenceTimeout = 5000

    private fun Player.spellAoe(range: Double): List<Entity> {
        return this.world.getNearbyEntities(this.location, range, range, range)
            .filterIsInstance<LivingEntity>()
            .filter { it != this }
    }


    private fun registerSpell(spell: Spell) {
        spells.add(spell)
    }

    fun testSpells() {
        val spell = Spell(
            name = "Fireball",
            sequence = listOf("LEFT", "RIGHT", "LEFT"),
            mode = SpellMode.SINGLE,
            action = { player, _ ->
                val location = player.eyeLocation
                val direction = location.direction
                val fireball = player.world.spawnEntity(location, EntityType.FIREBALL) as Fireball
                fireball.direction = direction
                fireball.yield = 2f  // Explosion power
                fireball.shooter = player
                player.sendMessage("You have launched a Fireball!")
            }
        )
        registerSpell(spell)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        val clickType = when {
            action.name.contains("LEFT_CLICK") -> "LEFT"
            action.name.contains("RIGHT_CLICK") -> "RIGHT"
            else -> return
        }

        val clickData = playerClicks.getOrPut(player) { PlayerClickData() }
        if (System.currentTimeMillis() - clickData.lastClickTime > sequenceTimeout) {
            clickData.clicks.clear()
        }
        clickData.clicks.add(clickType)
        clickData.lastClickTime = System.currentTimeMillis()
        spells.forEach { spell ->
            if (clickData.clicks.takeLast(spell.sequence.size) == spell.sequence) {
                when (spell.mode) {
                    SpellMode.SINGLE -> {
                        val target = player.getTargetEntity(50)
                        spell.action(player, target)
                    }

                    SpellMode.AOE -> {
                        val targets = player.spellAoe(10.0)
                        targets.forEach { target -> spell.action(player, target) }
                    }

                    SpellMode.SELF -> {
                        spell.action(player, player)
                    }
                }
                clickData.clicks.clear()
            }
        }
    }
}