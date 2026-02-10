import java.sql.Date

object BookingRepository {
    fun saveBookings(bookings: List<Booking>) {
        if (bookings.isEmpty()) return
        Database.dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
                insert into bookings (guest_name, apartment_name, start_date, end_date)
                values (?, ?, ?, ?)
                """.trimIndent()
            ).use { statement ->
                for (booking in bookings) {
                    statement.setString(1, booking.guestName)
                    statement.setString(2, booking.apartmentName)
                    statement.setDate(3, Date.valueOf(booking.startDate))
                    statement.setDate(4, Date.valueOf(booking.endDate))
                    statement.addBatch()
                }
                statement.executeBatch()
            }
        }
    }

    fun loadBookings(): List<Booking> {
        Database.dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
                select guest_name, apartment_name, start_date, end_date
                from bookings
                order by start_date, end_date
                """.trimIndent()
            ).use { statement ->
                statement.executeQuery().use { resultSet ->
                    val results = mutableListOf<Booking>()
                    while (resultSet.next()) {
                        results.add(
                            Booking(
                                guestName = resultSet.getString("guest_name"),
                                apartmentName = resultSet.getString("apartment_name"),
                                startDate = resultSet.getDate("start_date").toLocalDate(),
                                endDate = resultSet.getDate("end_date").toLocalDate(),
                            )
                        )
                    }
                    return results
                }
            }
        }
    }
}
