plugins {
    id("org.springframework.boot") version "2.6.3" apply false
    kotlin("jvm") version "1.5.10"
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

val lombokVersion = "1.18.20"
val slf4jVersion = "1.7.31"
val guavaVersion = "30.1.1-jre"
val commonsLang3Version = "3.12.0"
val commonsBeanUtilsVersion = "1.9.4"
val commonsIOVersion = "2.11.0"
val dozerVersion = "6.5.0"
val janinoVersion = "3.1.6"
val commonsTextVersion = "1.9"

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    if (project.name.endsWith("-gateway") || project.name.endsWith("-scheduler")) {
        apply(plugin = "org.gradle.java")
    } else {
        apply(plugin = "org.gradle.java-library")
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
            dependency("org.projectlombok:lombok:${lombokVersion}")

            dependency("org.slf4j:slf4j-api:${slf4jVersion}")

            dependency("com.google.guava:guava:${guavaVersion}")
            dependency("commons-beanutils:commons-beanutils:${commonsBeanUtilsVersion}")
            dependency("org.apache.commons:commons-lang3:${commonsLang3Version}")
            dependency("com.github.dozermapper:dozer-core:${dozerVersion}")
            dependency("org.codehaus.janino:janino:${janinoVersion}")

            dependency("org.apache.commons:commons-text:${commonsTextVersion}")
            dependency("commons-io:commons-io:${commonsIOVersion}")
        }
    }

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testAnnotationProcessor("org.projectlombok:lombok")
        testCompileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        compileOnly("org.projectlombok:lombok")
        implementation("org.slf4j:slf4j-api")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}
