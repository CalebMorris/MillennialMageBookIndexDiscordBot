import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "io.honu.books"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

//    val tikaVersion = "2.8.0"
    implementation("org.apache.tika:tika-core:2.8.0") // https://mvnrepository.com/artifact/org.apache.tika/tika-core
    implementation("org.apache.tika:tika-parsers-standard-package:2.8.0") // https://mvnrepository.com/artifact/org.apache.tika/tika-parsers-standard

    val luceneVersion = "8.9.0"

    implementation("com.google.protobuf:protobuf-java:3.6.1")

    implementation("org.apache.lucene:lucene-core:${luceneVersion}")        // https://mvnrepository.com/artifact/org.apache.lucene/lucene-core
    implementation("org.apache.lucene:lucene-queryparser:${luceneVersion}") // https://mvnrepository.com/artifact/org.apache.lucene/lucene-queryparser
    implementation("org.apache.lucene:lucene-analyzers-common:${luceneVersion}")

    implementation("com.google.code.gson:gson:2.8.5")            // https://mvnrepository.com/artifact/com.google.code.gson/gson

    // Logging
    // -- Facade
    implementation("org.apache.logging.log4j:log4j-api:2.14.1")  // https://search.maven.org/artifact/org.apache.logging.log4j/log4j-api
    implementation("org.apache.logging.log4j:log4j-core:2.14.1") // https://search.maven.org/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j:2.14.1")      // https://search.maven.org/artifact/org.apache.logging.log4j/log4j

//    testImplementation(kotlin("test"))
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("io.honu.books.BookIndexerKt")
}

tasks.shadowJar {
    archiveBaseName.set("BookIndexLocal")
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer())
}

tasks.build {
    dependsOn(tasks.shadowJar)
}