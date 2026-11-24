package ec.edu.bibliosec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ec.edu.bibliosec.data.DatabaseModule
import ec.edu.bibliosec.data.DriverFactory
import ec.edu.bibliosec.di.ServiceLocator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // F1: la BD se crea y siembra al arrancar. F2 conectará el estado de la
        // sesión y F3/F5 elegirán los repositorios según el flavor.
        val database = ServiceLocator.crearDatabase(DriverFactory(this))
        val resumen = DatabaseModule.resumen(database)

        setContent {
            App(
                database = database,
                resumen = resumen,
                esSeguro = ServiceLocator.esSeguro,
            )
        }
    }
}
