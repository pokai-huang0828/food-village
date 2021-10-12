package com.example.foodvillage2205.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodvillage2205.model.entities.Post
import com.example.foodvillage2205.model.repositories.PostRepository
import com.example.foodvillage2205.model.responses.PostsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostsViewModel(val postRepository: PostRepository) : ViewModel() {

    val postsStateFlow = MutableStateFlow<PostsResponse?>(null)

    init {
        viewModelScope.launch {
            postRepository.getPosts().collect {
                postsStateFlow.value = it
            }
        }
    }

    fun getPosts() = postRepository.getPosts()

    fun getPostById(id: String) = viewModelScope.launch {
        postRepository.getPostById(id)
    }

    fun createPost(post: Post) = postRepository.createPost(post)

    fun updatePost(post: Post) = postRepository.updatePost(post)

    fun deletePost(post: Post) = postRepository.deletePost(post)

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