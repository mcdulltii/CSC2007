package com.csc2007.notetaker.database

import com.csc2007.notetaker.database.repository.ItemRepository
import com.csc2007.notetaker.database.viewmodel.ItemViewModel
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
class ItemViewModelTest {
    // Mock the repository
    @Mock
    private lateinit var mockRepository: ItemRepository

    // The ViewModel being tested
    private lateinit var viewModel: ItemViewModel

    @Before
    fun setup() {
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)

        // Create the ViewModel with the mocked repository
        mockRepository = mock(ItemRepository::class.java)
        viewModel = ItemViewModel(mockRepository)
    }

    @After
    fun teardown() {
        // Reset main dispatcher after the test
        Dispatchers.resetMain()
    }

    @Test
    fun testGetItemById() {
        runBlocking {
            // Given
            val itemId = 1
            val mockItem = Item(itemId, "Mock Item", "Type", "Rarity", "Image")
            `when`(mockRepository.getItemById(itemId)).thenReturn(mockItem)

            // When
            viewModel.getItemById(itemId)

            // Then
            assert(viewModel.itemById.value == mockItem)
        }
    }

    @Test
    fun testInsert() {
        runBlocking {
            // Given
            val itemId = 1
            val itemName = "New Item"
            val itemType = "Type"
            val itemRarity = "Rarity"
            val itemImage = "Image"

            // When
            viewModel.insert(itemId, itemName, itemType, itemRarity, itemImage)

            // Then
            // Verify that insert function in repository is called
            verify(mockRepository).insert(Item(itemId, itemName, itemType, itemRarity, itemImage))
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
