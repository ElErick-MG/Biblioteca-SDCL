package ec.edu.bibliosec.domain

/** Roles del sistema. El rol se obtiene siempre de la BD, nunca de preferencias. */
enum class Rol { LECTOR, BIBLIOTECARIO }

/** Ciclo de vida de una solicitud de préstamo. */
enum class EstadoPrestamo { PENDIENTE, APROBADO, RECHAZADO, DEVUELTO }

data class Usuario(
    val id: Long,
    val username: String,
    val password: String,
    val rol: Rol,
    val codigoEstudiante: String?,
    val nombreCompleto: String,
)

data class Libro(
    val id: Long,
    val isbn: String,
    val titulo: String,
    val autor: String,
    val categoria: String,
    val copiasDisponibles: Long,
    val restringido: Boolean,
)

data class Prestamo(
    val id: Long,
    val libroId: Long,
    val usuarioId: Long,
    val codigoEstudiante: String,
    val dias: Long,
    val observacion: String?,
    val estado: EstadoPrestamo,
    val fechaSolicitud: String,
    val fechaDevolucion: String?,
)

data class Resena(
    val id: Long,
    val libroId: Long,
    val usuarioId: Long,
    val comentario: String,
    val fecha: String,
)
