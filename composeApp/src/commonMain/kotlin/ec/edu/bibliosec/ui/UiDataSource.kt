package ec.edu.bibliosec.ui

import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.data.toDomain
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.di.secureMode
import ec.edu.bibliosec.domain.EstadoPrestamo
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Prestamo
import ec.edu.bibliosec.domain.Resena
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.domain.Usuario
import ec.edu.bibliosec.security.PasswordHasher

/**
 * Fuente de datos temporal de la UI (F2).
 *
 * Lee directamente las consultas parametrizadas generadas por SQLDelight para
 * que la navegación sea funcional. En F3/F5 será sustituida por los
 * repositorios `data.vulnerable` / `data.segura` inyectados por ServiceLocator.
 */
class UiDataSource(private val database: BibliotecaDatabase) {

    private val queries get() = database.bibliotecaQueries

    val resumen: String get() = DatabaseModule.resumen(database)

    /** Autenticación de andamiaje: en F3 se hará vulnerable y en F5 segura. */
    fun autenticar(username: String, password: String): Usuario? {
        val fila = queries.loginSeguro(username.trim()).executeAsOneOrNull() ?: return null
        val obtainido = if (secureMode) {
            PasswordHasher().verify(password, fila.password)
        } else {
            fila.password == password
        }
        return if (obtainido) fila.toDomain() else null
    }

    fun libros(texto: String, rol: Rol): List<Libro> {
        val filas = if (texto.isBlank()) {
            queries.todosLosLibros().executeAsList()
        } else {
            queries.buscarLibrosSeguro(texto.trim()).executeAsList()
        }
        return filas
            .map { it.toDomain() }
            .filter { rol == Rol.BIBLIOTECARIO || !it.restringido }
    }

    fun libro(id: Long): Libro? = queries.librosPorId(id).executeAsOneOrNull()?.toDomain()

    fun titulosLibros(): Map<Long, String> =
        queries.todosLosLibros().executeAsList().associate { it.id to it.titulo }

    fun prestamosDelUsuario(usuarioId: Long): List<Prestamo> =
        queries.prestamosDelUsuario(usuarioId).executeAsList().map { it.toDomain() }

    fun prestamosPendientes(): List<Prestamo> =
        queries.prestamosPendientes().executeAsList().map { it.toDomain() }

    fun prestamosAprobados(): List<Prestamo> =
        queries.prestamosAprobados().executeAsList().map { it.toDomain() }

    fun prestamo(id: Long): Prestamo? = queries.prestamoPorId(id).executeAsOneOrNull()?.toDomain()

    fun resenas(libroId: Long): List<Resena> =
        queries.resenasDelLibro(libroId).executeAsList().map { it.toDomain() }

    fun solicitar(
        libroId: Long,
        usuarioId: Long,
        codigoEstudiante: String,
        dias: Long,
        observacion: String,
    ): Long {
        queries.insertarPrestamo(libroId, usuarioId, codigoEstudiante, dias, observacion, FECHA_DEMO)
        // SQLDelight no expone last_insert_rowid directamente; se toma el último del usuario.
        return queries.prestamosDelUsuario(usuarioId).executeAsList().maxByOrNull { it.id }?.id ?: 0L
    }

    fun actualizarEstado(id: Long, estado: EstadoPrestamo) {
        queries.actualizarEstadoPrestamo(
            estado = estado.name,
            fechaDevolucion = if (estado == EstadoPrestamo.DEVUELTO) FECHA_DEMO else null,
            id = id,
        )
    }

    fun registrarLibro(
        isbn: String,
        titulo: String,
        autor: String,
        categoria: String,
        copias: Long,
        restringido: Boolean,
    ) {
        queries.insertarLibro(isbn, titulo, autor, categoria, copias, if (restringido) 1L else 0L)
    }

    fun agregarResena(libroId: Long, usuarioId: Long, comentario: String) {
        queries.insertarResena(libroId, usuarioId, comentario, FECHA_DEMO)
    }

    private companion object {
        const val FECHA_DEMO = "2026-11-24T12:00:00Z"
    }
}
