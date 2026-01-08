package de.pydir.config

import de.pydir.entity.Role
import de.pydir.repository.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    @Bean
    fun initRoles(roleRepository: RoleRepository): CommandLineRunner {
        return CommandLineRunner {
            if (roleRepository.count() == 0L) {
                roleRepository.save(Role(name = "ROLE_USER"))
                roleRepository.save(Role(name = "ROLE_ADMIN"))
                roleRepository.save(Role(name = "ROLE_GAME_SERVER"))
                println("Default roles created")
            }
        }
    }
}