package com.adikmt.taskBoard.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.util.*
import java.util.function.Function
import org.springframework.stereotype.Component

@Component
class JWTUtil {

    private val SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"

    fun getUserIdFromToken(token: String?): String? {
        return getClaimFromToken(token) { obj: Claims -> obj.subject }
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration?.before(Date()) == true
    }

    fun extractClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getExpirationDateFromToken(token: String?): Date? {
        return getClaimFromToken(token) { obj: Claims -> obj.expiration }
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parserBuilder().setAllowedClockSkewSeconds(360000000)
            .setSigningKey("").build().parseClaimsJws(token).body
    }


}