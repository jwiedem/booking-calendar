package ui

import Booking
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BookingCalendar(bookings: List<Booking>) {
    val grouped = bookings.groupBy { it.startDate.month }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        grouped.forEach { (month, monthBookings) ->
            item {
                Text(month.name, style = MaterialTheme.typography.h5)
            }
            items(monthBookings) {
                BookingRow(it)
            }
        }
    }
}

@Composable
fun BookingRow(booking: Booking) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Text(booking.guestName, style = MaterialTheme.typography.h6)
            Text("${booking.startDate} → ${booking.endDate}")
        }
    }
}

@Composable
fun MonthCalendar(
    year: Int,
    month: Month,
    bookings: List<Booking>
) {
    val firstDay = LocalDate.of(year, month, 1)
    val daysInMonth = month.length(Year.isLeap(year.toLong()))
    val firstWeekday = firstDay.dayOfWeek.value % 7 // Monday = 1

    Column {
        // Month title
        Text(
            month.name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Weekday header
        Row {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(it)
                }
            }
        }

        // Calendar grid
        var dayCounter = 1
        val totalCells = ((firstWeekday + daysInMonth + 6) / 7) * 7

        for (cell in 0 until totalCells) {
            if (cell % 7 == 0) {
                Row
                Spacer(modifier = Modifier.height(4.dp))
            }
            val rowIndex = cell / 7
            val colIndex = cell % 7

            if (colIndex == 0) {
                Row {
                    repeat(7) { col ->
                        val index = rowIndex * 7 + col
                        CalendarCell(
                            index,
                            firstWeekday,
                            daysInMonth,
                            year,
                            month,
                            bookings
                        )
                    }
                }
            }
        }
    }
}