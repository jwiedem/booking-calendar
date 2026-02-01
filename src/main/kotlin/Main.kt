import XlsxReader.readBookings
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.BookingCalendar
import java.io.File
import java.time.Year

@Composable
@Preview
fun App() {
    var count by remember { mutableStateOf(0) }
    val bookings = remember { mutableStateOf<List<Booking>>(emptyList()) }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Hello Michael!", style = MaterialTheme.typography.h5)
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            Button(
                onClick = {
                    bookings.value = readBookings(
                        File("src/main/resources/Belegungsplan_Garten - Kopie.xlsx"),
                        "Garten",
                        Year.of(2026),
                    )
                }
            ) {
                Text("Load bookings")
            }

            Spacer(Modifier.height(16.dp))

            BookingCalendar(bookings.value)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Rental Calendar") {
        App()
    }
}
