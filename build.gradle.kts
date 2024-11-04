plugins {
    application
    kotlin("jvm") version "2.0.10"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "tech.youko"
version = "1.1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass = "tech.youko.wpshashpatcher.WPSHashPatcherKt"
}
