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
        if (region != null && region.isEmpty())
            throw StatusCodeException(400, "Region list must not be empty")

        return if (region != null)
            countryRepository.findByRegionInIgnoreCaseOrderByAlpha2(region.map { it.name })
        else countryRepository.findAllByOrderByAlpha2()
    }

    fun getCountryByAlpha2(alpha2: String): Any {
        if (!alpha2.matches(Regex("[a-zA-Z]{2}")))
            throw StatusCodeException(400, "Alpha2 must match [a-zA-Z]{2}.")

        return countryRepository.findByAlpha2IgnoreCase(alpha2)
            .orElseThrow { StatusCodeException(404, "Country not found") }
    }

}