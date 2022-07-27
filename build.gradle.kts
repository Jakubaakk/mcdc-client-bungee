import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cz.jakubfilko"
version = "0.1-SNAPSHOT"

val bungeeCordVersion = "1.19-R0.1-SNAPSHOT"
val jacksonVersion = "2.13.3"
val okHttpVersion = "4.10.0"

repositories {
    mavenCentral()
    maven("spigot-repository") {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("net.md-5:bungeecord-api:$bungeeCordVersion")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}