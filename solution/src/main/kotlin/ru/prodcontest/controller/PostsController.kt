package ru.prodcontest.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.prodcontest.data.post.enums.ReactionType
import ru.prodcontest.data.post.request.CreatePostRequest
import ru.prodcontest.data.user.model.User
import ru.prodcontest.service.PostsService
import java.util.UUID

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RestController
@RequestMapping("/api/posts")
class PostsController(
    val postsService: PostsService
) {

    @PostMapping("/new")
    fun createPost(
        @AuthenticationPrincipal user: User,
        @RequestBody body: CreatePostRequest
    ) = postsService.createPost(user, body)

    @GetMapping("/{id}")
    fun getPost(
        @AuthenticationPrincipal user: User,
        @PathVariable id: UUID
    ) = postsService.getPost(user, id)

    @PostMapping("/{id}/like")
    fun likePost(
        @AuthenticationPrincipal user: User,
        @PathVariable id: UUID
    ) = postsService.reactToPost(user, id, ReactionType.LIKE)

    @PostMapping("/{id}/dislike")
    fun dislikePost(
        @AuthenticationPrincipal user: User,
        @PathVariable id: UUID
    ) = postsService.reactToPost(user, id, ReactionType.DISLIKE)

    @GetMapping("/feed/my")
    fun getMyFeed(
        @AuthenticationPrincipal user: User,
        @RequestParam limit: Int = 5,
        @RequestParam offset: Int = 0
    ) = postsService.getMyFeed(user, limit, offset)

    @GetMapping("/feed/{login}")
    fun getUserFeed(
        @AuthenticationPrincipal user: User,
        @PathVariable login: String,
        @RequestParam limit: Int = 5,
        @RequestParam offset: Int = 0
    ) = postsService.getUserFeed(user, login, limit, offset)

}