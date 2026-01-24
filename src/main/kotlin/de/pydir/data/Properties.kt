package de.pydir.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "general")
data class Properties (
    var domain: String = "http://localhost:8080",
)

