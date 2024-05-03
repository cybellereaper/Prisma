package com.github.astridalia.character.mail

import java.time.LocalDateTime

data class Message(
    val content: String,
    var isRead: Boolean = false,
    val receivedAt: LocalDateTime = LocalDateTime.now(),
    var isDeleted: Boolean = false
)
