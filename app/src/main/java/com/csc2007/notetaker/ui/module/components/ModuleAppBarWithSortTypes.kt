
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.csc2007.notetaker.database.viewmodel.module.ModuleEvent
import com.csc2007.notetaker.database.viewmodel.note.SortType

@Composable
fun ModuleAppBarWithSortTypes(onEvent: (ModuleEvent) -> Unit) {
    var isTitle by remember { mutableStateOf(true) }
    var isAscending by remember { mutableStateOf(true) }

    // Common button styling
    val buttonSize = Modifier.size(70.dp, 30.dp)
    val commonBorder = BorderStroke(1.dp, Color.Black)
    val commonShape = RoundedCornerShape(10.dp)
    val contentPadding = PaddingValues(0.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Title Button
        Button(
            onClick = {
                onEvent(ModuleEvent.SortModules(if (isAscending) SortType.ASC_TITLE else SortType.DESC_TITLE))
                isTitle = true
            },
            modifier = buttonSize,
            border = commonBorder,
            colors = ButtonDefaults.buttonColors(if (isTitle) Color(0xFF526070) else Color.White),
            shape = commonShape,
            contentPadding = contentPadding
        ) {
            Text("Title", color = if (isTitle) Color.White else Color(0xFF526070))
        }

        // Date Button
        Button(
            onClick = {
                onEvent(ModuleEvent.SortModules(if (isAscending) SortType.ASC_DATE_ADDED else SortType.DESC_DATE_ADDED))
                isTitle = false
            },
            modifier = buttonSize,
            border = commonBorder,
            colors = ButtonDefaults.buttonColors(if (!isTitle) Color(0xFF526070) else Color.White),
            shape = commonShape,
            contentPadding = contentPadding
        ) {
            Text("Date", color = if (!isTitle) Color.White else Color(0xFF526070))
        }

        // Sort Ascending Button
        Button(
            onClick = {
                onEvent(if (isTitle) ModuleEvent.SortModules(SortType.ASC_TITLE) else ModuleEvent.SortModules(SortType.ASC_DATE_ADDED))
                isAscending = true
            },
            modifier = Modifier.size(100.dp, 30.dp),
            border = commonBorder,
            colors = ButtonDefaults.buttonColors(if (isAscending) Color(0xFF526070) else Color.White),
            shape = commonShape,
            contentPadding = contentPadding
        ) {
            Text("Ascending", color = if (isAscending) Color.White else Color.Black)
        }

        // Sort Descending Button
        Button(
            onClick = {
                onEvent(if (isTitle) ModuleEvent.SortModules(SortType.DESC_TITLE) else ModuleEvent.SortModules(SortType.DESC_DATE_ADDED))
                isAscending = false
            },
            modifier = Modifier.size(100.dp, 30.dp),
            border = commonBorder,
            colors = ButtonDefaults.buttonColors(if (!isAscending) Color(0xFF526070) else Color.White),
            shape = commonShape,
            contentPadding = contentPadding
        ) {
            Text("Descending", color = if (!isAscending) Color.White else Color.Black)
        }
    }
}
