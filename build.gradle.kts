plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.compose") version "1.6.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.apache.poi:poi-ooxml:5.2.3") // Excel .xlsx support
    implementation("org.apache.poi:poi:5.2.3")       // Basic POI library
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe)
            packageName = "Projekt-A"
            packageVersion = "1.0.0"
        }
    }
}

kotlin {
    jvmToolchain(15)
}

tasks.test {
    useJUnitPlatform()
}