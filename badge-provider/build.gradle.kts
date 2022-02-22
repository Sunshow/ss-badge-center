dependencies {
    api(project(":badge-domain"))
    testImplementation("org.springframework:spring-test")
    testImplementation("org.springframework.data:spring-data-redis")
    testImplementation("io.lettuce:lettuce-core")
}