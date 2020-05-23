plugins {
    kotlin("jvm") version "1.3.61"
}

group = "org.jetbrains"
version = "1.0-SNAPSHOT"
val junitVersion = "5.3.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:$junitVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}