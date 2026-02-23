import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import io.ktor.serialization.kotlinx.json.*
import java.time.LocalDate

@Serializable
data class ApiBooking(
    val bookingId: Int,
    val guestName: String,
    val apartmentName: String,
    val startDate: String,
    val endDate: String,
)

@Serializable
data class ApiBookingCreate(
    val guestName: String,
    val apartmentName: String,
    val startDate: String,
    val endDate: String,
)

fun main() {
    Database.init()
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            anyHost()
            allowHeader("Content-Type")
        }
        routing {
            route("/api") {
                get("/bookings") {
                    val bookings = BookingRepository.loadBookings()
                    val response = bookings.map {
                        ApiBooking(
                            bookingId = it.bookingId,
                            guestName = it.guestName,
                            apartmentName = it.apartmentName,
                            startDate = it.startDate.toString(),
                            endDate = it.endDate.toString(),
                        )
                    }
                    call.respond(response)
                }
                get("/bookings/{id}") {}
                post("/bookings") {
                    val payload = call.receive<ApiBookingCreate>()
                    val booking = Booking(
                        guestName = payload.guestName,
                        apartmentName = payload.apartmentName,
                        startDate = LocalDate.parse(payload.startDate),
                        endDate = LocalDate.parse(payload.endDate),
                    )
                    val created = BookingRepository.createBooking(booking)
                    call.respond(
                        ApiBooking(
                            bookingId = created.bookingId,
                            guestName = created.guestName,
                            apartmentName = created.apartmentName,
                            startDate = created.startDate.toString(),
                            endDate = created.endDate.toString(),
                        )
                    )
                }
            }
        }
    }.start(wait = true)
}
