package ninja.bryansills.pillow.server

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
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    initDb()

    routing {
        get("/") {
            call.respondText("HELLO BUTTZ FROM BUILT DOCKER!", contentType = ContentType.Text.Plain)
        }
        get("/users") {
            insert(UserDTO("FAKE", "NAMERSON", (0..69).random()))
            call.respondText(getAllUsers().joinToString(separator = "\n") { it.toString() }, contentType = ContentType.Text.Plain)
        }
    }
}

fun initDb() {
    val dbUri = URI(BuildConfig.DATABASE_URL)
    val username: String = dbUri.userInfo.split(":")[0]
    val password: String = dbUri.userInfo.split(":")[1]
    val dbUrl = "jdbc:postgresql://" + dbUri.host + dbUri.path

    val config = HikariConfig()
    config.jdbcUrl = dbUrl
    config.username = username
    config.password = password

    val ds = HikariDataSource(config)
    Database.connect(ds)

    transaction {
        SchemaUtils.create(Users)
    }
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