package com.quang1807.luckpermsplaceholders.placeholders

import com.quang1807.luckpermsplaceholders.LuckPermsPlaceholders
import com.quang1807.luckpermsplaceholders.utils.TimeUtils
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.luckperms.api.model.user.User
import net.luckperms.api.node.types.InheritanceNode
import org.bukkit.OfflinePlayer
import java.time.Instant
import java.util.*

class LuckPermsExpansion(private val plugin: LuckPermsPlaceholders) : PlaceholderExpansion() {

    override fun getIdentifier(): String = "luckperms"

    override fun getAuthor(): String = "quang1807"

    override fun getVersion(): String = plugin.description.version

    override fun persist(): Boolean = true

    override fun canRegister(): Boolean = true

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        if (player == null) return null

        val user = plugin.luckPerms.userManager.getUser(player.uniqueId) ?: return null

        return when {
            params.startsWith("cooldown_") -> {
                val remaining = params.removePrefix("cooldown_")
                when (remaining) {
                    "current" -> getCurrentTempGroupCooldown(user)
                    else -> getSpecificTempGroupCooldown(user, remaining)
                }
            }
            else -> null
        }
    }

    private fun getCurrentTempGroupCooldown(user: User): String {
        val currentTempGroup = getCurrentTempGroup(user) ?: return plugin.messageManager.getMessage("placeholders.no-temp-group")

        val expiry = getTempGroupExpiry(user, currentTempGroup.groupName) ?: return plugin.messageManager.getMessage("placeholders.permanent")

        val now = Instant.now()
        if (expiry.isBefore(now)) {
            return plugin.messageManager.getMessage("placeholders.expired")
        }

        return TimeUtils.formatTimeUntil(expiry, plugin.configManager.dateFormat)
    }

    private fun getSpecificTempGroupCooldown(user: User, groupName: String): String {
        val expiry = getTempGroupExpiry(user, groupName) ?: return plugin.messageManager.getMessage("placeholders.no-temp-group")

        val now = Instant.now()
        if (expiry.isBefore(now)) {
            return plugin.messageManager.getMessage("placeholders.expired")
        }

        return TimeUtils.formatTimeUntil(expiry, plugin.configManager.dateFormat)
    }

    private fun getCurrentTempGroup(user: User): InheritanceNode? {
        return user.nodes
            .filter { it is InheritanceNode && it.hasExpiry() }
            .map { it as InheritanceNode }
            .filter { it.expiry?.isAfter(Instant.now()) == true }
            .maxByOrNull { it.expiry!! }
    }

    private fun getTempGroupExpiry(user: User, groupName: String): Instant? {
        return user.nodes
            .filter { it is InheritanceNode && it.groupName.equals(groupName, ignoreCase = true) && it.hasExpiry() }
            .map { it as InheritanceNode }
            .firstOrNull()
            ?.expiry
    }
}