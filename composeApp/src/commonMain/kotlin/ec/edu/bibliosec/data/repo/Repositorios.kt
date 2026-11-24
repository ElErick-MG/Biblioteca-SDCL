package ec.edu.bibliosec.data.repo

/** Conjunto de repositorios inyectados por el ServiceLocator según el flavor. */
data class Repositorios(
    val auth: AuthRepository,
    val libro: LibroRepository,
    val prestamo: PrestamoRepository,
    val resena: ResenaRepository,
)
