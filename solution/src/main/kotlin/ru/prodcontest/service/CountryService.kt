package ru.prodcontest.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.prodcontest.data.countries.repo.CountryRepository
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class CountryService(
    val countryRepository: CountryRepository,
) {

    fun getAllCountries(region: List<String>?): Any {
        return if (region != null) countryRepository.findByRegionIn(region)
        else countryRepository.findAll()
    }

    fun getCountryByAlpha2(alpha2: String): Any {
        if (alpha2.length != 2)
            throw StatusCodeException(404, "Country not found")

        return countryRepository.findByAlpha2(alpha2).orElseThrow { IllegalArgumentException("Country not found") }
    }

}