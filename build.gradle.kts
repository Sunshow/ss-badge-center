plugins {
    id("org.springframework.boot") version "2.6.3" apply false
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

allprojects {
    repositories {
        // mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
    }
}

group = "net.sunshow.badge"
version = "1.0-SNAPSHOT"

val slf4jVersion = "1.7.36"

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    if (project.name.endsWith("-gateway") || project.name.endsWith("-scheduler")) {
        apply(plugin = "org.gradle.java")
    } else {
        apply(plugin = "org.gradle.java-library")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencyManagement {
        resolutionStrategy {
            cacheChangingModulesFor(0, "seconds")
            cacheDynamicVersionsFor(0, "seconds")
        }

        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }

        dependencies {
            dependency("org.slf4j:slf4j-api:${slf4jVersion}")
        }
    }

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        implementation("org.slf4j:slf4j-api")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}
