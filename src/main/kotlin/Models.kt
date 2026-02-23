import java.time.LocalDate

data class Booking(
    val bookingId: Int,
    val guestName: String,
    val apartmentName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
) {
    constructor(
        guestName: String,
        apartmentName: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ) : this(
        0,
        guestName,
        apartmentName,
        startDate,
        endDate,
    )
}
