package ec.edu.bibliosec.di

import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.data.DriverFactory
import ec.edu.bibliosec.data.repo.Repositorios
import ec.edu.bibliosec.data.vulnerable.AuthRepositoryVulnerable
import ec.edu.bibliosec.data.vulnerable.LibroRepositoryVulnerable
import ec.edu.bibliosec.data.vulnerable.PrestamoRepositoryVulnerable
import ec.edu.bibliosec.data.vulnerable.ResenaRepositoryVulnerable
import ec.edu.bibliosec.data.vulnerable.crearSesionVulnerable
import ec.edu.bibliosec.db.BibliotecaDatabase

/**
 * Punto de composición de la aplicación.
 *
 * F3: en el flavor vulnerable inyecta los repositorios inseguros.
 * F5: en el flavor segura inyectará los repositorios con controles.
 */
object ServiceLocator {

    val esSeguro: Boolean get() = secureMode

    private var repositorios: Repositorios? = null

    fun crearDatabase(driverFactory: DriverFactory): BibliotecaDatabase =
        DatabaseModule.crear(driverFactory)

    /** Debe llamarse al arrancar, antes de crear la UI. */
    fun inicializar(context: Any, database: BibliotecaDatabase) {
        repositorios = if (secureMode) {
            null // F5: Repositorios seguros
        } else {
            Repositorios(
                auth = AuthRepositoryVulnerable(crearSesionVulnerable(context)),
                libro = LibroRepositoryVulnerable(database),
                prestamo = PrestamoRepositoryVulnerable(database),
                resena = ResenaRepositoryVulnerable(database),
            )
        }
    }

    fun repositoriosActuales(): Repositorios? = repositorios
}
