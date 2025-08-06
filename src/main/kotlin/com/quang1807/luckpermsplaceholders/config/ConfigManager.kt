package com.quang1807.luckpermsplaceholders.config

import com.quang1807.luckpermsplaceholders.LuckPermsPlaceholders
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigManager(private val plugin: LuckPermsPlaceholders) {

    private lateinit var config: YamlConfiguration
    private lateinit var configFile: File

    var dateFormat: String = "dd/MM/yyyy HH:mm:ss"
        private set

    var updateInterval: Long = 1000L // milliseconds
        private set

    var enableDebug: Boolean = false
        private set

    fun loadConfig() {
        configFile = File(plugin.dataFolder, "config.yml")

        if (!configFile.exists()) {
            plugin.saveDefaultConfig()
            createDefaultConfig()
        }

        config = YamlConfiguration.loadConfiguration(configFile)

        // Load configuration values
        dateFormat = config.getString("settings.date-format", "dd/MM/yyyy HH:mm:ss")!!
        updateInterval = config.getLong("settings.update-interval", 1000L)
        enableDebug = config.getBoolean("settings.debug", false)

        plugin.logger.info("Configuration loaded successfully!")
    }

    private fun createDefaultConfig() {
        configFile.parentFile.mkdirs()

        val defaultConfig = """
# LuckPerms-More-Placeholders Configuration
# Author: quang1807

settings:
  # Date format for displaying time (Java SimpleDateFormat)
  # Examples:
  # dd/MM/yyyy HH:mm:ss -> 25/12/2024 15:30:45
  # yyyy-MM-dd HH:mm:ss -> 2024-12-25 15:30:45
  # HH:mm:ss -> 15:30:45
  date-format: "dd/MM/yyyy HH:mm:ss"
  
  # Update interval for placeholders in milliseconds
  # Default: 1000ms (1 second)
  update-interval: 1000
  
  # Enable debug mode
  debug: false

# Placeholder settings
placeholders:
  # Format for time display when no temp group found
  no-temp-group: "No temp group"
  
  # Format for expired temp group
  expired: "Expired"
  
  # Format for permanent group
  permanent: "Permanent"
        """.trimIndent()

        configFile.writeText(defaultConfig)
    }

    fun getConfig(): YamlConfiguration = config

    fun getString(path: String, default: String): String {
        return config.getString(path, default)!!
    }

    fun getBoolean(path: String, default: Boolean): Boolean {
        return config.getBoolean(path, default)
    }

    fun getLong(path: String, default: Long): Long {
        return config.getLong(path, default)
    }
}