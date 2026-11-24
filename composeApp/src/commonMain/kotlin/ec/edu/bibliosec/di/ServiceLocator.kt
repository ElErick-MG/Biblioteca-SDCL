package ec.edu.bibliosec.di

import ec.edu.bibliosec.data.DriverFactory
import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.db.BibliotecaDatabase

/**
 * Punto de composición de la aplicación.
 *
 * F1: crea la BD con su seed. En F3/F5 este objeto elegirá además entre las
 * implementaciones `data.vulnerable` y `data.segura` según [secureMode].
 */
object ServiceLocator {

    val esSeguro: Boolean get() = secureMode

    fun crearDatabase(driverFactory: DriverFactory): BibliotecaDatabase =
        DatabaseModule.crear(driverFactory)
}
