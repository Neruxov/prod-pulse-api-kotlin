package ru.prodcontest.util.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
class BooleanDeserializer : JsonDeserializer<Boolean>() {

    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Boolean {
        val jsonToken = p0?.currentToken
        return when (jsonToken) {
            JsonToken.VALUE_TRUE -> true
            JsonToken.VALUE_FALSE -> false
            else -> throw IllegalArgumentException("Invalid body")
        }
    }

}