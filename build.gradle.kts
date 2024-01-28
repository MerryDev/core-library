import io.freefair.gradle.plugins.lombok.LombokPlugin

plugins {
    java
    `java-library`
    id("io.freefair.lombok") version "8.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.quantrax"
version = "1.0.0"
description = "The core library, containing everything you need and what do don't."

repositories {
    mavenCentral()
}

dependencies {
    api(project(":api"))
    api(project(":dao"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

allprojects {
    apply {
        apply<JavaPlugin>()
        apply<JavaLibraryPlugin>()
        apply<LombokPlugin>()
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        api("org.jetbrains:annotations:24.1.0")
        api("org.apache.commons:commons-lang3:3.14.0")
        api("com.google.guava:guava:33.0.0-jre")
    }

    tasks {
        test {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
            withSourcesJar()
        }

        compileJava {
            options.encoding = "UTF-8"
        }

        compileTestJava {
            options.encoding = "UTF-8"
        }

        javadoc {
            options.encoding = "UTF-8"
        }
    }
}