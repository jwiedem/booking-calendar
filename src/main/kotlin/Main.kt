import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var count by remember { mutableStateOf(0) }

    MaterialTheme {
        Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            Text("Hello Windows Compose Desktop!", style = MaterialTheme.typography.h5)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            Button(onClick = { count++ }) {
                Text("Clicked $count times")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Rental Calendar") {
        App()
    }
}