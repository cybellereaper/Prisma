package com.github.astridalia.utils

import java.util.UUID

object CooldownManager {
    enum class CooldownCause {
        ENCHANTMENT,
        SPELL,
    }

    private val cooldowns: MutableMap<Pair<UUID, CooldownCause>, Long> = mutableMapOf()

    /**
     * Sets a cooldown for a specific player and cause.
     * @param uuid The UUID of the player.
     * @param cause The cause of the cooldown.
     * @param durationMillis The duration of the cooldown in milliseconds.
     */
    fun setCooldown(uuid: UUID, cause: CooldownCause, durationMillis: Long) {
        val key = Pair(uuid, cause)
        cooldowns[key] = System.currentTimeMillis() + durationMillis
    }

    /**
     * Checks if a cooldown is active for a specific player and cause.
     * @param uuid The UUID of the player.
     * @param cause The cause of the cooldown.
     * @return true if the cooldown is active, false otherwise.
     */
    fun isCooldownActive(uuid: UUID, cause: CooldownCause): Boolean {
        val key = Pair(uuid, cause)
        return cooldowns[key]?.let { it > System.currentTimeMillis() } ?: false
    }

    /**
     * Gets the remaining cooldown time in milliseconds for a specific player and cause.
     * @param uuid The UUID of the player.
     * @param cause The cause of the cooldown.
     * @return The remaining time in milliseconds, or 0 if no cooldown is active.
     */
    fun getRemainingCooldown(uuid: UUID, cause: CooldownCause): Long {
        val key = Pair(uuid, cause)
        return cooldowns[key]?.let { it - System.currentTimeMillis() }?.takeIf { it > 0 } ?: 0
    }

    /**
     * Clears a specific cooldown for a player and cause.
     * @param uuid The UUID of the player.
     * @param cause The cause of the cooldown.
     */
    fun clearCooldown(uuid: UUID, cause: CooldownCause) {
        val key = Pair(uuid, cause)
        cooldowns.remove(key)
    }

    /**
     * Clears all cooldowns for a specific player.
     * @param uuid The UUID of the player.
     */
    fun clearAllCooldownsForPlayer(uuid: UUID) {
        cooldowns.keys.removeIf { it.first == uuid }
    }
}