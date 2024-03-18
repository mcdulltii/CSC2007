package com.csc2007.notetaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.database.NoteTakingApp
import com.csc2007.notetaker.database.repository.Firestorage_db
import com.csc2007.notetaker.database.repository.Firestore_db
import com.csc2007.notetaker.database.viewmodel.AvatarViewModelFactory
import com.csc2007.notetaker.database.viewmodel.ItemViewModelFactory
import com.csc2007.notetaker.database.viewmodel.OwnViewModelFactory
import com.csc2007.notetaker.database.viewmodel.UserViewModelFactory
import com.csc2007.notetaker.database.viewmodel.module.ModuleViewModelFactory
import com.csc2007.notetaker.database.viewmodel.note.NoteViewModelFactory
import com.csc2007.notetaker.ui.NoteTakerTheme
import com.csc2007.notetaker.ui.util.NavGraph
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userViewModelFactory = UserViewModelFactory(
            (application as NoteTakingApp).userRepository
        )

        val itemViewModelFactory = ItemViewModelFactory(
            (application as NoteTakingApp).itemRepository
        )

        val ownViewModelFactory = OwnViewModelFactory(
            (application as NoteTakingApp).ownRepository
        )

        val avatarViewModelFactory = AvatarViewModelFactory(
            (application as NoteTakingApp).avatarRepository
        )

        val noteViewModelFactory = NoteViewModelFactory(
            (application as NoteTakingApp).noteRepository
        )

        val moduleViewModelFactory = ModuleViewModelFactory(
            (application as NoteTakingApp).moduleRepository,
            applicationContext, (application as NoteTakingApp).noteRepository
        )

//        val firestore_db = Firebase.firestore // idk why it says Firestore_db can't resolve sometimes, extremely weird
        val firestore_db = Firestore_db().get_firestore_db(this)
        val firestorage = Firestorage_db().get_firestorage_db(this)

        setContent {
            MainApp(
                userViewModelFactory = userViewModelFactory,
                noteViewModelFactory = noteViewModelFactory,
                moduleViewModelFactory = moduleViewModelFactory,
                itemViewModelFactory = itemViewModelFactory,
                ownViewModelFactory = ownViewModelFactory,
                avatarViewModelFactory = avatarViewModelFactory,
                firestore_db = firestore_db,
                firestorage = firestorage
            )
//            ,
//            ChatRoomViewModelFactory = ChatRoomViewModelFactory,
//            ChatMessageViewModelFactory = ChatMessageViewModelFactory
        }
    }
}

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun MainApp(
    userViewModelFactory: UserViewModelFactory,
    noteViewModelFactory: NoteViewModelFactory,
    moduleViewModelFactory: ModuleViewModelFactory,
    itemViewModelFactory: ItemViewModelFactory,
    ownViewModelFactory: OwnViewModelFactory,
    avatarViewModelFactory: AvatarViewModelFactory,
    firestore_db: FirebaseFirestore,
    firestorage: FirebaseStorage
) {
    val navController = rememberNavController()

    NoteTakerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NavGraph(
                navController = navController,
                userViewModelFactory = userViewModelFactory,
                noteViewModelFactory = noteViewModelFactory,
                moduleViewModelFactory = moduleViewModelFactory,
                itemViewModelFactory = itemViewModelFactory,
                ownViewModelFactory = ownViewModelFactory,
                avatarViewModelFactory = avatarViewModelFactory,
                firestore_db = firestore_db,
                firestorage = firestorage
//            ChatRoomViewModelFactory = ChatRoomViewModelFactory,
//            ChatMessageViewModelFactory = ChatMessageViewModelFactory
            )
        }
    }
}
