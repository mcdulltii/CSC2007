package com.csc2007.notetaker.database.viewmodel.module

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.csc2007.notetaker.database.entity.Module
import com.csc2007.notetaker.database.viewmodel.note.SortType


data class ModuleState(
    val modules: List<Module> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val sortType: SortType = SortType.ASC_TITLE,
    val searchQuery: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean? = null
)

