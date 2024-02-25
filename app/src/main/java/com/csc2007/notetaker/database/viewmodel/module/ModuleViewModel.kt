package com.csc2007.notetaker.database.viewmodel.module

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.entity.Module
import com.csc2007.notetaker.database.repository.ModulesRepository
import com.csc2007.notetaker.database.viewmodel.note.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.graphics.BitmapFactory
import android.net.Uri



class ModuleViewModel(private val repository: ModulesRepository, private val appContext: Context) : ViewModel() {

    private val _state = MutableStateFlow(ModuleState())
//    val state: StateFlow<ModuleState> = _state.asStateFlow()

    private var _sortType = MutableStateFlow(SortType.ASC_DATE_ADDED)

    private val _searchQuery = MutableStateFlow("")


    private var modules = combine(_sortType, _searchQuery) { sortType, searchQuery ->
        // Filter and sort notes based on sortType and searchQuery
        repository.modules.map { modules ->
            modules.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
                .let { filteredModules ->
                    when (sortType) {
                        SortType.ASC_TITLE -> filteredModules.sortedBy { it.title }
                        SortType.DESC_TITLE -> filteredModules.sortedByDescending { it.title }
                        SortType.ASC_DATE_ADDED -> filteredModules.sortedBy { it.dateCreated }
                        SortType.DESC_DATE_ADDED -> filteredModules.sortedByDescending { it.dateCreated }
                    }
                }
        }
    }.flatMapLatest { it }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, modules, _searchQuery) { state, sortType, modules, searchQuery ->
        state.copy(modules = modules, sortType = sortType, searchQuery = searchQuery)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ModuleState())



    fun onEvent(event: ModuleEvent) {
        when (event) {
            is ModuleEvent.SaveModule -> saveModuleWithImage(event.title, event.imageUri)
            is ModuleEvent.DeleteModule -> TODO()
            is ModuleEvent.SearchModule -> _searchQuery.value = event.query
            is ModuleEvent.SortModules -> {
                _sortType.value = event.sortType
            }
        }
    }

    fun saveModuleWithImage(title: String, imageUri: Uri) {
        viewModelScope.launch {
            _state.value = ModuleState(isSaving = true)
            try {
                val bitmap = withContext(Dispatchers.IO) { uriToBitmap(appContext, imageUri) }
                if (bitmap != null) {
                    val filename = "image_${System.currentTimeMillis()}"
                    val imagePath = saveImageToInternalStorage(appContext, bitmap, filename)

                    val module = Module(title = title, dateCreated = System.currentTimeMillis(),imagePath = imagePath)
                    repository.upsertModule(module)

                    _state.value = ModuleState(isSaving = false, saveSuccess = true)
                } else {
                    // Handle the error case where bitmap conversion failed
                    _state.value = ModuleState(isSaving = false, saveSuccess = false)
                }
            } catch (e: Exception) {
                _state.value = ModuleState(isSaving = false, saveSuccess = false)
            }
        }
    }


//    private fun saveEntityWithImage(title: String, bitmap: Bitmap, content: String) {
//        viewModelScope.launch {
//            _state.value = ModuleState(isSaving = true)
//
//            try {
//                val filename = "image_${System.currentTimeMillis()}"
//                val imagePath = withContext(Dispatchers.IO) {
//                    saveImageToInternalStorage(appContext, bitmap, filename)
//                }
//
//                val module = Module(title = title, dateCreated = System.currentTimeMillis(),content = content, imagePath = imagePath )
//
//                repository.upsertModule(module)
//
//                _state.value = ModuleState(isSaving = false, saveSuccess = true)
//            } catch (e: Exception) {
//                _state.value = ModuleState(isSaving = false, saveSuccess = false)
//            }
//        }
//    }

    private suspend fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, filename: String): String {
        context.openFileOutput("$filename.png", Context.MODE_PRIVATE).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return filename
    }

    fun saveEntityWithImage(title: String, imageUri: Uri, content: String, context: Context) {
        viewModelScope.launch {
            _state.value = ModuleState(isSaving = true)
            try {
                val bitmap = withContext(Dispatchers.IO) { uriToBitmap(context, imageUri) }
                val filename = "image_${System.currentTimeMillis()}"
                val imagePath = bitmap?.let { saveImageToInternalStorage(context, it, filename) }

                val module = imagePath?.let { Module(title = title, dateCreated = System.currentTimeMillis(), imagePath = it) }
                if (module != null) {
                    repository.upsertModule(module)
                }

                _state.value = ModuleState(isSaving = false, saveSuccess = true)
            } catch (e: Exception) {
                _state.value = ModuleState(isSaving = false, saveSuccess = false)
            }
        }
    }

    private suspend fun uriToBitmap(context: Context, imageUri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }
}

class ModuleViewModelFactory(private val repository: ModulesRepository, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModuleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ModuleViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
