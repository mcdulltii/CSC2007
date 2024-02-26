package com.csc2007.notetaker.database.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.Item
import com.csc2007.notetaker.database.repository.ItemRepository
import com.csc2007.notetaker.database.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ItemViewModel(private val repository: ItemRepository): ViewModel() {

    private val _itemById = MutableStateFlow<Item?>(null)
    var itemById: StateFlow<Item?> = _itemById

    fun getItemById(id: Int) {
        viewModelScope.launch {
            val item = repository.getItemById(id)
            _itemById.value = item
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