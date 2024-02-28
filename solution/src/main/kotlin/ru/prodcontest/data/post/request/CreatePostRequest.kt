package ru.prodcontest.data.post.request

data class CreatePostRequest(
    val content: String,
    val tags: List<String>
)
