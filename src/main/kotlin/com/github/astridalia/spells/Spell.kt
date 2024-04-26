package com.github.astridalia.spells

import org.bukkit.entity.Entity
import org.bukkit.entity.Player

data class Spell(
    val name: String,
    val sequence: List<String>,
    val mode: SpellMode,
    val action: (Player, Entity?) -> Unit
)