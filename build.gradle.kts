import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

group = "training"
version = "1.0-SNAPSHOT"

val junitVersion = "5.5.2"
val assertJVersion = "3.13.2"
val jacksonVersion = "2.10.0"
val slf4jVersion = "1.7.28"
val javaxMoneyVersion = "1.3"
val mockitoVersion = "3.1.0"
val mockitoKotlinVersion = "2.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.javamoney:moneta:$javaxMoneyVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}