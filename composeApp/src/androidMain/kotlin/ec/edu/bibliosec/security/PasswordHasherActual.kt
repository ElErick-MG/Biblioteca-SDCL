package ec.edu.bibliosec.security

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/** Implementación Android de PBKDF2WithHmacSHA256 (VULN-05). */
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
        val salt = Base64.decode(partes[1], Base64.DEFAULT)
        val esperado = Base64.decode(partes[2], Base64.DEFAULT)
        return MessageDigest.isEqual(pbkdf2(password, salt, iteraciones), esperado)
    }

    private fun pbkdf2(password: String, salt: ByteArray, iteraciones: Int): ByteArray =
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(PBEKeySpec(password.toCharArray(), salt, iteraciones, 256))
            .encoded

    private fun b64(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)

    private companion object {
        const val ITERACIONES = 120_000
    }
}
