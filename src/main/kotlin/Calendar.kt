package ui

import Booking
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

@Composable
fun BookingCalendar(bookings: List<Booking>, year: Year) {
    val bookingDays = buildBookingDayMap(bookings, year)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        year.months().forEachIndexed { index, yearMonth ->
            item {
                MonthGrid(
                    yearMonth = yearMonth,
                    bookingDays = bookingDays,
                    monthIndex = index
                )
            }
        }
    }
}

@Composable
private fun MonthGrid(
    yearMonth: YearMonth,
    bookingDays: Map<LocalDate, List<Booking>>,
    monthIndex: Int
) {
    val monthColor = if (monthIndex % 2 == 0) Color(0xFFFFF176) else Color(0xFF81D4FA)
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        MonthRow(
            yearMonth = yearMonth,
            bookingDays = bookingDays,
            monthColor = monthColor
        )
    }
}

@Composable
private fun MonthRow(
    yearMonth: YearMonth,
    bookingDays: Map<LocalDate, List<Booking>>,
    monthColor: Color
) {
    val headerHeight = 22.dp
    val bookingHeight = 36.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        MonthLabelCell(
            yearMonth = yearMonth,
            monthColor = monthColor,
            height = headerHeight * 2 + bookingHeight
        )
        Column(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            DayHeaderRow(
                yearMonth = yearMonth,
                background = monthColor,
                height = headerHeight
            )
            DayNumberRow(
                yearMonth = yearMonth,
                background = monthColor,
                height = headerHeight
            )
            BookingRow(
                yearMonth = yearMonth,
                bookingDays = bookingDays,
                height = bookingHeight
            )
        }
    }
}

@Composable
private fun MonthLabelCell(yearMonth: YearMonth, monthColor: Color, height: Dp) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(height)
            .border(1.dp, Color.LightGray)
            .background(monthColor)
            .padding(4.dp)
    ) {
        Text(
            text = "${yearMonth.month.name} ${yearMonth.year}",
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun DayHeaderRow(yearMonth: YearMonth, background: Color, height: Dp) {
    Row {
        for (day in 1..yearMonth.lengthOfMonth()) {
            val date = yearMonth.atDay(day)
            HeaderCell(
                text = germanDayAbbrev(date),
                background = background,
                height = height
            )
        }
    }
}

@Composable
private fun DayNumberRow(yearMonth: YearMonth, background: Color, height: Dp) {
    Row {
        for (day in 1..yearMonth.lengthOfMonth()) {
            HeaderCell(
                text = day.toString(),
                background = background,
                height = height
            )
        }
    }
}

@Composable
private fun HeaderCell(text: String, background: Color, height: Dp) {
    Box(
        modifier = Modifier
            .width(28.dp)
            .height(height)
            .border(1.dp, Color.LightGray)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun BookingRow(
    yearMonth: YearMonth,
    bookingDays: Map<LocalDate, List<Booking>>,
    height: Dp
) {
    Row {
        for (day in 1..yearMonth.lengthOfMonth()) {
            val date = yearMonth.atDay(day)
            val bookings = bookingDays[date].orEmpty()
            BookingCell(
                bookings = bookings,
                isWeekend = date.dayOfWeek.value >= 6,
                height = height
            )
        }
    }
}

@Composable
private fun BookingCell(bookings: List<Booking>, isWeekend: Boolean, height: Dp) {
    val background = if (isWeekend) Color(0xFFFFF176) else Color(0xFF81D4FA)
    Box(
        modifier = Modifier
            .width(28.dp)
            .height(height)
            .border(1.dp, Color.LightGray)
            .background(background)
    ) {
        if (bookings.isNotEmpty()) {
            Text(
                text = bookings.first().guestName,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 2.dp, top = 12.dp)
                    .rotate(-45f)
            )
        }
    }
}

private fun buildBookingDayMap(
    bookings: List<Booking>,
    year: Year
): Map<LocalDate, List<Booking>> {
    val result = mutableMapOf<LocalDate, MutableList<Booking>>()
    val startOfYear = year.atDay(1)
    val endOfYear = year.atMonth(12).atEndOfMonth()
    bookings.forEach { booking ->
        val start = maxOf(booking.startDate, startOfYear)
        val end = minOf(booking.endDate, endOfYear)
        var current = start
        while (!current.isAfter(end)) {
            result.getOrPut(current) { mutableListOf() }.add(booking)
            current = current.plusDays(1)
        }
    }
    return result
}

private fun Year.months(): List<YearMonth> {
    return (1..12).map { month -> YearMonth.of(value, month) }
}

private fun germanDayAbbrev(date: LocalDate): String {
    return when (date.dayOfWeek.value) {
        1 -> "Mo"
        2 -> "Di"
        3 -> "Mi"
        4 -> "Do"
        5 -> "Fr"
        6 -> "Sa"
        7 -> "So"
        else -> ""
    }
}
