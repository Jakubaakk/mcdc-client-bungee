package cz.jakubfilko.mcdc.client.bungee.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import cz.jakubfilko.mcdc.client.bungee.McdcException
import cz.jakubfilko.mcdc.client.bungee.PlayerNotFoundException
import cz.jakubfilko.mcdc.client.bungee.client.model.UserModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class McdcWsClient(
    private val host: String,
    private val apiKey: String
) {

    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()

    @Throws(PlayerNotFoundException::class, McdcException::class)
    fun getUser(nickname: String): UserModel {
        val request = baseRequestBuilder("/api/v1/users/byMinecraftNickname/$nickname").build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            if (response.code == 404) {
                throw PlayerNotFoundException("Player: $nickname was not found")
            }
            throw McdcException("Error occurred when getting player: $nickname, status code: ${response.code}")
        }
        return mapper.readValue(response.body!!.string(), UserModel::class.java)
    }

    fun createUser(nickname: String): UserModel {
        val json = mapper.writeValueAsString(UserModel(minecraftNickname = nickname))
        val request = baseRequestBuilder("/api/v1/users").post(json.toRequestBody()).build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw RuntimeException("asdada")
        }
        return mapper.readValue(response.body!!.string(), UserModel::class.java)
    }

    private fun baseRequestBuilder(uri: String): Request.Builder {
        return Request.Builder()
            .url(host + uri)
            .header("api-key", apiKey)
            .header("Accept", "application/json")
    }

}