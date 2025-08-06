package com.quang1807.luckpermsplaceholders.commands

import com.quang1807.luckpermsplaceholders.LuckPermsPlaceholders
import com.quang1807.luckpermsplaceholders.utils.TimeUtils
import net.luckperms.api.model.user.User
import net.luckperms.api.node.types.InheritanceNode
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.time.Instant

class LPPlaceholderCommand(private val plugin: LuckPermsPlaceholders) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("luckpermsplaceholders.admin")) {
            sender.sendMessage(plugin.messageManager.getPrefix() + plugin.messageManager.getMessage("commands.no-permission"))
            return true
        }

        when (args.getOrNull(0)?.lowercase()) {
            "reload" -> handleReload(sender)
            "check" -> handleCheck(sender, args)
            "help" -> handleHelp(sender)
            "info" -> handleInfo(sender)
            else -> handleHelp(sender)
        }

        return true
    }

    private fun handleReload(sender: CommandSender) {
        plugin.reload()
        sender.sendMessage(plugin.messageManager.getPrefix() + plugin.messageManager.getMessage("commands.reload.success"))
    }

    private fun handleCheck(sender: CommandSender, args: Array<out String>) {
        val targetName = args.getOrNull(1) ?: if (sender is Player) sender.name else {
            sender.sendMessage(plugin.messageManager.getPrefix() + "§cYou must specify a player name from console!")
            return
        }

        val target = Bukkit.getOfflinePlayer(targetName)
        if (!target.hasPlayedBefore() && !target.isOnline) {
            sender.sendMessage(plugin.messageManager.getPrefix() +
                    plugin.messageManager.getMessage("commands.player-not-found", "player" to (targetName ?: "")))
            return
        }

        val user = plugin.luckPerms.userManager.getUser(target.uniqueId)
        if (user == null) {
            sender.sendMessage(plugin.messageManager.getPrefix() +
                    plugin.messageManager.getMessage("commands.player-offline", "player" to (targetName ?: "")))
            return
        }

        displayTempGroupInfo(sender, user, targetName)
    }

    private fun displayTempGroupInfo(sender: CommandSender, user: User, playerName: String) {
        val primaryGroup = user.primaryGroup
        val currentTempGroup = getCurrentTempGroup(user)

        sender.sendMessage(plugin.messageManager.getMessage("commands.check.header"))
        sender.sendMessage(plugin.messageManager.getMessage("commands.check.player", "player" to (playerName ?: "")))
        sender.sendMessage(plugin.messageManager.getMessage("commands.check.current-group", "group" to (primaryGroup ?: "")))

        if (currentTempGroup != null) {
            val expiry = currentTempGroup.expiry!!
            val expiryText = if (TimeUtils.isExpired(expiry)) {
                plugin.messageManager.getMessage("placeholders.expired")
            } else {
                TimeUtils.formatExpiryDate(expiry, plugin.configManager.dateFormat)
            }

            sender.sendMessage(plugin.messageManager.getMessage("commands.check.temp-group", "tempgroup" to (currentTempGroup.groupName ?: "")))
            sender.sendMessage(plugin.messageManager.getMessage("commands.check.expires", "expires" to (expiryText ?: "")))
        } else {
            sender.sendMessage(plugin.messageManager.getMessage("commands.check.no-temp-group"))
        }

        sender.sendMessage(plugin.messageManager.getMessage("commands.check.footer"))
    }

    private fun getCurrentTempGroup(user: User): InheritanceNode? {
        return user.nodes
            .filter { it is InheritanceNode && it.hasExpiry() }
            .map { it as InheritanceNode }
            .filter { it.expiry?.isAfter(Instant.now()) == true }
            .maxByOrNull { it.expiry!! }
    }

    private fun handleHelp(sender: CommandSender) {
        sender.sendMessage(plugin.messageManager.getMessage("commands.help.header"))
        plugin.messageManager.getMessageList("commands.help.commands").forEach {
            sender.sendMessage(it)
        }
        sender.sendMessage(plugin.messageManager.getMessage("commands.help.footer"))
    }

    private fun handleInfo(sender: CommandSender) {
        val desc = plugin.description
        sender.sendMessage(plugin.messageManager.getMessage("commands.info.header"))
        sender.sendMessage(plugin.messageManager.getMessage("commands.info.name", "name" to (desc.name ?: "")))
        sender.sendMessage(plugin.messageManager.getMessage("commands.info.version", "version" to (desc.version ?: "")))
        sender.sendMessage(plugin.messageManager.getMessage("commands.info.author", "author" to desc.authors.joinToString(", ")))
        sender.sendMessage(plugin.messageManager.getMessage("commands.info.description", "description" to (desc.description ?: "")))
        sender.sendMessage(plugin.messageManager.getMessage("commands.info.footer"))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        if (!sender.hasPermission("luckpermsplaceholders.admin")) {
            return emptyList()
        }

        return when (args.size) {
            1 -> listOf("reload", "check", "help", "info").filter {
                it.startsWith(args[0], ignoreCase = true)
            }
            2 -> if (args[0].equals("check", ignoreCase = true)) {
                Bukkit.getOnlinePlayers().map { it.name }.filter {
                    it.startsWith(args[1], ignoreCase = true)
                }
            } else emptyList()
            else -> emptyList()
        }
    }
}
