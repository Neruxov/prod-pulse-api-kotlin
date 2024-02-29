package ru.prodcontest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.*
import javax.sql.DataSource

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Configuration
class JpaConfig {

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        entityManagerFactoryBean.dataSource = dataSource
        entityManagerFactoryBean.setPackagesToScan("ru.prodcontest.data")
        entityManagerFactoryBean.jpaVendorAdapter = HibernateJpaVendorAdapter()

        val properties = Properties()
        properties.setProperty("hibernate.hbm2ddl.auto", "update")
        properties.setProperty("hibernate.show_sql", "true")
//        properties.setProperty("hibernate.format_sql", "true")
        entityManagerFactoryBean.setJpaProperties(properties)

        return entityManagerFactoryBean
    }

}