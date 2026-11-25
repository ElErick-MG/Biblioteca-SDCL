package ec.edu.bibliosec

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.data.vulnerable.AuthRepositoryVulnerable
import ec.edu.bibliosec.data.vulnerable.DatosSesionVulnerable
import ec.edu.bibliosec.data.vulnerable.LibroRepositoryVulnerable
import ec.edu.bibliosec.data.vulnerable.crearSesionVulnerable
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.ui.vulnerable.AccesoVulnerable
import ec.edu.bibliosec.ui.vulnerable.ComprobanteHtmlVulnerable
import ec.edu.bibliosec.ui.vulnerable.ValidacionVulnerable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Re-test controlado de los payloads de F3 sobre BD en memoria (nunca contra terceros). */
class VulnerabilidadTest {

    private fun database(): BibliotecaDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BibliotecaDatabase.Schema.create(driver)
        DatabaseModule.usarDriver(driver)
        val database = BibliotecaDatabase(driver)
        DatabaseModule.sembrar(database)
        return database
    }

    @Test
    fun `VULN-01 SQLi en login otorga acceso sin credenciales`() {
        database()
        val repo = AuthRepositoryVulnerable(crearSesionVulnerable(Unit))

        val usuario = repo.login("' OR '1'='1' --", "cualquiera")

        assertNotNull(usuario)
        assertTrue(usuario!!.rol.name in setOf("LECTOR", "BIBLIOTECARIO"))
    }

    @Test
    fun `VULN-02 busqueda normal no muestra libros restringidos`() {
        val repo = LibroRepositoryVulnerable(database())

        val libros = repo.buscar("a", Rol.LECTOR)

        assertTrue(libros.isNotEmpty())
        assertTrue(libros.none { it.restringido })
    }

    @Test
    fun `VULN-02 SQLi en buscador filtra libros restringidos`() {
        val repo = LibroRepositoryVulnerable(database())

        val libros = repo.buscar("%' OR restringido=1 --", Rol.LECTOR)

        assertTrue(libros.any { it.restringido })
    }

    @Test
    fun `VULN-03 el HTML del comprobante no escapa el payload`() {
        val payload = "<img src=x onerror=\"alert('XSS')\">"
        val html = ComprobanteHtmlVulnerable.construir("Libro", "Lector", 7, payload, "PENDIENTE")

        assertTrue(html.contains(payload))
    }

    @Test
    fun `VULN-04 el formulario vulnerable acepta datos invalidos`() {
        assertTrue(ValidacionVulnerable.aceptar("-5", "", "x".repeat(50_000)))
    }

    @Test
    fun `VULN-05 la sesión vulnerable guarda la contraseña en plano`() {
        val sesion = crearSesionVulnerable(Unit)
        sesion.guardar(DatosSesionVulnerable(1, "BIBLIOTECARIO", "biblio1", "admin123"))

        assertEquals("admin123", sesion.leer()?.password)
    }

    @Test
    fun `VULN-01 SQLi permite entrar como bibliotecario sin password`() {
        database()
        val repo = AuthRepositoryVulnerable(crearSesionVulnerable(Unit))

        val usuario = repo.login("biblio1'--", "cualquiera")

        assertNotNull(usuario)
        assertEquals(Rol.BIBLIOTECARIO, usuario!!.rol)
    }

    @Test
    fun `VULN-09 el acceso admin vulnerable ignora el rol`() {
        assertTrue(AccesoVulnerable.puedeVerAdmin(null))
        assertTrue(AccesoVulnerable.puedeVerAdmin(Rol.LECTOR))
    }
}
