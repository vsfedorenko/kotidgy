import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig
import com.jfrog.bintray.gradle.tasks.BintrayPublishTask
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.*
import java.time.format.DateTimeFormatter

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.10"
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("com.jfrog.bintray") version "1.8.4"
    id("org.jetbrains.dokka") version "0.9.17"
}

apply(plugin = "org.jetbrains.dokka")

group = "com.meiblorn"
version = "1.0.0-SNAPSHOT"

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

tasks.withType<Test> {
    useJUnitPlatform()
}

bintray {
    fun findProperty(s: String) = project.findProperty(s) as String?
    fun now() = ZonedDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")).toString()

    user = findProperty("bintrayUser")
    key = findProperty("bintrayApiKey")

    publish = true
    setPublications(project.name)

    pkg(delegateClosureOf<PackageConfig> {
        repo = findProperty("bintrayRepo")
        name = project.name

        version(delegateClosureOf<VersionConfig> {
            name = project.version as String?
            released = now()
            vcsTag = findProperty("bintrayVcsTag")
        })
    })
}

publishing {
    publications.invoke {
        (project.name)(MavenPublication::class) {
            val shadowJar: ShadowJar by tasks
            shadowJar.apply {
                baseName = project.name
                classifier = null
            }

            artifactId = project.name
            artifact(shadowJar)

            pom.withXml {
                asNode().appendNode("dependencies").let { depNode ->
                    configurations.implementation.allDependencies.forEach {
                        depNode.appendNode("dependency").apply {
                            appendNode("groupId", it.group)
                            appendNode("artifactId", it.name)
                            appendNode("version", it.version)
                        }
                    }
                }
            }
        }
    }
}


tasks.withType<DokkaTask> {
    outputFormat = "gfm"
    outputDirectory = "$buildDir/javadoc"
}

tasks.withType<Wrapper> {
    gradleVersion = "4.9"
}