package ru.prodcontest.util.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.prodcontest.data.country.enums.Region

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Component
class RegionConverter : Converter<String, Region> {

    override fun convert(source: String): Region? {
        val result = Region.entries.find { it.name.equals(source, ignoreCase = true) }
        if (result == null)
            throw IllegalArgumentException("Region not found")

        return result
    }

}