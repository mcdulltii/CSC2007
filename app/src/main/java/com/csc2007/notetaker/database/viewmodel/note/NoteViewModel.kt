package com.csc2007.notetaker.database.viewmodel.note


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.csc2007.notetaker.database.entity.Note
import com.csc2007.notetaker.database.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class NoteViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    private var _sortType = MutableStateFlow(SortType.ASC_DATE_ADDED)

    private val _state = MutableStateFlow(NoteState())

    private val _searchQuery = MutableStateFlow("")


    private var notes = combine(_sortType, _searchQuery) { sortType, searchQuery ->
        // Filter and sort notes based on sortType and searchQuery
        repository.notes.map { notes ->
            notes.filter {
                it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
                .let { filteredNotes ->
                    when (sortType) {
                        SortType.ASC_TITLE -> filteredNotes.sortedBy { it.title }
                        SortType.DESC_TITLE -> filteredNotes.sortedByDescending { it.title }
                        SortType.ASC_DATE_ADDED -> filteredNotes.sortedBy { it.dateAdded }
                        SortType.DESC_DATE_ADDED -> filteredNotes.sortedByDescending { it.dateAdded }
                    }
                }
        }
    }.flatMapLatest { it }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    val state = combine(_state, _sortType, notes, _searchQuery) { state, sortType, notes, searchQuery ->
        state.copy(notes = notes, sortType = sortType, searchQuery = searchQuery)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    fun onEvent(event: NoteEvent) {
        when (event) {

            is NoteEvent.SearchNote -> {
                _searchQuery.value = event.query
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    repository.deleteNote(event.note)
                }
            }

            is NoteEvent.SaveNote -> {
                val note =
                    Note(
                        title = state.value.title.value,
                        content = state.value.content.value,
                        dateAdded = System.currentTimeMillis(),
                        moduleId = event.moduleId,
                    )
                viewModelScope.launch {
                    repository.upsertNote(note)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        content = mutableStateOf("")
                    )
                }
            }

            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
            }

            else -> {}
        }
    }
}

class NoteViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}