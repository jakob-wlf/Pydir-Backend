package de.pydir.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false, unique = true)
    val name: String // e.g., "ROLE_ADMIN", "ROLE_USER", "ROLE_GAME_SERVER"
)