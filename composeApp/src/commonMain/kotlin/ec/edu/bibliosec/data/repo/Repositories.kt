package ec.edu.bibliosec.data.repo

import ec.edu.bibliosec.domain.EstadoPrestamo
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Prestamo
import ec.edu.bibliosec.domain.Resena
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.domain.Usuario

/**
 * Contratos de persistencia. Las implementaciones se añaden en:
 *  - F3 → data.vulnerable (SQL concatenado, sesión insegura, sin validación)
 *  - F5 → data.segura (consultas parametrizadas y controles)
 */
interface AuthRepository {
    fun login(username: String, password: String): Usuario?
    fun usuarioPorId(id: Long): Usuario?
    fun cerrarSesion()
}

interface LibroRepository {
    fun buscar(texto: String, rol: Rol): List<Libro>
    fun registrar(libro: Libro): Long
    fun porId(id: Long): Libro?
}

interface PrestamoRepository {
    fun solicitar(libroId: Long, usuarioId: Long, codigoEstudiante: String, dias: Long, observacion: String?): Long
    fun pendientes(): List<Prestamo>
    fun delUsuario(usuarioId: Long): List<Prestamo>
    fun actualizarEstado(id: Long, estado: EstadoPrestamo, fechaDevolucion: String? = null)
}

interface ResenaRepository {
    fun delLibro(libroId: Long): List<Resena>
    fun agregar(libroId: Long, usuarioId: Long, comentario: String, fecha: String): Long
}
