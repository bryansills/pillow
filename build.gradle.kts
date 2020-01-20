import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }
}