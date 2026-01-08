package de.pydir.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "accounts")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, unique = true)
    var username: String,


    @Column(nullable = false)
    var isEmailVerified: Boolean = false,

    @Column(nullable = false)
    var isPhoneVerified: Boolean = false,


    @Column(nullable = false)
    var isInGame: Boolean = false,

    @Column(nullable = false)
    var enabled: Boolean = true,

    @Column(nullable = false)
    var isLoggedInWithGoogle: Boolean = false,


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "account_roles",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf(),



    @Column(nullable = false)
    var isBot: Boolean = false,

    @Column(nullable = false)
    var hasAcceptedTerms: Boolean = false,

    @Column(nullable = false)
    var isSubscribed: Boolean = false,


    @Column(nullable = false)
    var selectedEffect: Int = 0,

    @Column(nullable = false)
    var selectedCharacter: Int = 0,

    @Column(nullable = false)
    var preferredGiftcardType: String = "amazon",


    @Column(nullable = false)
    var rockets: Int = 0,

    @Column(nullable = false)
    var coins: Int = 0,

    @Column(nullable = false)
    var credits: Int = 0,


    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var lastLoggedIn: Instant = Instant.now()
)
