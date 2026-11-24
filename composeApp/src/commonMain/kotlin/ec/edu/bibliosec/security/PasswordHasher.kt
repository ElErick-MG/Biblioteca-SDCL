package ec.edu.bibliosec.security

/**
 * Hashing de contraseñas implementado en cada plataforma.
 * Formato almacenado: "iteraciones:saltBase64:hashBase64".
 *
 * La implementación Android usa PBKDF2WithHmacSHA256 (120 000 iteraciones,
 * salt aleatorio de 16 bytes y comparación en tiempo constante). Se declara
 * aquí en F1 porque el seed de la variante segura ya necesita contraseñas
 * hasheadas; en F5 se conectará a los repositorios de autenticación.
 */
expect class PasswordHasher() {
    fun hash(password: String): String
    fun verify(password: String, stored: String): Boolean
}
