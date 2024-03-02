package ru.prodcontest.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
class DateUtil {

    companion object {

        private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

        fun format(date: Date): String {
            return formatter.format(date)
                .replace("(\\d\\d)(\\d\\d)$".toRegex(), "$1:$2")
        }

    }

}