import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.csc2007.notetaker.database.viewmodel.note.NoteEvent
import com.csc2007.notetaker.database.viewmodel.note.NoteState

@Composable
fun NoteSearchBar(state: NoteState, onEvent: (NoteEvent) -> Unit) {
    OutlinedTextField(
        value = state.searchQuery,
        onValueChange = { onEvent(NoteEvent.SearchNote(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("Search notes...") },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon"
            )
        },
        shape = RoundedCornerShape(8.dp), // Apply border radius
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray, // Customize border color when focused
            unfocusedBorderColor = Color.Gray, // Customize border color
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}
