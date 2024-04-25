package com.github.astridalia.character

import com.github.astridalia.character.CharacterProfile.applyStatistic
import com.github.astridalia.enchantments.CustomEnchantment.namespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import kotlin.math.max

object CharacterProfile {

    private fun Player.resetToDefaults() {
        val baseHealth = this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.defaultValue ?: 20.0
        val baseAttack = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.defaultValue ?: 1.0

        this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = baseHealth
        this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = baseAttack
    }

    fun Player.applyStatistic(characterClasses: CharacterClasses, value: Int) {
        val baseHealth = this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.defaultValue ?: 20.0
        val baseAttack = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.defaultValue ?: 1.0
        resetToDefaults()
        setStatistic(Statistics.STRENGTH, characterClasses.defaultStatistics.strength)
        setStatistic(Statistics.AGILITY, characterClasses.defaultStatistics.agility)
        setStatistic(Statistics.DEFENSE, characterClasses.defaultStatistics.defense)
        val classStrength = getStatistic(Statistics.STRENGTH)
        val classDefense = getStatistic(Statistics.DEFENSE)
        val classAgility = getStatistic(Statistics.AGILITY)
        this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = max((baseHealth + (classDefense*2)+classStrength), 20.0)
        this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = max((baseAttack + (classStrength*0.5)+(classAgility*0.2)), 1.0)
    }

    fun Player.setStatistic(statistics: Statistics, value: Int) {
        val statisticsKey = statistics.name.lowercase().namespacedKey()
        this.persistentDataContainer.set(statisticsKey, PersistentDataType.INTEGER, value)
    }

    fun Player.getStatistic(statistics: Statistics): Int {
        val statisticsKey = statistics.name.lowercase().namespacedKey()
        return this.persistentDataContainer.get(statisticsKey, PersistentDataType.INTEGER) ?: 0
    }
}