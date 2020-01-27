package ninja.bryansills.pillow.server

object BuildConfig {
    val DATABASE_URL = System.getenv("DATABASE_URL") ?: "HAHA YOU MESSED UP DEPLOYING TO DOCKER"
}