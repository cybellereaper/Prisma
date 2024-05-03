package com.github.astridalia

import co.aikar.commands.PaperCommandManager
import com.github.astridalia.enchantments.listeners.*
import com.github.astridalia.spells.SpellManager
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class Prisma : JavaPlugin(), KoinComponent {

    private val paperCommandManager by inject<PaperCommandManager>()
    private val gridPickaxe by inject<GridPickaxeListener>()
    private val autoSmelting by inject<AutoSmeltingListener>()
    private val magnetListener by inject<MagnetListener>()
    private val explosiveArrows by inject<ExplosiveArrowListener>()
    private val damageIndicator by inject<DamageIndicator>()
    private val enchantmentSimpleAttacksListener by inject<EnchantmentSimpleAttacksListener>()
    private val myCommands by inject<MyCommands>()


    private val lavaWalkerListener by inject<LavaWalkerListener>()

    private val appModule = module {
        single { MyCommands }
        single { DamageIndicator }
        single { ExplosiveArrowListener }
        single { MagnetListener }
        single { AutoSmeltingListener }
        single { GridPickaxeListener }
        single<JavaPlugin> { this@Prisma }
        single<Plugin> { this@Prisma }
        single { LavaWalkerListener }
        single { PaperCommandManager(get()) }
        single { EnchantmentSimpleAttacksListener }
    }

    override fun onEnable() {
        startKoin {
            modules(appModule)
        }
        
        SpellManager.testSpells()

        paperCommandManager.registerCommand(myCommands)
        registerEventListeners(
            gridPickaxe,
            autoSmelting,
            magnetListener,
            explosiveArrows,
            enchantmentSimpleAttacksListener,
            LightningArrowListener,
            VampireHitListener,
            damageIndicator,
            lavaWalkerListener,
            EnderInstinctListener,
            PandoraListener,
            SpellManager
        )
    }

    private fun registerEventListeners(vararg listeners: Listener) {
        val pluginManager = server.pluginManager
        listeners.forEach { pluginManager.registerEvents(it, this) }
    }

    override fun onDisable() {
        stopKoin()
    }
}