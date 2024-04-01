package com.csc2007.notetaker.database

import com.csc2007.notetaker.database.repository.AvatarRepository
import com.csc2007.notetaker.database.viewmodel.AvatarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class AvatarViewModelTest {
    // Mock the repository
    @Mock
    private lateinit var mockRepository: AvatarRepository

    // The ViewModel being tested
    private lateinit var viewModel: AvatarViewModel

    @Before
    fun setup() {
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Create the ViewModel with the mocked repository
        mockRepository = mock(AvatarRepository::class.java)
        viewModel = AvatarViewModel(mockRepository)
    }

    @After
    fun teardown() {
        // Reset main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun testGetUserAvatar() = runBlockingTest {
        // Given
        val userId = 1
        val mockAvatar = Avatar(1, 1, 2)

        // Mock repository responses
        `when`(mockRepository.getUserAvatar(userId)).thenReturn(mockAvatar)

        // When
        viewModel.getUserAvatar(userId)

        // Then
        verify(mockRepository).getUserAvatar(userId)
    }
}
