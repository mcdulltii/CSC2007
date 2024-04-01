package com.csc2007.notetaker

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.annotation.ExperimentalCoilApi
import com.csc2007.notetaker.database.repository.Firestorage_db
import com.csc2007.notetaker.database.repository.Firestore_db
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalCoilApi::class,
        ExperimentalCoroutinesApi::class
    )
    @Test
    fun testMainActivityLayout() {
        // Start MainActivity
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Add assertions to test MainActivity behavior
        scenario.onActivity { activity ->
            // Assert that activity is not null
            assertNotNull(activity)
        }
    }

    @Test
    fun testFirestoreInitialization() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val firestoreDb = Firestore_db().get_firestore_db(context)

        // Assert that firestore is not null
        assertNotNull(firestoreDb)
    }

    @Test
    fun testFirestorageInitialization() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val firestorage = Firestorage_db().get_firestorage_db(context)

        // Assert that firestorage is not null
        assertNotNull(firestorage)
    }
}
