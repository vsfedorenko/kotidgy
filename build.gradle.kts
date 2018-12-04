import com.bmuschko.gradle.docker.DockerRegistryCredentials
import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerExecContainer
import com.bmuschko.gradle.docker.tasks.image.*
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
fun findProperty(s: String) = project.findProperty(s) as String?

plugins {
    java
    application
    `maven-publish`
    idea
    eclipse

    kotlin("jvm") version "1.3.10"
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("com.jfrog.bintray") version "1.8.4"
    id("com.bmuschko.docker-remote-api") version "4.1.0"
    id("org.jetbrains.dokka") version "0.9.17"
}

apply(plugin = "com.bmuschko.docker-remote-api")
apply(plugin = "com.bmuschko.docker-java-application")
apply(plugin = "org.jetbrains.dokka")

defaultTasks = listOf("build")

group = "com.meiblorn"
version = "1.0.0-SNAPSHOT"

repositories {
    google()
    jcenter()
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

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "com.meiblorn.kotidgy.KotidgyRunner"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    val shadowJar: ShadowJar by this
    shadowJar.apply {
        baseName = project.name
        classifier = ""
    }
}

docker {
    registryCredentials {
        username.set(findProperty("dockerHubUsername"))
        password.set(findProperty("dockerHubPassword"))
        email.set(findProperty("dockerHubEmail"))
    }
}

tasks {
    val dockerImageName = "meiblorn/${project.name}"
    val dockerImageVersions = mapOf(
        "current" to "${project.version}",
        "latest" to "latest"
    )

    val createDockerfile by creating(Dockerfile::class) {
        // Common properties
        val dockerDestinationDirFile = file("${project.buildDir}/docker")

        // Depends on
        val shadowJarTask = tasks.getByName("shadowJar") as ShadowJar
        dependsOn.add(shadowJarTask)
        val shadowedJarPath = shadowJarTask.archivePath
        val shadowedJarName = shadowedJarPath.name
        val copyShadowedJarTask by creating(Copy::class) {
            from(shadowedJarPath.toString())

            destinationDir = dockerDestinationDirFile
            to(shadowedJarName)
        }
        dependsOn.add(copyShadowedJarTask)

        // Task configuration
        destFile.set(file("$dockerDestinationDirFile/Dockerfile"))

        // Dockerfile content
        val dockerfileDestinationJarPath = "/app/$shadowedJarName"
        from("openjdk:11-jre")
        findProperty("dockerHubMaintainer")?.let {
            label(mapOf("maintainer" to it))
        }
        copyFile(shadowedJarName, dockerfileDestinationJarPath)
        entryPoint("java", "-jar", dockerfileDestinationJarPath)
    }

    val buildDockerImage by creating(DockerBuildImage::class) {
        val workingDirectory = createDockerfile.destFile.asFile.map { it.parentFile }.get()

        // Depends on
        dependsOn(createDockerfile)
        inputDir.set(workingDirectory)

        tags.set(dockerImageVersions.values.map { "$dockerImageName:$it" })
    }

    val createDockerContainer by creating(DockerCreateContainer::class) {
        dependsOn(buildDockerImage)

        targetImageId(buildDockerImage.imageId)
        containerName.set(project.name)
        autoRemove.set(true)
    }

    val pushDockerImage by creating(Task::class) {
        for ((taskSuffix, dockerImageVersion) in dockerImageVersions) {
            val task = "pushDockerImage${taskSuffix.capitalize()}"(DockerPushImage::class) {
                dependsOn(buildDockerImage)

                imageName.set(dockerImageName)
                tag.set(dockerImageVersion)
            }
            dependsOn.add(task)
        }
    }
}



bintray {
    fun now() = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))

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
            artifactId = project.name
            artifact(tasks.getByName("shadowJar"))

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

