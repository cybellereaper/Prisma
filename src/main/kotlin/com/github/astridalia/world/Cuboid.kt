package com.github.astridalia.world

import kotlinx.serialization.Serializable
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.util.Vector

@Serializable
data class Cuboid(
    @Serializable(with = WorldSerializer::class)
    var world: World,
    @Serializable(with = VectorSerializer::class)
    val minimumPoint: Vector,
    @Serializable(with = VectorSerializer::class)
    val maximumPoint: Vector
) : Cloneable, ConfigurationSerializable, Iterable<Block> {

    // Utilize primary constructor for all initializations to reduce redundancy
    constructor(cuboid: Cuboid) : this(cuboid.world, cuboid.minimumPoint, cuboid.maximumPoint)

    constructor(world: World, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) :
            this(world, Vector(minOf(x1, x2), minOf(y1, y2), minOf(z1, z2)), Vector(maxOf(x1, x2), maxOf(y1, y2), maxOf(z1, z2)))

    constructor(blockPosOne: Location, blockPosTwo: Location) : this(
        blockPosOne.world ?: throw IllegalArgumentException("Location has no world"),
        Vector(minOf(blockPosOne.x, blockPosTwo.x), minOf(blockPosOne.y, blockPosTwo.y), minOf(blockPosOne.z, blockPosTwo.z)),
        Vector(maxOf(blockPosOne.x, blockPosTwo.x), maxOf(blockPosOne.y, blockPosTwo.y), maxOf(blockPosOne.z, blockPosTwo.z))
    )

    // Check if a location or vector is within the cuboid
    fun contains(location: Location) = location.world == world && location.toVector().isInAABB(minimumPoint, maximumPoint)
    fun contains(vector: Vector) = vector.isInAABB(minimumPoint, maximumPoint)

    // Serialize the cuboid data for configuration
    override fun serialize(): MutableMap<String, Any> = mutableMapOf(
        "world" to world.name,
        "minimumPoint" to listOf(minimumPoint.x, minimumPoint.y, minimumPoint.z),
        "maximumPoint" to listOf(maximumPoint.x, maximumPoint.y, maximumPoint.z)
    )

    // Provide an iterator for the blocks within the cuboid
    override fun iterator(): Iterator<Block> = blocks.iterator()

    // Generate a list of all blocks within the cuboid
    val blocks: List<Block>
        get() = (minimumPoint.blockX..maximumPoint.blockX).flatMap { x ->
            (minimumPoint.blockY..maximumPoint.blockY).flatMap { y ->
                (minimumPoint.blockZ..maximumPoint.blockZ).mapNotNull { z ->
                    world.getBlockAt(x, y, z)
                }
            }
        }

    // Computed properties for convenience
    val lowerLocation: Location get() = minimumPoint.toLocation(world)
    val upperLocation: Location get() = maximumPoint.toLocation(world)
    val volume: Int
        get() = ((upperX - lowerX + 1) * (upperY - lowerY + 1) * (upperZ - lowerZ + 1)).toInt()

    // Direct access properties for the cuboids dimensions
    val lowerX: Double get() = minimumPoint.x
    val lowerY: Double get() = minimumPoint.y
    val lowerZ: Double get() = minimumPoint.z
    val upperX: Double get() = maximumPoint.x
    val upperY: Double get() = maximumPoint.y
    val upperZ: Double get() = maximumPoint.z
}