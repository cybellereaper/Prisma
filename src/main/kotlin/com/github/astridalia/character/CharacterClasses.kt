package com.github.astridalia.character

enum class CharacterClasses(
    val defaultStatistics: Profile,
    val description: String = "",
) {
    VAMPIRE(
        defaultStatistics = Profile(
            strength = 6,  // Good physical attacker
            defense = 4,   // Below average defense
            intellect = 7, // Above average intellect for using dark magic
            will = 5,      // Average willpower
            agility = 8    // High agility for quick movements
        ),
        description = "Ancient beings excelling in dark magic and agility. They drain life to heal, but struggle against holy attacks."
    ),
    ELF(
        defaultStatistics = Profile(
            strength = 3,  // Lower physical strength
            defense = 6,   // Slightly above average defense
            intellect = 8, // High intellect for magic
            will = 7,      // High willpower for resistance to magic
            agility = 6    // Above average agility
        ),
        description = "Graceful and intelligent, elves are powerful mages connected to nature, with keen senses and strategic minds."
    ),
    SIREN(
        defaultStatistics = Profile(
            strength = 4,  // Slightly below average physical strength
            defense = 5,   // Average defense
            intellect = 9, // High intellect for enchantments and illusions
            will = 8,      // High willpower, resistant to mental attacks
            agility = 4    // Below average agility
        ),
        description = "Enchanting beings using songs to manipulate foes. They wield mind-bending magic, avoiding direct combat."
    ),
    HUMAN(
        defaultStatistics = Profile(
            strength = 5,  // Average strength
            defense = 7,   // Above average defense, adaptable
            intellect = 5, // Average intellect
            will = 6,      // Slightly above average will, showing resilience
            agility = 7    // Above average agility, versatile
        ),
        description = "Adaptable and resilient, humans are versatile with no extreme weaknesses. They excel through determination and ingenuity."
    )
}

