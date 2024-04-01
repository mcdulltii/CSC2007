package com.csc2007.notetaker.database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.AvatarItem
import com.csc2007.notetaker.database.repository.AvatarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AvatarViewModel(private val repository: AvatarRepository): ViewModel() {

    private val _equippedHat = MutableStateFlow<AvatarItem?>(null)
    var equippedHat: StateFlow<AvatarItem?> = _equippedHat

    private val _equippedAccessory = MutableStateFlow<AvatarItem?>(null)
    var equippedAccessory: StateFlow<AvatarItem?> = _equippedAccessory

    private val _avatarImageString = MutableStateFlow<String>("base_avatar")
    var avatarImageString: StateFlow<String> = _avatarImageString

    fun getUserAvatar(userId: Int) {
        viewModelScope.launch {
            val avatar = repository.getUserAvatar(userId)


            if (avatar.hat !== null) {
                _equippedHat.value = repository.getEquippedHat(avatar.userId)
//                _avatarImageString.value = _avatarImageString.value + "_" + _equippedHat.value!!.image
            }

            if (avatar.accessory !== null) {
                _equippedAccessory.value = repository.getEquippedAccessory(avatar.userId)
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedAccessory.value!!.image
            }
        }
    }

    fun getUserAvatarImage() {
        viewModelScope.launch {

            _avatarImageString.value = "base_avatar"

            if (_equippedHat.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedHat.value!!.image
            }

            if (_equippedAccessory.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedAccessory.value!!.image
            }
        }
    }

    fun equipItem(userId: Int, itemId: Int?, itemType: String?) {
        viewModelScope.launch {
            if (itemType == "Hat") {
                if (itemId != null) {
                    repository.equipHat(itemId)
                }
            }
            val avatar = repository.getUserAvatar(userId)


            if (avatar.hat !== null) {
                _equippedHat.value = repository.getEquippedHat(avatar.userId)
            }

            if (avatar.accessory !== null) {
                _equippedAccessory.value = repository.getEquippedAccessory(avatar.userId)
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedAccessory.value!!.image
            }

            _avatarImageString.value = "base_avatar"

            if (_equippedHat.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedHat.value!!.image
            }

            if (_equippedAccessory.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedAccessory.value!!.image
            }
        }
    }
}

class AvatarViewModelFactory(private val repository: AvatarRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AvatarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AvatarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}