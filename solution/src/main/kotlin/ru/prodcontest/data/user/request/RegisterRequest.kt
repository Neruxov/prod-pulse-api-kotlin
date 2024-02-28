package ru.prodcontest.data.user.request

data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String,
    val countryCode: String,
    val isPublic: Boolean,
    val phone: String?,
    val image: String?
)
