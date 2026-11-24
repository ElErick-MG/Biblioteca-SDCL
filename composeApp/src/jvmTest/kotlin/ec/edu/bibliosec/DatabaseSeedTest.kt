package ec.edu.bibliosec

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.db.BibliotecaDatabase
import org.junit.Assert.assertEquals
import org.junit.Test

/** Verifica el criterio de aceptación de F1 sin depender de un dispositivo. */
class DatabaseSeedTest {

    @Test
    fun `la base de datos se crea con el seed obligatorio`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BibliotecaDatabase.Schema.create(driver)
        val database = BibliotecaDatabase(driver)

        DatabaseModule.sembrar(database)

        val q = database.bibliotecaQueries
        assertEquals(3L, q.contarUsuarios().executeAsOne())          // lector1, lector2, biblio1
        assertEquals(13L, q.contarLibros().executeAsOne())          // 10 públicos + 3 restringidos
        assertEquals(3L, q.contarLibrosRestringidos().executeAsOne())
        assertEquals(2L, q.contarPrestamos().executeAsOne())
        assertEquals(2L, q.contarResenas().executeAsOne())
    }

    @Test
    fun `las consultas parametrizadas devuelven solo libros publicos`() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BibliotecaDatabase.Schema.create(driver)
        val database = BibliotecaDatabase(driver)
        DatabaseModule.sembrar(database)

        val resultados = database.bibliotecaQueries.buscarLibrosSeguro("a").executeAsList()
        assertEquals(true, resultados.all { it.restringido == 0L })
    }
}
