package de.pydir

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PydirBackendApplication

fun main(args: Array<String>) {
	runApplication<PydirBackendApplication>(*args)
}
