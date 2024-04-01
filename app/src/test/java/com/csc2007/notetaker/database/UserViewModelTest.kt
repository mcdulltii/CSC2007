package com.csc2007.notetaker.database

import com.csc2007.notetaker.database.repository.UsersRepository
import com.csc2007.notetaker.database.viewmodel.UserViewModel
import com.csc2007.notetaker.database.viewmodel.hashString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @Mock
    private lateinit var repository: UsersRepository

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Create the ViewModel with the mocked repository
        repository = mock(UsersRepository::class.java)
        viewModel = UserViewModel(repository)
    }

    @After
    fun teardown() {
        // Reset main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    @Throws(Exception::class)
    fun testLogin_Successful() {
        runBlocking {
            // Given
            val email = "test@example.com"
            val password = "password"
            val secret = "secret".toByteArray()
            val hashedPassword = hashString(password, secret)
            val user = User(email = email, userName = "Test User", points = 0, password = hashedPassword, secret = secret)

            `when`(repository.getUserSecret(email)).thenReturn(secret)
            `when`(repository.login(email, hashedPassword)).thenReturn(user)

            // When
            viewModel.login(email, password)

            // Then
            assertEquals(true, viewModel.loggedIn.value)
            assertEquals(email, viewModel.loggedInUserEmail.value)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testLogin_Failed() {
        runBlocking {
            // Given
            val email = "test@example.com"
            val password = "password"
            val secret = "secret".toByteArray()
            val hashedPassword = hashString(password, secret)

            `when`(repository.getUserSecret(email)).thenReturn(secret)
            `when`(repository.login(email, hashedPassword)).thenReturn(null)

            // When
            viewModel.login(email, password)

            // Then
            assertEquals(null, viewModel.loggedIn.value)
            assertEquals("", viewModel.loggedInUserEmail.value)
        }
    }
}
