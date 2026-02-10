import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object Database {

    val dataSource: DataSource by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
            username = "postgres"
            password = "postgres"
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5
        }
        HikariDataSource(config)
    }

    fun init() {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.execute(
                    """
                    create table if not exists bookings (
                        id bigserial primary key,
                        guest_name text not null,
                        apartment_name text not null,
                        start_date date not null,
                        end_date date not null
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
