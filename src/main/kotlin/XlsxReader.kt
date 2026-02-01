import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.Month
import java.time.Year

object XlsxReader {
    val unbelegt = "unbelegt"

    fun readBookings(file: File, apartmentName: String, year: Year): List<Booking> {
        val bookings = mutableListOf<Booking>()
        val openBookings = mutableMapOf<String, LocalDate>()

        val workbook = XSSFWorkbook(FileInputStream(file))
        val sheet = workbook.getSheetAt(0)

        var monthIndex = 1

        for (row in sheet.drop(3)) {
            if (row.getCell(0)?.stringCellValue?.isNotBlank() == true) {
                val month = Month.of(monthIndex++)
                processMonthRow(
                    row,
                    month,
                    year,
                    apartmentName,
                    openBookings,
                    bookings
                )
            }
        }

        // close remaining bookings at end of year
        for ((guest, startDate) in openBookings) {
            bookings += Booking(
                guest,
                apartmentName,
                startDate,
                startDate.withMonth(12).withDayOfMonth(31)
            )
        }

        return bookings
    }

    fun cellText(cell: Cell?): String? {
        if (cell == null) return null

        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue.trim().takeIf { it.isNotEmpty() }
            CellType.FORMULA -> cell.stringCellValue.trim().takeIf { it.isNotEmpty() }
            else -> null
        }
    }

    fun processMonthRow(
        row: Row,
        month: Month,
        year: Year,
        apartment: String,
        openBookings: MutableMap<String, LocalDate>,
        bookings: MutableList<Booking>
    ) {
        val daysInMonth = month.length(year.isLeap)

        for (day in 1..daysInMonth) {
            val column = day + 1
            val guest = cellText(row.getCell(column))

            val date = LocalDate.of(year.value, month, day)

            // Close bookings that disappear
            for ((openGuest, startDate) in openBookings.toMap()) {
                if (openGuest != guest) {
                    bookings += Booking(
                        openGuest,
                        apartment,
                        startDate,
                        date
                    )
                    openBookings.remove(openGuest)
                }
            }

            // Open new booking
            if (guest != null && guest != unbelegt && !openBookings.containsKey(guest)) {
                openBookings[guest] = date
            }
        }
    }

}