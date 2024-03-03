package ru.prodcontest.util.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class StringDeserializer : JsonDeserializer<String>() {

    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): String {
        val jsonToken = p0?.currentToken
        return when (jsonToken) {
            JsonToken.VALUE_STRING -> p0.valueAsString
            else -> throw IllegalArgumentException("Invalid body")
        }
    }

}