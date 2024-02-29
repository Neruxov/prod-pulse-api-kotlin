package ru.prodcontest.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.prodcontest.data.country.enums.Region
import ru.prodcontest.service.CountryService


@RestController
@RequestMapping("/api/countries")
class CountryController(
    val countryService: CountryService
) {

    @GetMapping
    fun getAllCountries(@RequestParam region: List<Region>?) = countryService.getAllCountries(region)

    @GetMapping("/{alpha2}")
    fun getCountryById(@PathVariable alpha2: String) = countryService.getCountryByAlpha2(alpha2)

}