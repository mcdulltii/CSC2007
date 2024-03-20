package com.csc2007.notetaker.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.User
import com.csc2007.notetaker.database.repository.UsersRepository
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.RandomSecretGenerator
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

    private val _loggedInUserSecret = MutableStateFlow<ByteArray?>(null)
    var loggedInUserSecret: StateFlow<ByteArray?> = _loggedInUserSecret

    private val _registeredUserSecret = MutableStateFlow<ByteArray?>(null)
    var registeredUserSecret: StateFlow<ByteArray?> = _registeredUserSecret

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
                    _loggedInUserSecret.value = user.secret
                }
            }
        }
    }

    fun logout() {
        _loggedIn.value = null
        _loggedInUser.value = null
        _loggedInUserEmail.value = ""
        _loggedInUserUsername.value = ""
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

    fun updatePassword(password: String, id: Int) {
        viewModelScope.launch {
            repository.updatePassword(password = hashString(password), id = id)
        }
    }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {

            val randomSecretGenerator = RandomSecretGenerator()
            val secret: ByteArray = randomSecretGenerator.createRandomSecret(HmacAlgorithm.SHA1)

            _registeredUserSecret.value = secret

            val user = User(email = email, userName = username, password = hashString(password), secret = secret)
            repository.insert(user)

            val insertedUser = repository.getLastUser()
            repository.createNewAvatar(insertedUser.id)
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