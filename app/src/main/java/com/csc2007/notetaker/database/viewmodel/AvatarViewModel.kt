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

    private val _equippedShirt = MutableStateFlow<AvatarItem?>(null)
    var equippedShirt: StateFlow<AvatarItem?> = _equippedShirt

    private val _avatarImageString = MutableStateFlow<String>("base_avatar")
    var avatarImageString: StateFlow<String> = _avatarImageString

    fun getUserAvatar(userId: Int) {
        viewModelScope.launch {
            val avatar = repository.getUserAvatar(userId)


            if (avatar.hat !== null) {
                _equippedHat.value = repository.getEquippedHat(avatar.userId)
            }

            if (avatar.accessory !== null) {
                _equippedAccessory.value = repository.getEquippedAccessory(avatar.userId)
            }

            if (avatar.shirt !== null) {
                _equippedShirt.value = repository.getEquippedShirt(avatar.userId)
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

            if (_equippedShirt.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedShirt.value!!.image
            }
        }
    }

    fun equipItem(userId: Int, itemId: Int?, itemType: String?) {
        viewModelScope.launch {
            if (itemType == "Hat") {
                if (itemId != null) {
                    repository.equipHat(itemId, userId)
                }
            }

            if (itemType == "Accessory") {
                if (itemId != null) {
                    repository.equipAccessory(itemId, userId)
                }
            }

            if (itemType == "Shirt") {
                if (itemId != null) {
                    repository.equipShirt(itemId, userId)
                }
            }
            val avatar = repository.getUserAvatar(userId)


            if (avatar.hat !== null) {
                _equippedHat.value = repository.getEquippedHat(avatar.userId)
            }

            if (avatar.accessory !== null) {
                _equippedAccessory.value = repository.getEquippedAccessory(avatar.userId)
            }

            if (avatar.shirt !== null) {
                _equippedShirt.value = repository.getEquippedShirt(avatar.userId)
            }

            _avatarImageString.value = "base_avatar"

            if (_equippedHat.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedHat.value!!.image
            }

            if (_equippedAccessory.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedAccessory.value!!.image
            }

            if (_equippedShirt.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedShirt.value!!.image
            }
        }
    }

    fun unEquipItem(userId: Int, itemType: String?) {
        viewModelScope.launch {
            if (itemType == "Hat") {
                repository.unEquipHat(userId)
            }

            if (itemType == "Accessory") {
                repository.unEquipAccessory(userId)
            }

            if (itemType == "Shirt") {
                repository.unEquipShirt(userId)
            }

            val avatar = repository.getUserAvatar(userId)

            _equippedHat.value = repository.getEquippedHat(avatar.userId)
            _equippedAccessory.value = repository.getEquippedAccessory(avatar.userId)
            _equippedShirt.value = repository.getEquippedShirt(avatar.userId)


            _avatarImageString.value = "base_avatar"

            if (_equippedHat.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedHat.value!!.image
            }

            if (_equippedAccessory.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedAccessory.value!!.image
            }

            if (_equippedShirt.value != null) {
                _avatarImageString.value = _avatarImageString.value + "_" + _equippedShirt.value!!.image
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