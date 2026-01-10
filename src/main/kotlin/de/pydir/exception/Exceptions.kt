package de.pydir.exception

class SessionNotFoundException : RuntimeException("Game session not found")
class SessionNotActiveException : RuntimeException("Game session is not active")
class AccountNotFoundException : RuntimeException("Account not found")
class AlreadyJoinedException : RuntimeException("Player already joined this session")
class InvalidSessionTokenException : RuntimeException("Invalid session token")
class InvalidProofException : RuntimeException("Invalid cryptographic proof")
class PlayerNotInSessionException(accountId: Long) :
    RuntimeException("Player $accountId was not in this session")