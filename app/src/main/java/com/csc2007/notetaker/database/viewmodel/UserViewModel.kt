package com.csc2007.notetaker.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.MessageDigest

fun hashString(password: String): ByteArray {
    val data = password.toByteArray()
    val sha256 = MessageDigest.getInstance("SHA-256")
    return sha256.digest(data)
}

class UserViewModel(private val repository: UsersRepository) : ViewModel() {

    private val _loggedIn = MutableStateFlow<Boolean?>(null)
    val loggedIn: StateFlow<Boolean?> = _loggedIn

    val allUsers = repository.allUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isEmpty() or password.isEmpty()) {
                _loggedIn.value = false
            } else {
                val user = repository.login(email, hashString(password))
                _loggedIn.value = user != null && user.password.contentEquals(hashString(password))
            }
        }
    }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {
            val user = User(email = email, userName = username, password = hashString(password))
            repository.insert(user)
        }
    }
}

class UserViewModelFactory(private val repository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}