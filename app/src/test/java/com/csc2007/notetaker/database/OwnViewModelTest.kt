package com.csc2007.notetaker.database

import com.csc2007.notetaker.database.repository.OwnRepository
import com.csc2007.notetaker.database.viewmodel.OwnViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class OwnViewModelTest {
    // Mock the repository
    @Mock
    private lateinit var mockRepository: OwnRepository

    // The ViewModel being tested
    private lateinit var viewModel: OwnViewModel

    @Before
    fun setup() {
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Create the ViewModel with the mocked repository
        mockRepository = mock(OwnRepository::class.java)
        viewModel = OwnViewModel(mockRepository)
    }

    @After
    fun teardown() {
        // Reset main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun testGetOwnedItems() {
        runBlocking {
            // Given
            val userId = 1
            val type = "Hat"
            val mockItems =
                listOf(OwnItem(userId = userId, 1, "Item 1"), OwnItem(userId = userId, 2, "Item 2"))
            `when`(mockRepository.getOwnedItems(userId, type)).thenReturn(mockItems)

            // When
            viewModel.getOwnedItems(userId, type)

            // Then
            viewModel.ownedItems.value?.let {
                assert(it.size == mockItems.size)
                assert(it.containsAll(mockItems))
            }
        }
    }

    @Test
    fun testInsert() {
        runBlocking {
            // Given
            val userId = 1
            val itemId = 1
            `when`(mockRepository.checkIfUserOwnItem(userId, itemId)).thenReturn(null)

            // When
            viewModel.insert(userId, itemId)

            // Then
            // Verify that insert function in repository is called
            verify(mockRepository).insert(Own(userId = userId, itemId = itemId))
        }
    }

    @Test
    fun testDeleteAll() {
        runBlocking {
            // When
            viewModel.deleteAll()

            // Then
            // Verify that deleteAll function in repository is called
            verify(mockRepository).deleteAll()
        }
    }
}
