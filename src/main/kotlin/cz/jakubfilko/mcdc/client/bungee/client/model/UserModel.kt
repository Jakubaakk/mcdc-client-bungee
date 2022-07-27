package cz.jakubfilko.mcdc.client.bungee.client.model

import java.util.UUID

data class UserModel(
    val id: UUID? = null,
    val minecraftNickname: String? = null,
    val discordId: String? = null,
    val verified: Boolean? = null,
    val subscribed: Boolean? = null
)