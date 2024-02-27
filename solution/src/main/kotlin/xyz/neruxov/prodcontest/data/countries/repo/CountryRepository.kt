package xyz.neruxov.prodcontest.data.countries.repo

import org.springframework.data.jpa.repository.JpaRepository
import xyz.neruxov.prodcontest.data.countries.model.Country

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface CountryRepository : JpaRepository<Country, Long> {

    fun findByAlpha2(alpha2: String): Country

}