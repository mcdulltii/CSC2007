package com.csc2007.notetaker.database.viewmodel.module

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.csc2007.notetaker.database.entity.Module
import com.csc2007.notetaker.database.entity.Note
import com.csc2007.notetaker.database.viewmodel.note.SortType

sealed interface ModuleEvent {
    data class SortModules(val sortType: SortType) : ModuleEvent

    data class DeleteModule(val module: Module) : ModuleEvent


    data class SearchModule(val query: String) : ModuleEvent

    data class SaveModule(val title: String, val imageUri: Uri) : ModuleEvent

}