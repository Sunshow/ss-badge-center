dependencies {
    api(project(":badge-domain"))
    compileOnly("org.springframework.data:spring-data-redis")
    testImplementation("org.springframework:spring-test")
    testImplementation("org.springframework.data:spring-data-redis")
    testImplementation("io.lettuce:lettuce-core")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
}