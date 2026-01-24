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
}