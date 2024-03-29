package ru.prodcontest.data.country.repo

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.country.model.Country
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface CountryRepository : JpaRepository<Country, Long> {

    fun findByAlpha2IgnoreCase(alpha2: String): Optional<Country>

    fun findByRegionInIgnoreCaseOrderByAlpha2(region: List<String>): List<Country>

    fun findAllByOrderByAlpha2(): List<Country>

    fun existsByAlpha2(alpha2: String): Boolean

}