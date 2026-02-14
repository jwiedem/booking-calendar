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
    val bookings = remember { mutableStateOf<List<Booking>>(emptyList()) }
    val displayYear = remember { Year.of(2026) }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Hello Michael!", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val garten = readBookings(
                        File("src/main/resources/Belegungsplan_Garten - Kopie.xlsx"),
                        "Garten",
                        displayYear,
                    )
                    val parkblick = readBookings(
                        File("src/main/resources/Belegungsplan_Parkblick - Kopie.xlsx"),
                        "Parkblick",
                        displayYear,
                    )
                    val weitblick = readBookings(
                        File("src/main/resources/Belegungsplan_Weitblick - Kopie.xlsx"),
                        "Weitblick",
                        displayYear,
                    )
                    BookingRepository.saveBookings(garten)
                    BookingRepository.saveBookings(parkblick)
                    BookingRepository.saveBookings(weitblick)
                    bookings.value = BookingRepository.loadBookings()
                }
            ) {
                Text("Import and save bookings")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    bookings.value = BookingRepository.loadBookings()
                }
            ) {
                Text("Load bookings from database")
            }

            Spacer(Modifier.height(16.dp))

            BookingCalendar(bookings.value, displayYear)
        }
    }
}

fun main() = application {
    Database.init()
    Window(onCloseRequest = ::exitApplication, title = "Booking Calendar") {
        App()
    }
}
