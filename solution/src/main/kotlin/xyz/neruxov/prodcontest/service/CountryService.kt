package xyz.neruxov.prodcontest.service

import org.springframework.stereotype.Service
import xyz.neruxov.prodcontest.data.countries.repo.CountryRepository

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class CountryService(
    val countryRepository: CountryRepository,
) {

    fun getAllCountries(region: String?): Any {
        val countries = countryRepository.findAll()
        if (region != null) {
            return countries.filter { it.region == region }
        }

        return countries
    }

    fun getCountryByAlpha2(alpha2: String): Any {
        return countryRepository.findByAlpha2(alpha2)
    }

}