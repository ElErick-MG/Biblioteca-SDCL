package ec.edu.bibliosec.data

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.db.SqlDriver
import ec.edu.bibliosec.db.BibliotecaDatabase

/** Driver SQLite local del dispositivo (archivo biblioteca.db). */
actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(BibliotecaDatabase.Schema, context, "biblioteca.db")
}
