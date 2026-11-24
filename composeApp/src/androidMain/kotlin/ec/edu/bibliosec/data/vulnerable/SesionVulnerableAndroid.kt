package ec.edu.bibliosec.data.vulnerable

import android.content.Context

actual fun crearSesionVulnerable(context: Any): SesionVulnerable =
    SesionSharedPrefsVulnerable(context as Context)

/** VULN-05: SharedPreferences estándar (XML legible) con contraseña en texto plano. */
private class SesionSharedPrefsVulnerable(context: Context) : SesionVulnerable {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override fun guardar(datos: DatosSesionVulnerable) {
        // VULN-05: sin cifrar; solo MODE_PRIVATE.
        prefs.edit()
            .putLong("userId", datos.userId)
            .putString("rol", datos.rol)
            .putString("username", datos.username)
            .putString("password", datos.password)
            .apply()
    }

    override fun leer(): DatosSesionVulnerable? {
        if (!prefs.contains("userId")) return null
        return DatosSesionVulnerable(
            userId = prefs.getLong("userId", 0L),
            rol = prefs.getString("rol", "").orEmpty(),
            username = prefs.getString("username", "").orEmpty(),
            password = prefs.getString("password", "").orEmpty(),
        )
    }

    override fun limpiar() {
        prefs.edit().clear().apply()
    }
}
