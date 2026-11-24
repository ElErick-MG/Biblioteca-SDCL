package ec.edu.bibliosec.data

import ec.edu.bibliosec.domain.EstadoPrestamo
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Prestamo
import ec.edu.bibliosec.domain.Resena
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.domain.Usuario
import ec.edu.bibliosec.db.Libro as LibroRow
import ec.edu.bibliosec.db.Prestamo as PrestamoRow
import ec.edu.bibliosec.db.Resena as ResenaRow
import ec.edu.bibliosec.db.Usuario as UsuarioRow

/** Traduce las filas generadas por SQLDelight a los modelos de dominio. */
internal fun UsuarioRow.toDomain() = Usuario(
    id = id,
    username = username,
    password = password,
    rol = Rol.valueOf(rol),
    codigoEstudiante = codigoEstudiante,
    nombreCompleto = nombreCompleto,
)

internal fun LibroRow.toDomain() = Libro(
    id = id,
    isbn = isbn,
    titulo = titulo,
    autor = autor,
    categoria = categoria,
    copiasDisponibles = copiasDisponibles,
    restringido = restringido == 1L,
)

internal fun PrestamoRow.toDomain() = Prestamo(
    id = id,
    libroId = libroId,
    usuarioId = usuarioId,
    codigoEstudiante = codigoEstudiante,
    dias = dias,
    observacion = observacion,
    estado = EstadoPrestamo.valueOf(estado),
    fechaSolicitud = fechaSolicitud,
    fechaDevolucion = fechaDevolucion,
)

internal fun ResenaRow.toDomain() = Resena(
    id = id,
    libroId = libroId,
    usuarioId = usuarioId,
    comentario = comentario,
    fecha = fecha,
)
