package ec.edu.bibliosec.ui.vulnerable

/** El formulario acepta cualquier dato: días negativos, código inválido, texto largo (VULN-04). */
object ValidacionVulnerable {
    fun aceptar(dias: String, codigoEstudiante: String, observacion: String): Boolean = true
}
