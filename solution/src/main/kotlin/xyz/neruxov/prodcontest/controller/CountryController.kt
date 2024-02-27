package xyz.neruxov.prodcontest.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import xyz.neruxov.prodcontest.service.CountryService


@RestController
@RequestMapping("/api/countries")
class CountryController(
    val countryService: CountryService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCountries(@RequestParam region: String?) = countryService.getAllCountries(region)

    @GetMapping("/{alpha2}")
    @ResponseStatus(HttpStatus.OK)
    fun getCountryById(@PathVariable alpha2: String) = countryService.getCountryByAlpha2(alpha2)

}