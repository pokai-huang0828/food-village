package com.example.foodvillage2205.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.responses.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostsViewModel(private val postRepository: PostRepository) : ViewModel() {

    val postsStateFlow = MutableStateFlow<Resource<*>?>(null)

    init {
        viewModelScope.launch {
            postRepository.getPosts().collect {
                postsStateFlow.value = it
            }
        }
    }

    suspend fun getPostById(id: String) = postRepository.getPostById(id)

    fun createPost(post: Post, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        postRepository.createPost(post, onResponse)
    }

    fun updatePost(post: Post, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        postRepository.updatePost(post, onResponse)
    }

    fun deletePost(post: Post, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        postRepository.deletePost(post, onResponse)
    }

}

class PostViewModelFactory(private val postRepository: PostRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(postRepository) as T
        }

        throw IllegalStateException()
    }
}