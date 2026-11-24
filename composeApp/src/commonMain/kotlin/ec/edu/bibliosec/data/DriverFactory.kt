package ec.edu.bibliosec.data

import app.cash.sqldelight.db.SqlDriver

/** Crea el driver SQLite de la plataforma (AndroidSqliteDriver / JdbcSqliteDriver). */
expect class DriverFactory {
    fun createDriver(): SqlDriver
}
