package ru.prodcontest.data.user.request

data class PasswordUpdateRequest(
    val oldPassword: String,
    val newPassword: String
)
