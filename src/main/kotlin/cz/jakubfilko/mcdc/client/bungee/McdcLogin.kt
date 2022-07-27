package cz.jakubfilko.mcdc.client.bungee

import com.google.common.io.ByteStreams
import cz.jakubfilko.mcdc.client.bungee.configuration.McdcConfiguration
import cz.jakubfilko.mcdc.client.bungee.listener.LoginListener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class McdcLogin : Plugin() {


    override fun onEnable() {
        val config = getConfiguration()
        val host = config.getString(HOST_CONFIG)
        val apiKey = config.getString(APIKEY_CONFIG)
        val kickMessage = config.getString(MESSAGES_KICK_CONFIG)
        val errors = emptyList<String>().toMutableList()
        when (null) {
            host -> errors.add(HOST_CONFIG)
            apiKey -> errors.add(APIKEY_CONFIG)
            kickMessage -> errors.add(MESSAGES_KICK_CONFIG)
        }

        if (errors.isNotEmpty()) {
            val errorMessage = "Missing configuration: ${errors.joinToString(", ")}"
            throw MissingConfiguration(errorMessage)
        }

        val mcdcConfiguration = McdcConfiguration(host!!, apiKey!!, kickMessage!!)
        val loginListener = LoginListener(mcdcConfiguration)

        proxy.pluginManager.registerListener(this, loginListener)
    }

    private fun getConfiguration(): Configuration {
        return try {
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))
        } catch (ex: IOException) {
            saveDefaultConfiguration()
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))
        } ?: throw RuntimeException("Cannot load configuration")
    }

    private fun saveDefaultConfiguration() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            try {
                configFile.createNewFile()
                getResourceAsStream("config.yml").use { `is` ->
                    FileOutputStream(configFile).use { os -> ByteStreams.copy(`is`, os) }
                }
            } catch (e: IOException) {
                throw RuntimeException("Unable to create configuration file", e)
            }
        }
    }

    companion object {
        private const val HOST_CONFIG = "webservice.host"
        private const val APIKEY_CONFIG = "webservice.apiKey"
        private const val MESSAGES_KICK_CONFIG = "messages.kick"
    }
}