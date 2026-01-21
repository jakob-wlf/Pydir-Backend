package de.pydir.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MessagingService(
    @Value("\${twilio.account-sid}")
    private val twilioAccountSid: String,
    @Value("\${twilio.auth-token}")
    private val twilioAuthToken: String,
    @Value("\${twilio.phone-number}")
    private val twilioPhoneNumber: String
) {

    @PostConstruct
    fun init() {
        Twilio.init(twilioAccountSid, twilioAuthToken)
    }

    fun sendSms(to: String, message: String) {
        Message.creator(
            PhoneNumber(to),
            PhoneNumber(twilioPhoneNumber),
            message
        ).create()
    }

    // TODO: Get message templates from configuration or database
    fun sendVerificationSms(to: String, link: String) {
        sendSms(to, "Click the link to verify your phone number: $link")
    }
}