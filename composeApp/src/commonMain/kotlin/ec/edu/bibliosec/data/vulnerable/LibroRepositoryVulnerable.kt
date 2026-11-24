package ec.edu.bibliosec.data.vulnerable

import ec.edu.bibliosec.data.toDomain
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.data.repo.LibroRepository

/** Buscador con SQLi que permite filtrar credenciales y libros restringidos. */
class LibroRepositoryVulnerable(private val database: BibliotecaDatabase) : LibroRepository {

    override fun buscar(texto: String, rol: Rol): List<Libro> {
        // VULN-02: concatenación directa; el rol se ignora (VULN-09).
        // Payload: %' OR restringido=1 --
        // VULN-02 + VULN-09: sin filtro por rol ni por `restringido`; el payload
        // `%' OR restringido=1 --` hace aparecer los libros de uso interno.
        val sql = """
            SELECT * FROM Libro
            WHERE (titulo LIKE '%$texto%'
              OR autor LIKE '%$texto%'
              OR isbn LIKE '%$texto%'
            )
        """.trimIndent()
        return SqlVulnerable.libros(sql).map { it.toDomain() }
    }

    override fun registrar(libro: Libro): Long {
        database.bibliotecaQueries.insertarLibro(
            libro.isbn, libro.titulo, libro.autor, libro.categoria,
            libro.copiasDisponibles, if (libro.restringido) 1L else 0L,
        )
        return database.bibliotecaQueries.todosLosLibros().executeAsList().maxByOrNull { it.id }?.id ?: 0L
    }

    override fun porId(id: Long): Libro? =
        database.bibliotecaQueries.librosPorId(id).executeAsOneOrNull()?.toDomain()
}
