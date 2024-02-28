package ru.prodcontest.util

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
class PasswordUtil {

    companion object {

        fun meetsRequirements(password: String): Boolean {
            return password.length in 6..100 &&
                   password.contains(Regex("[a-z]")) &&
                   password.contains(Regex("[A-Z]")) &&
                   password.contains(Regex("[0-9]"))
        }

    }

}