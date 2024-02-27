package com.csc2007.notetaker.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.Item
import com.csc2007.notetaker.database.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ItemViewModel(private val repository: ItemRepository): ViewModel() {

    private val _itemById = MutableStateFlow<Item?>(null)
    var itemById: StateFlow<Item?> = _itemById

    val allItems = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun getItemById(id: Int) {
        viewModelScope.launch {
            val item = repository.getItemById(id)
            _itemById.value = item
        }
    }

    fun insert(id: Int, name: String, type: String, rarity: String, image: String) {
        viewModelScope.launch {
            val item = Item(id = id, name = name, type = type, rarity = rarity, image = image)
            repository.insert(item)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}

class ItemViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}