package ec.edu.bibliosec.data.vulnerable

import ec.edu.bibliosec.data.repo.ResenaRepository
import ec.edu.bibliosec.data.toDomain
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.domain.Resena

/** Las reseñas se guardan y renderizan sin sanitizar (VULN-03). */
class ResenaRepositoryVulnerable(private val database:BibliotecaDatabase) : ResenaRepository {

    override fun delLibro(libroId: Long): List<Resena> =
        database.bibliotecaQueries.resenasDelLibro(libroId).executeAsList().map { it.toDomain() }

    override fun agregar(libroId: Long, usuarioId: Long, comentario: String, fecha: String): Long {
        // VULN-03: el HTML malicioso se persiste tal cual.
        database.bibliotecaQueries.insertarResena(libroId, usuarioId, comentario, fecha)
        return database.bibliotecaQueries.resenasDelLibro(libroId)
            .executeAsList().maxByOrNull { it.id }?.id ?: 0L
    }
}
