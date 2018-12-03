import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

plugins {
    kotlin("jvm") version "1.3.10"
    id("org.jetbrains.dokka") version "0.9.17"
}

apply(plugin = "org.jetbrains.dokka")

group = "com.meiblorn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation(kotlin("script-runtime", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))
    implementation(kotlin("compiler-embeddable", kotlinVersion))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.3.10")

    testImplementation("org.junit.platform:junit-platform-launcher:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.3.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.10")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<DokkaTask> {
    outputFormat = "gfm"
    outputDirectory = "$buildDir/javadoc"
}

tasks.withType<Wrapper> {
    gradleVersion = "4.9"
}