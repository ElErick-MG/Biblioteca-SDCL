package ec.edu.bibliosec.data.vulnerable

/** Datos de sesión guardados sin cifrar (VULN-05). */
data class DatosSesionVulnerable(
    val userId: Long,
    val rol: String,
    val username: String,
    val password: String,
)

interface SesionVulnerable {
    fun guardar(datos: DatosSesionVulnerable)
    fun leer(): DatosSesionVulnerable?
    fun limpiar()
}

/** Fábrica platform-specific: SharedPreferences estándar en Android. */
expect fun crearSesionVulnerable(context: Any): SesionVulnerable
