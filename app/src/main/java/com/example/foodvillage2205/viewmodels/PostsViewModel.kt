package com.example.foodvillage2205.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostsRepository
import com.example.foodvillage2205.model.responses.PostsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostsViewModel(val postsRepository: PostsRepository) : ViewModel() {

    val postsStateFlow = MutableStateFlow<PostsResponse?>(null)

    init {
        viewModelScope.launch {
            postsRepository.getPosts().collect {
                postsStateFlow.value = it
            }
        }
    }

    fun getPosts() = postsRepository.getPosts()

    fun createPost(post: Post) = postsRepository.createPost(post)

    fun updatePost(post: Post) = postsRepository.updatePost(post)

    fun deletePost(post: Post) = postsRepository.deletePost(post)

}

class PostViewModelFactory(private val postsRepository: PostsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(postsRepository) as T
        }

        throw IllegalStateException()
    }
}