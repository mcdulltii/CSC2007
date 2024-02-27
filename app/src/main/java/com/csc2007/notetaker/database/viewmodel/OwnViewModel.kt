package com.csc2007.notetaker.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.Own
import com.csc2007.notetaker.database.OwnItem
import com.csc2007.notetaker.database.repository.OwnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OwnViewModel(private val repository: OwnRepository): ViewModel() {

    private val _ownedItems = MutableStateFlow<List<OwnItem>?>(null)
    var ownedItems: StateFlow<List<OwnItem>?> = _ownedItems

    fun getOwnedItems(userId: Int) {
        viewModelScope.launch {
            _ownedItems.value = repository.getOwnedItems(userId)
        }
    }

    fun insert(userId: Int, itemId: Int) {
        viewModelScope.launch {
            val own = Own(userId = userId, itemId = itemId)
            val ownedByUser = repository.checkIfUserOwnItem(userId = userId, itemId = itemId)
            if (ownedByUser == null) {
                repository.insert(own)
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}

class OwnViewModelFactory(private val repository: OwnRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OwnViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}