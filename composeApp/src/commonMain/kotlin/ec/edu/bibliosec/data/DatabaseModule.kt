package ec.edu.bibliosec.data

import app.cash.sqldelight.db.SqlDriver
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.di.secureMode
import ec.edu.bibliosec.security.PasswordHasher

/**
 * Crea la base de datos local y la siembra al primer arranque.
 *
 * En la variante vulnerable las contraseñas se guardan en texto plano;
 * en la segura se guardan como hash PBKDF2 con salt (VULN-05).
 */
object DatabaseModule {

    /**
     * Driver compartido. Los repositorios vulnerables (F3) lo necesitan para
     * construir SQL por concatenación y demostrar SQLi (VULN-01/02).
     */
    private var driverCompartido: SqlDriver? = null

    fun crear(driverFactory: DriverFactory): BibliotecaDatabase {
        val driver = driverFactory.createDriver()
        driverCompartido = driver
        val database = BibliotecaDatabase(driver)
        sembrar(database)
        return database
    }

    fun driver(): SqlDriver =
        driverCompartido ?: error("La base de datos aún no fue creada (DatabaseModule.crear)")

    /** Permite usar un driver en memoria en las pruebas de explotación controlada. */
    fun usarDriver(driver: SqlDriver) {
        driverCompartido = driver
    }

    fun sembrar(database: BibliotecaDatabase) {
        val queries = database.bibliotecaQueries
        val hasher = PasswordHasher()

        fun clave(texto: String): String = if (secureMode) hasher.hash(texto) else texto

        queries.transaction {
            queries.limpiarResenas()
            queries.limpiarPrestamos()
            queries.limpiarLibros()
            queries.limpiarUsuarios()

            // Usuarios (HU-01)
            queries.insertarUsuario("lector1", clave("lector123"), "LECTOR", "E2024001", "Ana Lector")
            queries.insertarUsuario("lector2", clave("lector456"), "LECTOR", "E2024002", "Bruno Lector")
            queries.insertarUsuario("biblio1", clave("admin123"), "BIBLIOTECARIO", null, "Carla Bibliotecaria")

            // Catálogo: 12 libros, 3 de ellos restringidos (HU-02, HU-07)
            queries.insertarLibro("978-84-01-0001-1", "Clean Code", "Robert C. Martin", "Software", 3, 0)
            queries.insertarLibro("978-84-01-0002-8", "The Pragmatic Programmer", "Andrew Hunt", "Software", 2, 0)
            queries.insertarLibro("978-84-01-0003-5", "Design Patterns", "Gamma et al.", "Software", 4, 0)
            queries.insertarLibro("978-84-01-0004-2", "Introduccion a la BD", "Elmasri", "Datos", 2, 0)
            queries.insertarLibro("978-84-01-0005-9", "Sistemas Operativos", "Tanenbaum", "Sistemas", 1, 0)
            queries.insertarLibro("978-84-01-0006-6", "Redes de Computadoras", "Kurose", "Redes", 2, 0)
            queries.insertarLibro("978-84-01-0007-3", "Algoritmos", "Cormen", "Algoritmia", 3, 0)
            queries.insertarLibro("978-84-01-0008-0", "Bases de Datos Seguras", "Sandhu", "Seguridad", 2, 0)
            queries.insertarLibro("978-84-01-0009-7", "Historia de la Biblioteca", "Perez", "Historia", 1, 0)
            queries.insertarLibro("978-84-01-0010-3", "Estructuras de Datos", "Knuth", "Algoritmia", 2, 0)
            queries.insertarLibro("I-001", "Manual interno de adquisiciones", "Direccion", "Uso interno", 1, 1)
            queries.insertarLibro("I-002", "Archivo historico reservado", "Archivo", "Uso interno", 1, 1)
            queries.insertarLibro("I-003", "Tesis embargada", "Autor anonimo", "Uso interno", 1, 1)

            // Préstamos de ejemplo (HU-03, HU-06)
            queries.insertarPrestamo(1, 1, "E2024001", 7, "Para el proyecto de seguridad", "2026-11-20T10:00:00Z")
            queries.insertarPrestamo(4, 2, "E2024002", 15, "Consulta de examen", "2026-11-21T11:30:00Z")

            // Reseñas de ejemplo (HU-05)
            queries.insertarResena(1, 1, "Excelente guia de buenas practicas.", "2026-11-22T09:15:00Z")
            queries.insertarResena(1, 2, "Muy util para el laboratorio.", "2026-11-22T10:05:00Z")
        }
    }

    /** Resumen legible para verificar el seed en pantalla durante F1. */
    fun resumen(database: BibliotecaDatabase): String {
        val q = database.bibliotecaQueries
        return "Usuarios: ${q.contarUsuarios().executeAsOne()} · Libros: ${q.contarLibros().executeAsOne()} " +
            "(restringidos: ${q.contarLibrosRestringidos().executeAsOne()}) · " +
            "Préstamos: ${q.contarPrestamos().executeAsOne()} · " +
            "Reseñas: ${q.contarResenas().executeAsOne()}"
    }
}
