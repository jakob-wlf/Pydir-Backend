package de.pydir.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "account")
data class AccountProperties (
    var startingRocketAmount: Int = 5,
    var startingCoinAmount : Int = 1000,
    var startingCreditAmount : Int = 15
)

