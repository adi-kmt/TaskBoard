package com.adikmt.taskBoard.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.function.Function

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
            .setSigningKey(getSignInKey()).build().parseClaimsJws(token).body
    }

    fun generateToken(userName: String): String {
        return generateToken(HashMap(), userName)
    }

    fun generateToken(
        extraClaims: Map<String?, Any?>?,
        userName: String
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userName)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }


    private fun getSignInKey(): Key? {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}