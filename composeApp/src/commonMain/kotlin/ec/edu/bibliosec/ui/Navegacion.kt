package ec.edu.bibliosec.ui

/** Rutas de la navegación base de F2. F5 endurecerá el acceso con AccessPolicy. */
sealed interface Ruta {
    data object Login : Ruta
    data object Catalogo : Ruta
    data class Prestamo(val libroId: Long) : Ruta
    data class Comprobante(val prestamoId: Long) : Ruta
    data class Resenas(val libroId: Long) : Ruta
    data object MisPrestamos : Ruta
    data object Admin : Ruta
}
