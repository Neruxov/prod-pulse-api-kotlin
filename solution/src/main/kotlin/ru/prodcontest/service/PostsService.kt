package ru.prodcontest.service

import org.springframework.stereotype.Service
import ru.prodcontest.data.friend.repo.FriendRepository
import ru.prodcontest.data.post.enums.ReactionType
import ru.prodcontest.data.post.model.Post
import ru.prodcontest.data.post.model.Reaction
import ru.prodcontest.data.post.model.Tag
import ru.prodcontest.data.post.repo.PostRepository
import ru.prodcontest.data.post.repo.ReactionRepository
import ru.prodcontest.data.post.repo.TagRepository
import ru.prodcontest.data.post.request.CreatePostRequest
import ru.prodcontest.data.user.model.User
import ru.prodcontest.exception.type.StatusCodeException
import ru.prodcontest.util.DateFormatter
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class PostsService(
    val postRepository: PostRepository,
    val tagRepository: TagRepository,
    val reactionRepository: ReactionRepository,
    val friendRepository: FriendRepository
) {

    fun createPost(user: User, body: CreatePostRequest): Any {
        if (body.content.length > 1000)
            throw StatusCodeException(400, "Post length must be less than 1000")

        if (body.tags.any { it.length > 20 })
            throw StatusCodeException(400, "Tag length must be less than 20")

        var post = Post(
            UUID.randomUUID(),
            body.content,
            mutableListOf(),
            user
        )

        post = postRepository.save(post)
        body.tags.forEach { tagRepository.save(Tag(0, it, post)) }

        return mapOf(
            "id" to post.id,
            "content" to post.content,
            "author" to post.user.login,
            "tags" to post.tags.map { it.name },
            "createdAt" to DateFormatter.format(post.createdAt),
            "likesCount" to 0,
            "dislikesCount" to 0
        )
    }

    fun getPost(user: User, id: UUID): Any {
        val post = postRepository.findById(id)
            .orElseThrow { StatusCodeException(404, "Post not found") }

        println(post.user.login + " " + user.login)
        if (post.user != user && !post.user.isPublic && !friendRepository.existsByUserLoginAndFriendLogin(post.user.login, user.login))
            throw StatusCodeException(404, "This user's profile is private")

        return mapOf(
            "id" to post.id,
            "content" to post.content,
            "author" to post.user.login,
            "tags" to post.tags.map { it.name },
            "createdAt" to DateFormatter.format(post.createdAt),
            "likesCount" to post.reactions.count { it.type == ReactionType.LIKE },
            "dislikesCount" to post.reactions.count { it.type == ReactionType.DISLIKE }
        )
    }

    fun reactToPost(user: User, id: UUID, reactionType: ReactionType): Any {
        var post = postRepository.findById(id)
            .orElseThrow { StatusCodeException(404, "Post not found") }

        if (post.user != user && !post.user.isPublic && !friendRepository.existsByUserLoginAndFriendLogin(post.user.login, user.login))
            throw StatusCodeException(404, "This user's profile is private")

        val reaction = reactionRepository.findByPostIdAndUserId(id, user.id)
        if (reaction.isPresent && reaction.get().type != reactionType) {
            val r = reaction.get()
            r.type = reactionType
            reactionRepository.save(r)
        } else if (reaction.isEmpty) {
            reactionRepository.save(Reaction(0, post, user, reactionType))
            post = Post(
                post.id,
                post.content,
                post.tags,
                post.user,
                post.createdAt,
                post.reactions
            )
        }

        return mapOf(
            "id" to post.id,
            "content" to post.content,
            "author" to post.user.login,
            "tags" to post.tags.map { it.name },
            "createdAt" to DateFormatter.format(post.createdAt),
            "likesCount" to post.reactions.count { it.type == ReactionType.LIKE },
            "dislikesCount" to post.reactions.count { it.type == ReactionType.DISLIKE }
        )
    }

    fun getMyFeed(user: User, limit: Int, offset: Int): Any {
        val posts = postRepository.findByUserLoginOrderByCreatedAtDesc(user.login)
        if (offset >= posts.size)
            return emptyList<Map<String, Any>>()

        val feed = posts.subList(offset, offset + limit.coerceAtMost(posts.size))
        return feed.map { mapOf(
            "id" to it.id,
            "content" to it.content,
            "author" to it.user.login,
            "tags" to it.tags.map { it.name },
            "createdAt" to DateFormatter.format(it.createdAt),
            "likesCount" to it.reactions.count { it.type == ReactionType.LIKE },
            "dislikesCount" to it.reactions.count { it.type == ReactionType.DISLIKE }
        ) }
    }

    fun getUserFeed(user: User, login: String, limit: Int, offset: Int): Any {
        if (user.login == login) return getMyFeed(user, limit, offset)

        val targetUser = friendRepository.findByUserLoginAndFriendLogin(login, user.login)
            .orElseThrow { StatusCodeException(404, "User not found or has a private profile") }
            .user

        val posts = postRepository.findByUserLoginOrderByCreatedAtDesc(targetUser.login)
        if (offset >= posts.size)
            return emptyList<Map<String, Any>>()

        val feed = posts.subList(offset, offset + limit.coerceAtMost(posts.size))
        return feed.map { mapOf(
            "id" to it.id,
            "content" to it.content,
            "author" to it.user.login,
            "tags" to it.tags.map { it.name },
            "createdAt" to DateFormatter.format(it.createdAt),
            "likesCount" to it.reactions.count { it.type == ReactionType.LIKE },
            "dislikesCount" to it.reactions.count { it.type == ReactionType.DISLIKE }
        ) }
    }

}