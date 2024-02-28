package ru.prodcontest.data.countries.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.countries.model.Country
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface CountryRepository : JpaRepository<Country, Long> {

    fun findByAlpha2(alpha2: String): Optional<Country>

    fun findByRegionIn(region: List<String>): List<Country>

}