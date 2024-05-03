package com.github.astridalia.character.mail

import java.util.*

data class Mailbox(
    val to: UUID,
    val from: UUID,
    val messages: MutableList<Message> = mutableListOf()
) {
    init {
        require(to != from) { "Sender and receiver cannot be the same." }
    }
}