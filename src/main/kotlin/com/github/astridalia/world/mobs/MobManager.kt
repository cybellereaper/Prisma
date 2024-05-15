package com.github.astridalia.world.mobs

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import java.util.*

object MobManager {
    private val mobPool = mutableListOf<Entity>()


    fun cleanUp() {
        mobPool.removeIf { !it.isValid }
    }

    private val entityClassMap: Map<String, Class<out Entity>?> = mapOf(
        "ZOMBIE" to EntityType.ZOMBIE.entityClass,
        "SKELETON" to EntityType.SKELETON.entityClass,
        "CREEPER" to EntityType.CREEPER.entityClass,
        "SPIDER" to EntityType.SPIDER.entityClass,
        "COW" to EntityType.COW.entityClass,
        "PIG" to EntityType.PIG.entityClass,
        "SHEEP" to EntityType.SHEEP.entityClass,
        "CHICKEN" to EntityType.CHICKEN.entityClass,
        "SQUID" to EntityType.SQUID.entityClass,
        "WOLF" to EntityType.WOLF.entityClass,
        "OCELOT" to EntityType.OCELOT.entityClass,
        "HORSE" to EntityType.HORSE.entityClass,
        "VILLAGER" to EntityType.VILLAGER.entityClass,
        "ALLAY" to EntityType.ALLAY.entityClass,
        "RABBIT" to EntityType.RABBIT.entityClass,
    )

    fun spawnCustomMob(location: Location, entityType: String, customName: String): Entity? {
        val entityClass = entityClassMap[entityType.uppercase()] ?: return null
        val entity = location.world?.spawn(location, entityClass) ?: return null
        try {
            val setCustomNameVisibleMethod = entity::class.java.getMethod("setCustomNameVisible", Boolean::class.java)
            val setCustomNameMethod = entity::class.java.getMethod("setCustomName", String::class.java)
            setCustomNameVisibleMethod.invoke(entity, true)
            setCustomNameMethod.invoke(entity, customName)
            mobPool.add(entity)
        } catch (e: Exception) {
            // Log error or handle exception
            return null
        }

        return entity
    }
}