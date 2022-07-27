package cz.jakubfilko.mcdc.client.bungee

open class McdcException(message: String?) : Exception(message)

class PlayerNotFoundException(message: String?) : McdcException(message)

class MissingConfiguration(message: String?) : McdcException(message)