package ec.edu.bibliosec.ui

import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.data.repo.Repositorios
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
class UiDataSource(
    private val database: BibliotecaDatabase,
    private val repos: Repositorios? = null,
) {

    private val queries get() = database.bibliotecaQueries

    val resumen: String get() = DatabaseModule.resumen(database)

    /** F3: delega en el repositorio vulnerable cuando el ServiceLocator lo inyectó. */
    fun autenticar(username: String, password: String): Usuario? {
        repos?.auth?.let { return it.login(username, password) }
        val fila = queries.loginSeguro(username.trim()).executeAsOneOrNull() ?: return null
        val obtainido = if (secureMode) {
            PasswordHasher().verify(password, fila.password)
        } else {
            fila.password == password
        }
        return if (obtainido) fila.toDomain() else null
    }

    fun libros(texto: String, rol: Rol): List<Libro> {
        // El driver Android es más estricto que JDBC; un SQLi mal formado no debe cerrar la app.
        repos?.libro?.let { return runCatching { it.buscar(texto, rol) }.getOrDefault(emptyList()) }
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
        repos?.prestamo?.delUsuario(usuarioId)
            ?: queries.prestamosDelUsuario(usuarioId).executeAsList().map { it.toDomain() }

    fun prestamosPendientes(): List<Prestamo> =
        repos?.prestamo?.pendientes()
            ?: queries.prestamosPendientes().executeAsList().map { it.toDomain() }

    fun prestamosAprobados(): List<Prestamo> =
        queries.prestamosAprobados().executeAsList().map { it.toDomain() }

    fun prestamo(id: Long): Prestamo? = queries.prestamoPorId(id).executeAsOneOrNull()?.toDomain()

    fun resenas(libroId: Long): List<Resena> =
        repos?.resena?.delLibro(libroId)
            ?: queries.resenasDelLibro(libroId).executeAsList().map { it.toDomain() }

    fun solicitar(
        libroId: Long,
        usuarioId: Long,
        codigoEstudiante: String,
        dias: Long,
        observacion: String,
    ): Long {
        repos?.prestamo?.let {
            return it.solicitar(libroId, usuarioId, codigoEstudiante, dias, observacion)
        }
        queries.insertarPrestamo(libroId, usuarioId, codigoEstudiante, dias, observacion, FECHA_DEMO)
        // SQLDelight no expone last_insert_rowid directamente; se toma el último del usuario.
        return queries.prestamosDelUsuario(usuarioId).executeAsList().maxByOrNull { it.id }?.id ?: 0L
    }

    fun actualizarEstado(id: Long, estado: EstadoPrestamo) {
        repos?.prestamo?.let {
            it.actualizarEstado(
                id,
                estado,
                if (estado == EstadoPrestamo.DEVUELTO) FECHA_DEMO else null,
            )
            return
        }
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
        // INSERT OR IGNORE ya evita ISBN duplicados; runCatching cubre otros fallos del driver.
        runCatching {
            queries.insertarLibro(isbn, titulo, autor, categoria, copias, if (restringido) 1L else 0L)
        }
    }

    fun agregarResena(libroId: Long, usuarioId: Long, comentario: String) {
        repos?.resena?.let {
            it.agregar(libroId, usuarioId, comentario, FECHA_DEMO)
            return
        }
        queries.insertarResena(libroId, usuarioId, comentario, FECHA_DEMO)
    }

    private companion object {
        const val FECHA_DEMO = "2026-11-24T12:00:00Z"
    }
}
