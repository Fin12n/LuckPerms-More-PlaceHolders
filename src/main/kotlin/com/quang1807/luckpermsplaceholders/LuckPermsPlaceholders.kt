package com.quang1807.luckpermsplaceholders

import com.quang1807.luckpermsplaceholders.commands.LPPlaceholderCommand
import com.quang1807.luckpermsplaceholders.config.ConfigManager
import com.quang1807.luckpermsplaceholders.placeholders.LuckPermsExpansion
import com.quang1807.luckpermsplaceholders.utils.MessageManager
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import org.bukkit.plugin.java.JavaPlugin

class LuckPermsPlaceholders : JavaPlugin() {

    lateinit var luckPerms: LuckPerms
        private set

    lateinit var configManager: ConfigManager
        private set

    lateinit var messageManager: MessageManager
        private set

    override fun onEnable() {
        // Initialize LuckPerms API
        if (!initializeLuckPerms()) {
            logger.severe("LuckPerms not found! Disabling plugin...")
            server.pluginManager.disablePlugin(this)
            return
        }

        // Initialize PlaceholderAPI
        if (!server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
            logger.severe("PlaceholderAPI not found! Disabling plugin...")
            server.pluginManager.disablePlugin(this)
            return
        }

        // Initialize managers
        configManager = ConfigManager(this)
        messageManager = MessageManager(this)

        // Load configurations
        configManager.loadConfig()
        messageManager.loadMessages()

        // Register placeholders
        LuckPermsExpansion(this).register()

        // Register commands
        getCommand("lpplaceholder")?.setExecutor(LPPlaceholderCommand(this))

        logger.info("LuckPerms-More-Placeholders v${description.version} has been enabled!")
        logger.info("Author: ${description.authors.joinToString(", ")}")
    }

    override fun onDisable() {
        logger.info("LuckPerms-More-Placeholders has been disabled!")
    }

    private fun initializeLuckPerms(): Boolean {
        return try {
            luckPerms = LuckPermsProvider.get()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    fun reload() {
        configManager.loadConfig()
        messageManager.loadMessages()
    }
}