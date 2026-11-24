package ec.edu.bibliosec

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.ui.AppState
import ec.edu.bibliosec.ui.Ruta
import ec.edu.bibliosec.ui.UiDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Verifica la navegación funcional por rol de F2. */
class NavegacionTest {

    private fun estado(): AppState {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BibliotecaDatabase.Schema.create(driver)
        val database = BibliotecaDatabase(driver)
        DatabaseModule.sembrar(database)
        return AppState(UiDataSource(database)).also { it.iniciar() }
    }

    @Test
    fun `el lector entra al catalogo`() {
        val state = estado()
        state.login("lector1", "lector123")

        assertEquals(Rol.LECTOR, state.usuario?.rol)
        assertEquals(Ruta.Catalogo, state.ruta)
    }

    @Test
    fun `el bibliotecario entra al panel admin`() {
        val state = estado()
        state.login("biblio1", "admin123")

        assertEquals(Rol.BIBLIOTECARIO, state.usuario?.rol)
        assertEquals(Ruta.Admin, state.ruta)
    }

    @Test
    fun `credenciales invalidas no navegan`() {
        val state = estado()
        state.login("lector1", "clave-incorrecta")

        assertEquals(Ruta.Login, state.ruta)
        assertTrue(state.mensaje.isNotBlank())
    }

    @Test
    fun `el catalogo del lector oculta libros restringidos`() {
        val state = estado()
        state.login("lector1", "lector123")

        assertTrue(state.libros.isNotEmpty())
        assertTrue(state.libros.all { !it.restringido })
    }
}
