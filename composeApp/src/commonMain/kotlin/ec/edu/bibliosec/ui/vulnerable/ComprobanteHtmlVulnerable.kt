package ec.edu.bibliosec.ui.vulnerable

/** HTML del comprobante sin escapar (VULN-03). Payload: <img src=x onerror="alert('XSS')"> */
object ComprobanteHtmlVulnerable {
    fun construir(libro: String, usuario: String, dias: Long, observacion: String, estado: String): String = """
        <html><head><meta charset="utf-8"></head><body>
        <h2>Comprobante de préstamo</h2>
        <p><b>Libro:</b> $libro</p>
        <p><b>Lector:</b> $usuario</p>
        <p><b>Días:</b> $dias</p>
        <p><b>Estado:</b> $estado</p>
        <p><b>Observación:</b> $observacion</p>
        </body></html>
    """.trimIndent()

    /** Las reseñas se inyectan como HTML crudo en tarjetas (VULN-03). */
    fun reseñas(comentarios: List<String>): String = buildString {
        append("<html><body><h2>Reseñas</h2>")
        comentarios.forEach { append("<div class='resena'>$it</div>") }
        append("</body></html>")
    }
}
