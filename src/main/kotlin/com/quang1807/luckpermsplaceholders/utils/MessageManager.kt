package com.quang1807.luckpermsplaceholders.utils

import com.quang1807.luckpermsplaceholders.LuckPermsPlaceholders
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class MessageManager(private val plugin: LuckPermsPlaceholders) {

    private lateinit var messages: YamlConfiguration
    private lateinit var messageFile: File

    fun loadMessages() {
        messageFile = File(plugin.dataFolder, "messages.yml")

        if (!messageFile.exists()) {
            createDefaultMessages()
        }

        messages = YamlConfiguration.loadConfiguration(messageFile)
        plugin.logger.info("Messages loaded successfully!")
    }

    private fun createDefaultMessages() {
        messageFile.parentFile.mkdirs()

        val defaultMessages = """
# LuckPerms-More-Placeholders Messages
# Author: quang1807
# Use & for color codes

prefix: "&8[&bLuckPerms-More-Placeholders&8] "

commands:
  no-permission: "&cYou don't have permission to use this command!"
  player-not-found: "&cPlayer '{player}' not found!"
  player-offline: "&cPlayer '{player}' is offline!"
  
  reload:
    success: "&aConfiguration and messages reloaded successfully!"
    
  check:
    header: "&7====== &bTemp Group Info &7======"
    player: "&7Player: &f{player}"
    current-group: "&7Current Group: &f{group}"
    temp-group: "&7Temp Group: &f{tempgroup}"
    expires: "&7Expires: &f{expires}"
    no-temp-group: "&7Temp Group: &cNone"
    footer: "&7========================="
    
  help:
    header: "&7====== &bLuckPerms-More-Placeholders Help &7======"
    commands:
      - "&7/lpplaceholder reload &8- &fReload configuration"
      - "&7/lpplaceholder check [player] &8- &fCheck temp group info"
      - "&7/lpplaceholder help &8- &fShow this help"
      - "&7/lpplaceholder info &8- &fShow plugin information"
    footer: "&7============================================="
    
  info:
    header: "&7====== &bPlugin Information &7======"
    name: "&7Name: &f{name}"
    version: "&7Version: &f{version}"
    author: "&7Author: &f{author}"
    description: "&7Description: &f{description}"
    footer: "&7============================="

placeholders:
  no-temp-group: "No temp group"
  expired: "Expired"
  permanent: "Permanent"
  error: "Error"
        """.trimIndent()

        messageFile.writeText(defaultMessages)
    }

    fun getMessage(path: String, vararg replacements: Pair<String, String>): String {
        var message = messages.getString(path, "Message not found: $path")!!

        // Apply replacements
        replacements.forEach { (placeholder, value) ->
            message = message.replace("{$placeholder}", value)
        }

        return ChatColor.translateAlternateColorCodes('&', message)
    }

    fun getMessageList(path: String): List<String> {
        return messages.getStringList(path).map {
            ChatColor.translateAlternateColorCodes('&', it)
        }
    }

    fun getPrefix(): String {
        return getMessage("prefix")
    }
}