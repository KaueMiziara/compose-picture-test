import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun CustomFilePicker(onFileSelected: (File) -> Unit) {
    var currentDirectory by remember { mutableStateOf(File(System.getProperty("user.home"))) }
    var files by remember { mutableStateOf(currentDirectory.listFiles()?.toList() ?: emptyList()) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Current Directory: ${currentDirectory.absolutePath}", style = MaterialTheme.typography.h6)

        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {
                currentDirectory.parentFile?.let {
                    currentDirectory = it
                    files = it.listFiles()?.toList() ?: emptyList()
                }
            },
            enabled = currentDirectory.parentFile != null,
        ) {
            Text("Go to Parent Directory")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(files) { file ->
                Row(modifier = Modifier.fillMaxWidth().clickable {
                    if (file.isDirectory) {
                        currentDirectory = file
                        files = file.listFiles()?.toList() ?: emptyList()
                    } else {
                        onFileSelected(file)
                    }
                }.padding(8.dp)) {
                    Text(
                        text = if (file.isDirectory) "[DIR] ${file.name}" else file.name,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun FilePickerDemo() {
    val selectedFiles = remember { mutableStateListOf<File>() }
    var showPicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Selected Files: ${selectedFiles.joinToString { it.name }}",
            style = MaterialTheme.typography.body1,
        )

        Button(onClick = { showPicker = !showPicker }, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Open File Picker")
        }

        if (showPicker) {
            CustomFilePicker(
                onFileSelected = { file ->
                    selectedFiles.add(file)
                },
            )
        }
    }
}
