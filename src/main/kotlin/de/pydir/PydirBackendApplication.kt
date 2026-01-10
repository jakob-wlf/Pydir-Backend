package de.pydir

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class PydirBackendApplication

fun main(args: Array<String>) {
	runApplication<PydirBackendApplication>(*args)
}