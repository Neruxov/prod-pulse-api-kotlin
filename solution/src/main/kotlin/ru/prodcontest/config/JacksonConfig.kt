package ru.prodcontest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import ru.prodcontest.util.BooleanDeserializer

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.deserializerByType(Boolean::class.java, BooleanDeserializer())
        return builder
    }

}