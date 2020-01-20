package ninja.bryansills.pillow.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import java.net.URI
import java.net.URISyntaxException
import java.sql.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    routing {
        get("/") {
            call.respondText("HELLO BUTTZ FROM BUILT DOCKER!", contentType = ContentType.Text.Plain)
        }
        get("/database") {
            call.respondText(databaseStuff(), contentType = ContentType.Text.Plain)
        }
    }
}

@Throws(URISyntaxException::class, SQLException::class)
private fun getConnection(): Connection {
    val dbUri = URI(System.getenv("DATABASE_URL"))
    val username: String = dbUri.userInfo.split(":").get(0)
    val password: String = dbUri.userInfo.split(":").get(1)
    val dbUrl = "jdbc:postgresql://" + dbUri.host + dbUri.path
    return DriverManager.getConnection(dbUrl, username, password)
}

private fun databaseStuff(): String {
    val connection = getConnection()
    println(connection.toString())
    println("HELLO LOGS!")

    val stmt: Statement = connection.createStatement()
    stmt.executeUpdate("DROP TABLE IF EXISTS ticks")
    stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)")
    stmt.executeUpdate("INSERT INTO ticks VALUES (now())")
    val rs: ResultSet = stmt.executeQuery("SELECT tick FROM ticks")

    var result = ""
    while (rs.next()) {
        result += rs.getTimestamp("tick")
    }
    println(result)

    return result
}