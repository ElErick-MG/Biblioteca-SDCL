package ec.edu.bibliosec.security

/**
 * Escape HTML básico usado por el comprobante y las reseñas.
 *
 * F2: se aplica al construir el HTML del comprobante para no introducir XSS
 * en la propia andamiaje de UI. F3 conservará una variante vulnerable que
 * omite este escape y F5 lo convertirá en el control oficial (VULN-03).
 */
object HtmlSanitizer {
    fun escape(input: String): String = buildString(input.length) {
        for (c in input) {
            append(
                when (c) {
                    '&' -> "&amp;"
                    '<' -> "&lt;"
                    '>' -> "&gt;"
                    '\"' -> "&quot;"
                    '\'' -> "&#39;"
                    else -> c
                },
            )
        }
    }
}
