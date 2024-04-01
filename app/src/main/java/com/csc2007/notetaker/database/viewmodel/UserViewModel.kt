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
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

fun hashString(password: String, secret: ByteArray): ByteArray {
    val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
    val spec = PBEKeySpec(password.toCharArray(), secret, 1000, 256)
    val key: SecretKey = factory.generateSecret(spec)
    val hash: ByteArray = key.encoded

    return hash
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

    private val _loggedInUserPoints = MutableStateFlow<Int>(0)
    var loggedInUserPoints: StateFlow<Int> = _loggedInUserPoints

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
            _loggedInUserSecret.value = null
            if (email.isEmpty() or password.isEmpty()) {
                _loggedIn.value = false
            } else {
                try {
                    _loggedInUserSecret.value = repository.getUserSecret(email)
                    val user = repository.login(email, hashString(password, loggedInUserSecret.value!!))
                    if (user != null) {
                        _loggedIn.value = true && user.password.contentEquals(hashString(password, loggedInUserSecret.value!!))
                        if (_loggedIn.value == true) {
                            _loggedInUser.value = user
                            _loggedInUserEmail.value = user.email
                            _loggedInUserUsername.value = user.userName
                            _loggedInUserSecret.value = user.secret
                            _loggedInUserPoints.value = user.points
                        }
                    }
                } catch (e: NullPointerException) {
                    _loggedIn.value = false
                }

            }
        }
    }

    fun resetLoggedInState() {
        _loggedIn.value = null
    }

    fun logout() {
        _loggedIn.value = null
        _loggedInUser.value = null
        _loggedInUserEmail.value = ""
        _loggedInUserUsername.value = ""
        _loggedInUserPoints.value = 0
    }

    fun getUserSecret(email: String) {
        viewModelScope.launch {
            _loggedInUserSecret.value = repository.getUserSecret(email)
        }
    }

    fun getUserPoints(email: String) {
        viewModelScope.launch {
            _loggedInUserPoints.value = repository.getUserPoints(email)
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

    fun updateUserPoints(points: Int, email: String) {
        viewModelScope.launch {
            repository.updateUserPoints(points, email)
            _loggedInUserPoints.value = repository.getUserPoints(email)
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
            loggedInUserSecret.value?.let {
                hashString(password,
                    it
                )
            }?.let { repository.updatePassword(password = it, id = id) }
        }
    }

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch {

            val randomSecretGenerator = RandomSecretGenerator()
            val secret: ByteArray = randomSecretGenerator.createRandomSecret(HmacAlgorithm.SHA1)

            _registeredUserSecret.value = secret

            val user = User(email = email, userName = username, password = hashString(password, secret), secret = secret, points = 0)
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