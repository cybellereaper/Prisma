package com.github.astridalia.utils

import org.bukkit.ChatColor

enum class Rarity(
    val color: ChatColor,
    val weight: Int = 1
) {
    UNIQUE(ChatColor.GRAY, 8),
    COMMON(ChatColor.YELLOW, 10),
    RARE(ChatColor.GREEN, 5),
    EPIC(ChatColor.DARK_PURPLE, 3),
    LEGENDARY(ChatColor.DARK_RED, 1);
}