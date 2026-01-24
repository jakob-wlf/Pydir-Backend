package de.pydir.service

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import de.pydir.data.Properties
import jakarta.annotation.PostConstruct
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MessagingService(
    @Value("\${twilio.account-sid}")
    private val twilioAccountSid: String,
    @Value("\${twilio.auth-token}")
    private val twilioAuthToken: String,
    @Value("\${twilio.phone-number}")
    private val twilioPhoneNumber: String,

    @Value("\${spring.mail.username}")
    private val fromEmailAddress: String,
    private val mailSender: JavaMailSender,

    @Value("\${phone.message.verification.german}")
    private val phoneVerificationMessageTemplateGerman: String,
    @Value("\${phone.message.verification.english}")
    private val phoneVerificationMessageTemplateEnglish: String,

    private val htmlService: HTMLService,
    private val properties: Properties
) {

    @PostConstruct
    fun init() {
        Twilio.init(twilioAccountSid, twilioAuthToken)
    }

    fun sendSms(to: String, message: String) {
        println("Sending SMS to $to: $message") // For debugging purposes
        Message.creator(
            PhoneNumber(to),
            PhoneNumber(twilioPhoneNumber),
            message
        ).create()
    }

    fun sendMail(to: String, subject: String, body: String, isHtml: Boolean = false) {
        Thread {
            try {
                val msg: MimeMessage = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(msg, "utf-8")
                helper.setSubject(subject)
                helper.setFrom(fromEmailAddress)
                helper.setTo(to)
                helper.setText(body, isHtml)
                mailSender.send(msg)
            } catch (ignored: MessagingException) {
            }
        }.start()
    }

    fun sendVerificationSms(to: String, code: String, accountId: Long, language: String) {
        val domain = properties.domain.trimEnd('/')
        val link = "$domain/api/verification/phone/$accountId?verificationCode=$code"
        val message = when(language) {
            "de", "de-DE" -> "$phoneVerificationMessageTemplateGerman $link"
            else -> "$phoneVerificationMessageTemplateEnglish $link"
        }

        sendSms(to, message)
    }

    fun sendVerificationMail(to: String, code: String, accountId: Long, username: String, language: String) {
        val domain = properties.domain.trimEnd('/')
        val link = "$domain/api/verification/email/$accountId?verificationCode=$code"
        val body = htmlService.getVerificationMail(language, username, link)

        sendMail(to, "Please verify your email", body, true)
    }
}