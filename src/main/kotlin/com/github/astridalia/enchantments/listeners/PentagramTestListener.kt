package com.github.astridalia.enchantments.listeners

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.math.cos
import kotlin.math.sin


object PentagramTestListener : Listener {
    fun getPentagramPoints(center: Location, radius: Double, height: Double, rotation: Double): List<Location> {
        return (0..4).map {
            val angle = 2 * Math.PI * it / 5 + Math.toRadians(rotation - 18.0)  // Starts from the top and rotates
            Location(center.world,
                center.x + radius * cos(angle),
                center.y + height,
                center.z + radius * sin(angle))
        }
    }

//    @EventHandler
//    fun onPlayerMove(event: PlayerMoveEvent) {
//        val player = event.player
//        val rotation = (System.currentTimeMillis() / 100) % 360  // Continuous rotation
//        val points = getPentagramPoints(player.location, 1.5, 0.1, rotation.toDouble())  // 5 blocks radius, 2 blocks above ground
//        connectPentagramPoints(points)
//    }

    private fun drawLine(world: World, start: Location, end: Location) {
        val distance = start.distance(end)
        val count = (distance * 10).toInt()  // More points for a smoother line
        (0..count).forEach { i ->
            val x = start.x + (end.x - start.x) * i / count
            val y = start.y + (end.y - start.y) * i / count
            val z = start.z + (end.z - start.z) * i / count
            world.spawnParticle(Particle.REDSTONE, x, y, z, 1, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(org.bukkit.Color.BLUE, 1.0f))
        }
    }

    private fun connectPentagramPoints(points: List<Location>) {
        val indices = listOf(0, 2, 4, 1, 3, 0)  // Order to connect points for a pentagram
        for (i in 0 until indices.size - 1) {
            drawLine(points[i].world, points[indices[i]], points[indices[i + 1]])
        }
        points.forEach { point ->
            point.world.spawnParticle(Particle.SMOKE_NORMAL, point, 1, 0.0, 0.0, 0.0, 0.0)
        }
    }
}