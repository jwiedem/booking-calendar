import java.time.LocalDate

data class Booking(
    val guestName: String,
    val apartmentName: String,
    val beginDate: LocalDate?,
    val endDate: LocalDate?,
)