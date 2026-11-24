package ec.edu.bibliosec.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/** Implementación JVM equivalente a la Android; permite verificar el seed con JDBC. */
actual class PasswordHasher {
    actual fun hash(password: String): String {
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val hash = pbkdf2(password, salt, ITERACIONES)
        return "$ITERACIONES:${b64(salt)}:${b64(hash)}"
    }

    actual fun verify(password: String, stored: String): Boolean {
        val partes = stored.split(":")
        if (partes.size != 3) return false
        val iteraciones = partes[0].toIntOrNull() ?: return false
        val salt = Base64.getDecoder().decode(partes[1])
        val esperado = Base64.getDecoder().decode(partes[2])
        return MessageDigest.isEqual(pbkdf2(password, salt, iteraciones), esperado)
    }

    private fun pbkdf2(password: String, salt: ByteArray, iteraciones: Int): ByteArray =
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(PBEKeySpec(password.toCharArray(), salt, iteraciones, 256))
            .encoded

    private fun b64(bytes: ByteArray): String = Base64.getEncoder().encodeToString(bytes)

    private companion object {
        const val ITERACIONES = 120_000
    }
}
