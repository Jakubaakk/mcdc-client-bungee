package cz.jakubfilko.mcdc.client.bungee.listener

import cz.jakubfilko.mcdc.client.bungee.McdcException
import cz.jakubfilko.mcdc.client.bungee.PlayerNotFoundException
import cz.jakubfilko.mcdc.client.bungee.client.McdcWsClient
import cz.jakubfilko.mcdc.client.bungee.configuration.McdcConfiguration
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler


class LoginListener(
    private val configuration: McdcConfiguration
) : Listener {

    private val client = McdcWsClient(configuration.host, configuration.apiKey)

    @EventHandler
    fun normalLogin(event: ServerConnectEvent) {
        val nickname = event.player.displayName
        try {
            val user = client.getUser(nickname)
            if (user.verified == null || !user.verified) {
                event.player.disconnect(configuration.kickMessage)
            }
        } catch (ex: McdcException) {
            if (ex is PlayerNotFoundException) {
                val createdUser = client.createUser(nickname)
                if (createdUser.verified == null || !createdUser.verified) {
                    event.player.disconnect(configuration.kickMessage)
                }
                return
            }
            event.player.disconnect(configuration.kickMessage)
        }
    }
}