package ru.prodcontest.data.user.request

data class UpdateRequest(
    val countryCode: String?,
    val isPublic: Boolean?,
    val phone: String?,
    val image: String?,
)
