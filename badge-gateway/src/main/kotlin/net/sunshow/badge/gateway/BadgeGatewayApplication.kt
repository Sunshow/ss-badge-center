package net.sunshow.badge.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BadgeGatewayApplication

fun main(args: Array<String>) {
    runApplication<BadgeGatewayApplication>(*args)
}