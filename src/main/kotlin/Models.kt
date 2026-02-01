import java.time.LocalDate

data class Booking(
    val guestName: String,
    val apartmentName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)