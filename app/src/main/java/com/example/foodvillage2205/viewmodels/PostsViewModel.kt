package com.example.foodvillage2205.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.responses.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PostsViewModel(private val postRepository: PostRepository) : ViewModel() {

    val postsStateFlow = MutableStateFlow<Resource<*>?>(null)

    init {
        viewModelScope.launch {
            postRepository.getPosts().collect {
                postsStateFlow.value = it
            }
        }
    }

    fun getPostByUser(field: String, id: String, onResponse: (Resource<*>) -> Unit) =
        viewModelScope.launch {
            postRepository.getPostByUser(field, id, onResponse)
        }

    fun getPostById(postId: String, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        postRepository.getPostById(postId, onResponse)
    }

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
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            return PostsViewModel(postRepository) as T
        }
        throw IllegalStateException()
    }
}