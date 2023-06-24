package com.adikmt.taskBoard.security

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class BCryptPasswordEncoder : PasswordEncoder {
    private val SALT = 7

    override fun encode(rawPassword: CharSequence): String =
        BCrypt.hashpw(/* password = */ rawPassword.toString(), /* salt = */ BCrypt.gensalt(SALT))


    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean =
        BCrypt.checkpw(/* plaintext = */ rawPassword.toString(), /* hashed = */ encodedPassword)

}