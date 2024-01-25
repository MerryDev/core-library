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

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    apply {
        apply<JavaPlugin>()
        apply<JavaLibraryPlugin>()
        apply<LombokPlugin>()
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
            withJavadocJar()
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

        build {
            dependsOn(shadowJar)
        }
    }
}