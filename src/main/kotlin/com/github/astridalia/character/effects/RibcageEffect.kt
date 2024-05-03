package com.github.astridalia.character.effects

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin


class RibcageEffect(private val player: Player) : BukkitRunnable() {
    override fun run() {
        if (!player.isOnline) {
            this.cancel() // Stop the effect if the player is offline
            return
        }

        val location = player.location.clone().add(0.0, 1.2, 0.0) // Adjust to chest height
        val world = player.world

        // Parameters for the ribcage
        val ribs = 6 // Number of ribs on each side
        val ribDepth = 0.25 // Depth of each rib
        val ribLength = 0.6 // Length of each rib
        val ribSpacing = 0.15 // Vertical spacing between ribs

        // Particle options for the ribcage
        val particleColor = Color.fromRGB(255, 255, 255) // White color for bones
        val particleOptions = Particle.DustOptions(particleColor, 1.0f) // Particle size

        for (i in 0 until ribs) {
            val y = ribSpacing * i - ribs * ribSpacing / 2 // Position each rib vertically
            for (angle in 0..180 step 10) { // Create a semi-circle for each rib
                val rad = Math.toRadians(angle.toDouble())
                val x = cos(rad) * ribLength
                val z = sin(rad) * ribDepth

                // Left rib
                val leftRib = location.clone().add(Vector(x, y, z))
                world.spawnParticle(Particle.REDSTONE, leftRib, 1, particleOptions)

                // Right rib (mirror)
                val rightRib = location.clone().add(Vector(x, y, -z))
                world.spawnParticle(Particle.REDSTONE, rightRib, 1, particleOptions)
            }
        }
    }
}
