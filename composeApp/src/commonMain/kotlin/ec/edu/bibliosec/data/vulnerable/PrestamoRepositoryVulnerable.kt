package ec.edu.bibliosec.data.vulnerable

import ec.edu.bibliosec.data.repo.PrestamoRepository
import ec.edu.bibliosec.data.toDomain
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.domain.EstadoPrestamo
import ec.edu.bibliosec.domain.Prestamo

/** Sin validación de días, código ni observación (VULN-04). */
class PrestamoRepositoryVulnerable(private val database: BibliotecaDatabase) : PrestamoRepository {

    override fun solicitar(
        libroId: Long,
        usuarioId: Long,
        codigoEstudiante: String,
        dias: Long,
        observacion: String?,
    ): Long {
        // VULN-04: se guarda cualquier valor, sin rangos ni longitud.
        database.bibliotecaQueries.insertarPrestamo(
            libroId, usuarioId, codigoEstudiante, dias, observacion, "2026-11-24T12:00:00Z",
        )
        return database.bibliotecaQueries.prestamosDelUsuario(usuarioId)
            .executeAsList().maxByOrNull { it.id }?.id ?: 0L
    }

    override fun pendientes(): List<Prestamo> =
        database.bibliotecaQueries.prestamosPendientes().executeAsList().map { it.toDomain() }

    override fun delUsuario(usuarioId: Long): List<Prestamo> =
        database.bibliotecaQueries.prestamosDelUsuario(usuarioId).executeAsList().map { it.toDomain() }

    override fun actualizarEstado(id: Long, estado: EstadoPrestamo, fechaDevolucion: String?) {
        // VULN-09: no se verifica el rol de quien ejecuta la acción.
        database.bibliotecaQueries.actualizarEstadoPrestamo(estado.name, fechaDevolucion, id)
    }
}
