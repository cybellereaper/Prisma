package com.github.astridalia.utils

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.inventory.ItemStack

enum class ItemRarity(
    val color: TextColor,
    val weight: Int = 1,
) {
    UNIQUE(NamedTextColor.GRAY, 8),
    COMMON(NamedTextColor.WHITE, 10),
    RARE(NamedTextColor.GREEN, 5),
    EPIC(NamedTextColor.DARK_PURPLE, 3),
    LEGENDARY(NamedTextColor.DARK_RED, 1);
}