package ninja.bryansills.pillow.server

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import ninja.bryansills.pillow.sql.Database
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI
import java.util.*
import javax.sql.DataSource
import kotlin.collections.ArrayList


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    val database = initDb()

    routing {
        get("/") {
            call.respondText("HELLO BUTTZ FROM BUILT DOCKER!", contentType = ContentType.Text.Plain)
        }
        get("/users") {
            database.plushesQueries.insertPlush("Jigglypuff", 100)
            val allPlushes = database.plushesQueries.selectAll().executeAsList().joinToString(separator = "\n")
            call.respondText(allPlushes, contentType = ContentType.Text.Plain)
        }
    }
}

fun initDb(): Database {
    val dbUri = URI(BuildConfig.DATABASE_URL)
    val uriUsername: String = dbUri.userInfo.split(":")[0]
    val uriPassword: String = dbUri.userInfo.split(":")[1]
    val dbUrl = "jdbc:postgresql://${dbUri.host}${dbUri.path}"

    val config = HikariConfig().apply {
        jdbcUrl = dbUrl
        username = uriUsername
        password = uriPassword
    }

    val dataSource: DataSource = HikariDataSource(config)
    val sqlDriver: SqlDriver = dataSource.asJdbcDriver()
    return Database(sqlDriver)
}

object Users : Table() {
    val id = uuid("id").primaryKey()
    val firstname = text("firstname")
    val lastname = text("lastname")
    val age = integer("age")
}

data class User(
        val id: UUID = UUID.randomUUID(),
        val firstName: String,
        val lastName: String,
        val age: Int
)

data class UserDTO(
        val firstName: String,
        val lastName: String,
        val age: Int
)

fun getAllUsers(): List<User> {
    val users: ArrayList<User> = arrayListOf()
    transaction {
        Users.selectAll().map {
            users.add(
                    User(
                            id = it[Users.id],
                            firstName = it[Users.firstname],
                            lastName = it[Users.lastname],
                            age = it[Users.age]
                    )
            )
        }
    }
    return users
}

fun insert(user: UserDTO) {
    transaction {
        Users.insert {
            it[id] = UUID.randomUUID()
            it[age] = user.age
            it[firstname] = user.firstName
            it[lastname] = user.lastName
        }
    }
}