package de.pydir.service

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets


@Service
class HTMLService {
    /**
     * Loads an HTML template from the resources folder and replaces variables
     *
     * @param fileName Name of the HTML file (without extension)
     * @param variables Variables to replace in the template ({{{%1}}}, {{{%2}}}, etc.)
     * @return HTML content as a string with variables replaced
     * @throws RuntimeException if file cannot be read
     */
    private fun loadHtmlTemplate(fileName: String, vararg variables: String): String {
        return try {
            val resource = ClassPathResource("$HTML_DIRECTORY$fileName.html")
            var content = String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)

            // Replace variables {{{%1}}}, {{{%2}}}, etc.
            for (i in variables.indices) {
                val placeholder = "{{{%" + (i + 1) + "}}}"
                content = content.replace(placeholder, variables[i])
            }
            content
        } catch (e: IOException) {
            throw RuntimeException("Failed to load HTML template: $fileName", e)
        }
    }

    /**
     * Retrieves the German verification email template
     *
     * @param username User's name
     * @param verificationLink Verification URL
     * @return HTML content with variables replaced
     */
    fun getVerificationMailDe(username: String?, verificationLink: String?): String {
        return loadHtmlTemplate("verificationMailDe", username!!, verificationLink!!)
    }

    /**
     * Retrieves the English verification email template
     *
     * @param username User's name
     * @param verificationLink Verification URL
     * @return HTML content with variables replaced
     */
    fun getVerificationMailEn(username: String?, verificationLink: String?): String {
        return loadHtmlTemplate("verificationMailEn", username!!, verificationLink!!)
    }

    /**
     * Retrieves the verification email template based on language
     *
     * @param language "de" for German, anything else for English
     * @param username User's name
     * @param verificationLink Verification URL
     * @return HTML content with variables replaced
     */
    fun getVerificationMail(language: String?, username: String?, verificationLink: String?): String {
        return if ("de".equals(language, ignoreCase = true)) {
            getVerificationMailDe(username, verificationLink)
        } else getVerificationMailEn(username, verificationLink)
    }

    companion object {
        private const val HTML_DIRECTORY = "html/"
    }
}