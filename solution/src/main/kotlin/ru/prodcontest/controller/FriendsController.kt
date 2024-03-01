package ru.prodcontest.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.prodcontest.data.friend.request.FriendRequest
import ru.prodcontest.data.user.model.User
import ru.prodcontest.service.FriendService

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RestController
@RequestMapping("/api/friends")
class FriendsController(
    val friendService: FriendService
) {

    @PostMapping("/add")
    fun addFriend(
        @AuthenticationPrincipal user: User,
        @RequestBody body: FriendRequest
    ) = friendService.addFriend(user, body)

    @PostMapping("/remove")
    fun removeFriend(
        @AuthenticationPrincipal user: User,
        @RequestBody body: FriendRequest
    ) = friendService.removeFriend(user, body)

    @GetMapping
    fun getFriends(
        @AuthenticationPrincipal user: User,
        @RequestParam limit: Int = 5,
        @RequestParam offset: Int = 0
    ) = friendService.getFriends(user, limit, offset)

}