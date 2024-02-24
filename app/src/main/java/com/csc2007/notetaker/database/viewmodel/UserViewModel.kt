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

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser

    private val _loggedInUserEmail = MutableStateFlow<String>("")
    var loggedInUserEmail: StateFlow<String> = _loggedInUserEmail

    private val _loggedInUserUsername = MutableStateFlow<String>("")
    var loggedInUserUsername: StateFlow<String> = _loggedInUserUsername

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
                if (_loggedIn.value == true) {
                    _loggedInUser.value = user
                    _loggedInUserEmail.value = user.email
                    _loggedInUserUsername.value = user.userName
                }
            }
        }
    }


    private fun getUserById(id: Int) {
        viewModelScope.launch {
            val user = repository.getUserById(id)
            _loggedInUser.value = user
            _loggedInUserEmail.value = user.email
            _loggedInUserUsername.value = user.userName
        }
    }

    fun updateEmailAndUserName(email: String, username: String, id: Int) {
        viewModelScope.launch {
            repository.updateEmailAndUserName(email, username, id)
            val user = repository.getUserById(id)
            _loggedInUser.value = user
            _loggedInUserEmail.value = user.email
            _loggedInUserUsername.value = user.userName
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