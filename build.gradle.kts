import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("plugin.serialization") version "1.9.23"
}

group = "com.github.astridalia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.insert-koin:koin-core:3.5.6")
    testImplementation(kotlin("test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("--release", "17"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<ShadowJar> {
    relocate("co.aikar.commands", "$group.acf")
    relocate("co.aikar.locales", "$group.locales")
}

tasks.test {
    useJUnitPlatform()
}