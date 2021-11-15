/**
 * @ Author: 2205 Team (Food Village)
 * @ Create Time: 2021-11-11 20:41:02
 * @ Description: This file contains a UserViewModel
 */

package com.example.foodvillage2205.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(val userRepository: UserRepository) : ViewModel() {
    val userStateFlow = MutableStateFlow<Resource<*>?>(null)

    init {
        viewModelScope.launch {
            userRepository.getUsers().collect {
                userStateFlow.value = it
            }
        }
    }

    fun getUserById(id: String, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        userRepository.getUserById(id, onResponse)
    }

    fun createUser(user: User, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        userRepository.createUser(user, onResponse)
    }

    fun updateUser(user: User, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        userRepository.updateUser(user, onResponse)
    }

    fun deleteUser(user: User, onResponse: (Resource<*>) -> Unit) = viewModelScope.launch {
        userRepository.deleteUser(user, onResponse)
    }
}

class UserViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalStateException()
    }
}