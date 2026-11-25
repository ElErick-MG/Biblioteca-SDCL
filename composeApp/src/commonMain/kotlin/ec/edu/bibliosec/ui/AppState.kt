package ec.edu.bibliosec.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ec.edu.bibliosec.domain.EstadoPrestamo
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Prestamo
import ec.edu.bibliosec.domain.Resena
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.domain.Usuario
import ec.edu.bibliosec.ui.vulnerable.ValidacionVulnerable

/** Estado de la navegación y de los formularios (F2/F3, sin coroutines añadidas). */
class AppState(
    private val source: UiDataSource,
    private val esSeguro: Boolean = false,
) {

    var ruta by mutableStateOf<Ruta>(Ruta.Login)
        private set
    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var mensaje by mutableStateOf("")
        private set

    var busqueda by mutableStateOf("")
        private set
    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set
    var titulos by mutableStateOf<Map<Long, String>>(emptyMap())
        private set

    var dias by mutableStateOf("7")
        private set
    var codigo by mutableStateOf("")
        private set
    var observacion by mutableStateOf("")
        private set
    var comprobanteId by mutableStateOf(0L)
        private set

    var resenas by mutableStateOf<List<Resena>>(emptyList())
        private set
    var comentario by mutableStateOf("")
        private set

    var misPrestamos by mutableStateOf<List<Prestamo>>(emptyList())
        private set
    var pendientes by mutableStateOf<List<Prestamo>>(emptyList())
        private set
    var aprobados by mutableStateOf<List<Prestamo>>(emptyList())
        private set

    fun iniciar() {
        titulos = source.titulosLibros()
        libros = source.libros("", Rol.LECTOR)
    }

    fun login(nombre: String, clave: String) {
        val encontrado = source.autenticar(nombre, clave)
        if (encontrado == null) {
            mensaje = "Credenciales inválidas"
            return
        }
        usuario = encontrado
        mensaje = ""
        recargar()
        ruta = if (encontrado.rol == Rol.BIBLIOTECARIO) Ruta.Admin else Ruta.Catalogo
    }

    /**
     * Acceso directo al panel desde el login.
     * VULN-01/VULN-09: en vulnerable se acepta cualquier credencial que pase el SQLi
     * y se navega a Admin sin verificar el rol. En segura se exige BIBLIOTECARIO.
     */
    fun loginAdmin(nombre: String, clave: String) {
        val encontrado = source.autenticar(nombre, clave)
        if (encontrado == null) {
            mensaje = "Credenciales inválidas"
            return
        }
        if (esSeguro && encontrado.rol != Rol.BIBLIOTECARIO) {
            mensaje = "Acceso denegado: se requiere rol BIBLIOTECARIO"
            return
        }
        usuario = encontrado
        mensaje = ""
        recargar()
        ruta = Ruta.Admin
    }

    fun cerrarSesion() {
        usuario = null
        mensaje = ""
        ruta = Ruta.Login
    }

    private fun recargar() {
        val rol = usuario?.rol ?: Rol.LECTOR
        titulos = source.titulosLibros()
        libros = source.libros(busqueda, rol)
        pendientes = source.prestamosPendientes()
        aprobados = source.prestamosAprobados()
        usuario?.let { misPrestamos = source.prestamosDelUsuario(it.id) }
    }

    fun buscar(texto: String) {
        busqueda = texto
        libros = source.libros(texto, usuario?.rol ?: Rol.LECTOR)
    }

    fun abrirCatalogo() {
        recargar()
        ruta = Ruta.Catalogo
    }

    fun abrirPrestamo(libroId: Long) {
        dias = "7"
        codigo = usuario?.codigoEstudiante.orEmpty()
        observacion = ""
        ruta = Ruta.Prestamo(libroId)
    }

    fun cambiarDias(valor: String) { dias = valor }
    fun cambiarCodigo(valor: String) { codigo = valor }
    fun cambiarObservacion(valor: String) { observacion = valor }

    fun enviarPrestamo(libroId: Long) {
        val id = usuario?.id ?: return
        // VULN-04: en el flavor vulnerable se acepta cualquier valor (incluso "abc" → 0).
        if (!esSeguro && !ValidacionVulnerable.aceptar(dias, codigo, observacion)) return
        val diasNum = if (esSeguro) (dias.toLongOrNull() ?: return) else (dias.toLongOrNull() ?: 0L)
        comprobanteId = source.solicitar(libroId, id, codigo, diasNum, observacion)
        recargar()
        ruta = Ruta.Comprobante(comprobanteId)
    }

    fun abrirResenas(libroId: Long) {
        resenas = source.resenas(libroId)
        comentario = ""
        ruta = Ruta.Resenas(libroId)
    }

    fun cambiarComentario(valor: String) { comentario = valor }

    fun agregarResena(libroId: Long) {
        val id = usuario?.id ?: return
        if (comentario.isBlank()) return
        source.agregarResena(libroId, id, comentario.trim())
        resenas = source.resenas(libroId)
        comentario = ""
    }

    fun abrirMisPrestamos() {
        usuario?.let { misPrestamos = source.prestamosDelUsuario(it.id) }
        ruta = Ruta.MisPrestamos
    }

    fun abrirAdmin() {
        pendientes = source.prestamosPendientes()
        aprobados = source.prestamosAprobados()
        ruta = Ruta.Admin
    }

    fun aprobar(id: Long) {
        source.actualizarEstado(id, EstadoPrestamo.APROBADO)
        recargar()
    }

    fun rechazar(id: Long) {
        source.actualizarEstado(id, EstadoPrestamo.RECHAZADO)
        recargar()
    }

    fun devolver(id: Long) {
        source.actualizarEstado(id, EstadoPrestamo.DEVUELTO)
        recargar()
    }

    fun registrarLibro(
        isbn: String,
        titulo: String,
        autor: String,
        categoria: String,
        copias: Long,
        restringido: Boolean,
    ) {
        source.registrarLibro(isbn, titulo, autor, categoria, copias, restringido)
        recargar()
    }
}
