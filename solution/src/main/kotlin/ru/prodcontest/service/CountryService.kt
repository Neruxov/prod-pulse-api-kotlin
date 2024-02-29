package ru.prodcontest.service

import org.springframework.stereotype.Service
import ru.prodcontest.data.country.enums.Region
import ru.prodcontest.data.country.repo.CountryRepository
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class CountryService(
    val countryRepository: CountryRepository,
) {

    fun getAllCountries(region: List<Region>?): Any {
        return if (region != null) countryRepository.findByRegionInIgnoreCaseOrderByAlpha2(region.map { it.name })
        else countryRepository.findAllByOrderByAlpha2()
    }

    fun getCountryByAlpha2(alpha2: String): Any {
        if (alpha2.length != 2)
            throw StatusCodeException(404, "Country not found")

        return countryRepository.findByAlpha2OrderByAlpha2(alpha2).orElseThrow { IllegalArgumentException("Country not found") }
    }

}