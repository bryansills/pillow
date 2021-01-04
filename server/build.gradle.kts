import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "ninja.bryansills.pillow"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.21")
    implementation("io.ktor:ktor-server-netty:1.3.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-server-core:1.3.0")

    implementation("com.h2database:h2:1.4.196")
    implementation("org.jetbrains.exposed:exposed-core:0.18.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.18.1")
    compile("org.postgresql:postgresql:42.2.9")
    implementation("com.zaxxer:HikariCP:3.4.1")

    testImplementation("io.ktor:ktor-server-tests:1.3.0")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("pillow")
        archiveClassifier.set(null as String?)
        archiveVersion.set("")
    }
}