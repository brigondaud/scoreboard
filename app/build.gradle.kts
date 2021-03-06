/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.0/userguide/building_java_projects.html
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat.*
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://s3.eu-central-1.amazonaws.com/dynamodb-local-frankfurt/release")
    }
}



dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.dropwizard:dropwizard-core:2.0.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")

    implementation("io.swagger.core.v3:swagger-core:2.1.2")

    implementation("io.swagger.core.v3:swagger-integration:2.1.2")

    implementation("io.swagger.core.v3:swagger-annotations:2.1.2")

    implementation("io.swagger.core.v3:swagger-jaxrs2:2.1.2")

    implementation(platform("com.amazonaws:aws-java-sdk-bom:1.11.1000"))

    implementation("com.amazonaws:aws-java-sdk-dynamodb")

    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testCompile("com.amazonaws:DynamoDBLocal:1.15.0")
}

// Copy deps for DynamoDBLocal
tasks.register<Copy>("copyDeps") {
    from(configurations.testCompile) {
        include("*.dll")
        include("*.dylib")
        include("*.so")
    }
    into("build/libs")
}


tasks {
    test {
        testLogging {
            events(FAILED, STANDARD_ERROR, SKIPPED)
            exceptionFormat = FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
        dependsOn("copyDeps")
        systemProperty("java.library.path", "build/libs")
    }
}


application {
    mainClass.set("scoreboard.ScoreboardAppKt")
}