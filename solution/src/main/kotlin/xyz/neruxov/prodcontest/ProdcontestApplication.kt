package xyz.neruxov.prodcontest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource


@SpringBootApplication
class ProdcontestApplication

fun main(args: Array<String>) {
    runApplication<ProdcontestApplication>(*args)
}

@Configuration
class CustomDataSourceConfig(
    private val environment: Environment,
) {

    @Bean
    fun getDataSource(): DataSource {
        var url = environment.getProperty("spring.datasource.url") ?: throw Exception("Application property not found: spring.datasource.url")
        val result: String

        try {
            if (url.startsWith("jdbc:postgresql://")) { // JDBC URL
                result = url
            } else if (url.startsWith("postgres://")) { // URL
                url = url.replace("postgres://", "")

                val parts = url.split("@")
                val credentials = parts[0].split(":")

                val user = credentials[0]
                val password = credentials[1]

                result = "jdbc:postgresql://" + parts[1] + "?user=" + user + "&password=" + password
            } else { // DSN
                throw NotImplementedError("DSN is not supported (yet!)")
            }
        } catch (e: Exception) {
            throw Exception("Failed to parse database URL: $url", e)
        }

        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.url(result)
        return dataSourceBuilder.build()
    }

}
