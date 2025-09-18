package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
    authorAvatar = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))

            repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
                override fun onSuccess(result: List<Post>) {
                    _data.postValue(
                        FeedModel(posts = result, empty = result.isEmpty())
                    )
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
    }

    fun save() {
        edited.value?.let { post ->
            repository.saveAsync(post, object : PostRepository.Callback<Post> {
                override fun onSuccess(result: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        repository.likeByIdAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(result: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map { if (it.id == result.id) result else it }
                    )
                )
                _postCreated.postValue(Unit)
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun unlikeById(id: Long) {
        repository.unlikeByIdAsync(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(post: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map { if (it.id == post.id) post else it }
                    )
                )
                _postCreated.postValue(Unit) // Если необходимо
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun removeById(id: Long) {
        repository.removeByIdAsync(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(unit: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty().filter { it.id != id })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value)
            }
        })
    }
}
