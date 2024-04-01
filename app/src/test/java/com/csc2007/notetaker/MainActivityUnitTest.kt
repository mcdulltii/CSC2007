package com.csc2007.notetaker

import com.csc2007.notetaker.database.repository.AvatarRepository
import com.csc2007.notetaker.database.repository.ItemRepository
import com.csc2007.notetaker.database.repository.ModulesRepository
import com.csc2007.notetaker.database.repository.NotesRepository
import com.csc2007.notetaker.database.repository.OwnRepository
import com.csc2007.notetaker.database.repository.UsersRepository
import com.csc2007.notetaker.database.viewmodel.AvatarViewModelFactory
import com.csc2007.notetaker.database.viewmodel.ItemViewModelFactory
import com.csc2007.notetaker.database.viewmodel.OwnViewModelFactory
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModelFactory
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModelFactory
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.mockito.Mockito.mock

class MainActivityUnitTest {

    @Test
    fun check_factories_initialization() {
        // Mock dependencies
        val userRepository = mock(UsersRepository::class.java)
        val itemRepository = mock(ItemRepository::class.java)
        val ownRepository = mock(OwnRepository::class.java)
        val avatarRepository = mock(AvatarRepository::class.java)
        val noteRepository = mock(NotesRepository::class.java)
        val moduleRepository = mock(ModulesRepository::class.java)

        val userViewModelFactory = UserViewModelFactory(userRepository)
        val itemViewModelFactory = ItemViewModelFactory(itemRepository)
        val ownViewModelFactory = OwnViewModelFactory(ownRepository)
        val avatarViewModelFactory = AvatarViewModelFactory(avatarRepository)
        val noteViewModelFactory = NoteViewModelFactory(noteRepository)
        val moduleViewModelFactory = ModuleViewModelFactory(moduleRepository, mock(), noteRepository)

        // Assert that factories are not null
        assertNotNull(userViewModelFactory)
        assertNotNull(itemViewModelFactory)
        assertNotNull(ownViewModelFactory)
        assertNotNull(avatarViewModelFactory)
        assertNotNull(noteViewModelFactory)
        assertNotNull(moduleViewModelFactory)
    }
}
