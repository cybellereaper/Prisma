package com.github.astridalia.spells

data class PlayerClickData(
    var clicks: MutableList<String> = mutableListOf(),
    var lastClickTime: Long = System.currentTimeMillis()
)