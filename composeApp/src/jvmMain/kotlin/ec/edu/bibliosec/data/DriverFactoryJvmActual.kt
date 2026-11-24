package ec.edu.bibliosec.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ec.edu.bibliosec.db.BibliotecaDatabase

/** Driver JVM para inspección y pruebas; no es la app de escritorio (F0/F1). */
actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:biblioteca.db").also { BibliotecaDatabase.Schema.create(it) }
}
