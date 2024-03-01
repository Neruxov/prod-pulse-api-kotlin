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
import ru.prodcontest.data.user.repo.UserRepository
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
    val friendRepository: FriendRepository,
    val userRepository: UserRepository
) {

    fun createPost(user: User, body: CreatePostRequest): Any {
        if (body.content.length > 1000 || body.content.isEmpty())
            throw StatusCodeException(400, "Post length must be less than 1000 and not be empty")

        if (body.tags.any { it.length > 20 || it.isEmpty() })
            throw StatusCodeException(400, "Tag length must be less than 20 and not be empty")

        var post = Post(
            UUID.randomUUID(),
            body.content,
            mutableListOf(),
            user
        )

        post = postRepository.save(post)
        body.tags.forEach { tagRepository.save(Tag(0, it, post.id)) }

        return post.toMap().toMutableMap().apply {
            put("tags", body.tags.map { it })
        }
    }

    fun getPost(user: User, id: UUID): Any {
        val post = postRepository.findById(id)
            .orElseThrow { StatusCodeException(404, "Post not found") }

        if (post.user != user && !post.user.isPublic && !friendRepository.existsByUserLoginAndFriendLogin(post.user.login, user.login))
            throw StatusCodeException(404, "This user's profile is private")

        return post.toMap()
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
            reactionRepository.save(Reaction(0, post.id, user.id, reactionType))
            post = Post(
                post.id,
                post.content,
                post.tags,
                post.user,
                post.createdAt,
                post.reactions
            )
        }

        return post.toMap()
    }

    fun getMyFeed(user: User, limit: Int, offset: Int): Any {
        return getFeedForUser(user, limit, offset).map { it.toMap() }
    }

    fun getUserFeed(user: User, login: String, limit: Int, offset: Int): Any {
        if (user.login == login) return getMyFeed(user, limit, offset)

        val targetUser = userRepository.findByLogin(login)
            .orElseThrow { StatusCodeException(404, "User not found") }

        if (!targetUser.isPublic && !friendRepository.existsByUserLoginAndFriendLogin(login, user.login))
            throw StatusCodeException(404, "User has a private profile")

        return getFeedForUser(targetUser, limit, offset).map { it.toMap() }
    }

    private fun getFeedForUser(user: User, limit: Int, offset: Int): List<Post> {
        val posts = postRepository.findByUserLoginOrderByCreatedAtDesc(user.login)
        if (offset >= posts.size)
            return emptyList()

        if (limit < 0 || limit > 50)
            throw StatusCodeException(400, "Limit must be between 0 and 50")

        if (offset < 0)
            throw StatusCodeException(400, "Offset must be greater or equal to 0")

        return posts.subList(offset, (offset + limit).coerceAtMost(posts.size))
    }

}