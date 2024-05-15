package com.github.astridalia.character.effects

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PentagramEffect(private val player: Player) : BukkitRunnable() {
    private val radius = 1.5
    private val height = 0.1

    override fun run() {
        if (!player.isOnline || !player.isValid) {
            this.cancel()
            return
        }

        val rotation = (System.currentTimeMillis() / 100) % 360  // Continuous rotation
        val points = getPentagramPoints(player.location, radius, height, rotation.toDouble())
        connectPentagramPoints(points)
    }

    private fun getPentagramPoints(center: Location, radius: Double, height: Double, rotation: Double): List<Location> {
        return (0..4).map {
            val angle = 2 * PI * it / 5 + Math.toRadians(rotation - 18.0)  // Starts from the top and rotates
            Location(
                center.world,
                center.x + radius * cos(angle),
                center.y + height,
                center.z + radius * sin(angle)
            )
        }
    }

    private fun connectPentagramPoints(points: List<Location>) {
        val indices = listOf(0, 2, 4, 1, 3, 0)  // Order to connect points for a pentagram
        for (i in 0 until indices.size - 1)
            drawLine(points[indices[i]].world, points[indices[i]], points[indices[i + 1]])
        points.forEach { point ->
            point.world.spawnParticle(Particle.SMOKE_NORMAL, point, 1, 0.0, 0.0, 0.0, 0.0)
        }
    }

    private fun drawLine(world: World, start: Location, end: Location) {
        val distance = start.distance(end)
        val count = (distance * 10).toInt()  // More points for a smoother line
        (0..count).forEach { i ->
            val x = start.x + (end.x - start.x) * i / count
            val y = start.y + (end.y - start.y) * i / count
            val z = start.z + (end.z - start.z) * i / count
            world.spawnParticle(
                Particle.REDSTONE,
                x,
                y,
                z,
                1,
                0.0,
                0.0,
                0.0,
                0.0,
                Particle.DustOptions(org.bukkit.Color.BLUE, 1.0f)
            )
        }
    }
}