package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import retrofit2.Callback
import ru.netology.nmedia.api.PostApiServiceHolder
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiServiceHolder.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ) {

                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun likeByIdAsync(
        id: Long,
        callback: PostRepository.Callback<Post>
    ) {
        PostApiServiceHolder.service.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: run {
                    callback.onError(
                        RuntimeException(
                            response.message() + response.code().toString()
                        )
                    )
                    return
                })
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun unlikeByIdAsync(
        id: Long,
        callback: PostRepository.Callback<Post>
    ) {
        PostApiServiceHolder.service.unlikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: run {
                    callback.onError(
                        RuntimeException(
                            response.message() + response.code().toString()
                        )
                    )
                    return
                })
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun saveAsync(
        post: Post,
        callback: PostRepository.Callback<Post>
    ) {
        PostApiServiceHolder.service.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: run {
                    callback.onError(
                        RuntimeException(
                            response.message() + response.code().toString()
                        )
                    )
                    return
                })
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }

    override fun removeByIdAsync(
        id: Long,
        callback: PostRepository.Callback<Unit>
    ) {
        PostApiServiceHolder.service.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: retrofit2.Call<Unit>, response: retrofit2.Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                callback.onSuccess(response.body() ?: run {
                    callback.onError(
                        RuntimeException(
                            response.message() + response.code().toString()
                        )
                    )
                    return
                })
            }

            override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                callback.onError(RuntimeException(t))
            }
        })
    }
}
