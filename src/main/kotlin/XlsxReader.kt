import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.Month

object XlsxReader {
    val maxIndex = 32
    val unbelegt = "unbelegt"

    fun readBookings(file: File, apartmentName: String): List<Booking> {
        val bookings = mutableListOf<Booking>()
        val fis = FileInputStream(file)
        val workbook = XSSFWorkbook(fis)
        val sheet = workbook.getSheetAt(0)

        for (row in sheet.drop(3)) {
            var month = 1
            if (row.getCell(0)?.stringCellValue?.isNotEmpty() == true) {
                bookings.add(getBookings(row, Month.of(month++)))
            }
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

    fun getBookings(row: Row, month: Month): List<Booking> {
        val bookings = mutableListOf<Booking>()
        var currentGuest: String? = null
        var startDay = -1

        for (column in 2..maxIndex) {
            val currentDay = column - 1
            val guest = cellText(row.getCell(column))

            if (guest != null) {
                if (currentGuest == null) {
                    currentGuest = guest
                    startDay = currentDay
                } else if (guest != currentGuest) {
                    bookings.add(
                        Booking(
                        currentGuest,
                        "Garten",
                        LocalDate.of(26, month, startDay),
                        LocalDate.of(26, month, currentDay - 1))
                    )
                    if (guest != unbelegt) {
                        currentGuest = guest
                        startDay = currentDay
                    } else {
                        currentGuest = null
                    }
                }
                currentGuest = guest
            }
        }
        return bookings
    }
}