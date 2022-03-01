apply(plugin = "org.springframework.boot")

dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    api(project(":badge-provider"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    launchScript()
}