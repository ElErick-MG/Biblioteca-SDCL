package ec.edu.bibliosec

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ec.edu.bibliosec.db.BibliotecaDatabase
import ec.edu.bibliosec.di.ServiceLocator
import ec.edu.bibliosec.domain.Rol
import ec.edu.bibliosec.ui.AccesoDenegadoScreen
import ec.edu.bibliosec.ui.AppState
import ec.edu.bibliosec.ui.Ruta
import ec.edu.bibliosec.ui.UiDataSource
import ec.edu.bibliosec.ui.admin.AdminScreen
import ec.edu.bibliosec.ui.catalogo.CatalogoScreen
import ec.edu.bibliosec.ui.comprobante.ComprobanteScreen
import ec.edu.bibliosec.ui.login.LoginScreen
import ec.edu.bibliosec.ui.prestamo.PrestamoScreen
import ec.edu.bibliosec.ui.prestamos.MisPrestamosScreen
import ec.edu.bibliosec.ui.resenas.ResenasScreen
import ec.edu.bibliosec.ui.vulnerable.AccesoVulnerable
import ec.edu.bibliosec.ui.vulnerable.ComprobanteHtmlVulnerable

/**
 * Punto de entrada compartido de la UI (F2).
 *
 * La navegación es funcional por rol: un LECTOR no puede abrir `Admin`.
 * En F5 la barrera se reforzará con `AccessPolicy` también en casos de uso
 * y repositorios (VULN-09).
 */
@Composable
fun App(database: BibliotecaDatabase, resumen: String, esSeguro: Boolean) {
    val source = remember(database) {
        UiDataSource(database, ServiceLocator.repositoriosActuales())
    }
    val state = remember(source) { AppState(source, esSeguro).also { it.iniciar() } }

    Surface {
        Column {
            if (state.ruta != Ruta.Login) {
                Text(if (esSeguro) "Variante: SEGURA" else "Variante: VULNERABLE")
                Text(resumen, style = MaterialTheme.typography.bodySmall)
            }

            when (val ruta = state.ruta) {
                Ruta.Login -> LoginScreen(
                    esSeguro = esSeguro,
                    mensaje = state.mensaje,
                    onLogin = { u, p -> state.login(u, p) },
                    onLoginAdmin = { u, p -> state.loginAdmin(u, p) },
                )

                Ruta.Catalogo -> CatalogoScreen(
                    libros = state.libros,
                    busqueda = state.busqueda,
                    onBuscar = { state.buscar(it) },
                    onSolicitar = { state.abrirPrestamo(it) },
                    onResenas = { state.abrirResenas(it) },
                    onMisPrestamos = { state.abrirMisPrestamos() },
                    onCerrarSesion = { state.cerrarSesion() },
                )

                is Ruta.Prestamo -> PrestamoScreen(
                    libro = source.libro(ruta.libroId),
                    dias = state.dias,
                    codigo = state.codigo,
                    observacion = state.observacion,
                    onDias = { state.cambiarDias(it) },
                    onCodigo = { state.cambiarCodigo(it) },
                    onObservacion = { state.cambiarObservacion(it) },
                    onEnviar = { state.enviarPrestamo(ruta.libroId) },
                    onVolver = { state.abrirCatalogo() },
                    permitirObservacionLarga = !esSeguro,
                )

                is Ruta.Comprobante -> {
                    val prestamo = source.prestamo(ruta.prestamoId)
                    ComprobanteScreen(
                        libro = prestamo?.let { source.libro(it.libroId)?.titulo } ?: "—",
                        usuario = state.usuario?.nombreCompleto ?: "—",
                        dias = prestamo?.dias ?: 0,
                        observacion = prestamo?.observacion.orEmpty(),
                        estado = prestamo?.estado?.name ?: "—",
                        onVolver = { state.abrirCatalogo() },
                        htmlVulnerable = if (!esSeguro) {
                            ComprobanteHtmlVulnerable.construir(
                                libro = prestamo?.let { source.libro(it.libroId)?.titulo } ?: "—",
                                usuario = state.usuario?.nombreCompleto ?: "—",
                                dias = prestamo?.dias ?: 0,
                                observacion = prestamo?.observacion.orEmpty(),
                                estado = prestamo?.estado?.name ?: "—",
                            )
                        } else null,
                    )
                }

                is Ruta.Resenas -> ResenasScreen(
                    libro = source.libro(ruta.libroId),
                    resenas = state.resenas,
                    comentario = state.comentario,
                    onComentario = { state.cambiarComentario(it) },
                    onAgregar = { state.agregarResena(ruta.libroId) },
                    onVolver = { state.abrirCatalogo() },
                    htmlVulnerable = if (!esSeguro) {
                        ComprobanteHtmlVulnerable.reseñas(state.resenas.map { it.comentario })
                    } else null,
                )

                Ruta.MisPrestamos -> MisPrestamosScreen(
                    prestamos = state.misPrestamos,
                    titulos = state.titulos,
                    onVolver = { state.abrirCatalogo() },
                )

                Ruta.Admin -> if (
                    if (esSeguro) {
                        state.usuario?.rol == Rol.BIBLIOTECARIO
                    } else {
                        // VULN-09: se ignora el rol en el flavor vulnerable.
                        AccesoVulnerable.puedeVerAdmin(state.usuario?.rol)
                    }
                ) {
                    AdminScreen(
                        pendientes = state.pendientes,
                        aprobados = state.aprobados,
                        libros = state.libros,
                        titulos = state.titulos,
                        onAprobar = { state.aprobar(it) },
                        onRechazar = { state.rechazar(it) },
                        onDevolver = { state.devolver(it) },
                        onRegistrar = { isbn, titulo, autor, cat, copias, restringido ->
                            state.registrarLibro(isbn, titulo, autor, cat, copias, restringido)
                        },
                        onVolver = { state.abrirCatalogo() },
                    )
                } else {
                    AccesoDenegadoScreen(onVolver = { state.abrirCatalogo() })
                }
            }
        }
    }
}
