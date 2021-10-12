package com.example.foodvillage2205.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodvillage2205.model.entities.User
import com.example.foodvillage2205.model.repositories.UserRepository
import com.example.foodvillage2205.model.responses.UsersResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(val userRepository: UserRepository) : ViewModel() {
    val userStateFlow = MutableStateFlow<UsersResponse?>(null)

    init {
        viewModelScope.launch {
            userRepository.getUsers().collect {
                userStateFlow.value = it
            }
        }
    }

    fun getUsers() = userRepository.getUsers()

    fun getUserById(id: String) = viewModelScope.launch {
        userRepository.getUserById(id)
    }

    fun createUser(user: User) = userRepository.createUser(user)

    fun updateUser(user: User) = userRepository.updateUser(user)

    fun deleteUser(user: User) = userRepository.deleteUser(user)
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