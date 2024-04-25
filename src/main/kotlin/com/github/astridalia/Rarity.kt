package com.github.astridalia

import org.bukkit.ChatColor

enum class Rarity(
    val color: ChatColor
) {
    UNIQUE(ChatColor.GRAY), COMMON(ChatColor.YELLOW),
    RARE(ChatColor.GREEN), EPIC(ChatColor.DARK_PURPLE),
    LEGENDARY(ChatColor.DARK_RED);
}