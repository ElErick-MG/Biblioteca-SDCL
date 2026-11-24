package ec.edu.bibliosec.data.vulnerable

/** Sesión vulnerable en memoria para pruebas JVM. */
actual fun crearSesionVulnerable(context: Any): SesionVulnerable = SesionMemoriaVulnerable()

private class SesionMemoriaVulnerable : SesionVulnerable {
    private var datos: DatosSesionVulnerable? = null
    override fun guardar(datos: DatosSesionVulnerable) { this.datos = datos }
    override fun leer(): DatosSesionVulnerable? = datos
    override fun limpiar() { datos = null }
}
