package ec.edu.bibliosec.data.vulnerable

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.db.Libro as LibroRow
import ec.edu.bibliosec.db.Usuario as UsuarioRow

/** Ejecución de SQL construido por concatenación directa (VULN-01 / VULN-02). */
object SqlVulnerable {

    fun usuarios(sql: String): List<UsuarioRow> =
        DatabaseModule.driver()
            .executeQuery(null, sql, { cursor ->
                val filas = mutableListOf<UsuarioRow>()
                while (cursor.next().value == true) filas.add(usuario(cursor))
                QueryResult.Value(filas)
            }, 0)
            .value

    fun libros(sql: String): List<LibroRow> =
        DatabaseModule.driver()
            .executeQuery(null, sql, { cursor ->
                val filas = mutableListOf<LibroRow>()
                while (cursor.next().value == true) filas.add(libro(cursor))
                QueryResult.Value(filas)
            }, 0)
            .value

    private fun usuario(cursor: SqlCursor) = UsuarioRow(
        id = cursor.getLong(0)!!,
        username = cursor.getString(1)!!,
        password = cursor.getString(2)!!,
        rol = cursor.getString(3)!!,
        codigoEstudiante = cursor.getString(4),
        nombreCompleto = cursor.getString(5)!!,
    )

    private fun libro(cursor: SqlCursor) = LibroRow(
        id = cursor.getLong(0)!!,
        isbn = cursor.getString(1)!!,
        titulo = cursor.getString(2)!!,
        autor = cursor.getString(3)!!,
        categoria = cursor.getString(4)!!,
        copiasDisponibles = cursor.getLong(5)!!,
        restringido = cursor.getLong(6)!!,
    )
}
