package ec.edu.bibliosec

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Punto de entrada compartido de la UI.
 *
 * F1: muestra el resumen del seed para verificar que la BD se creó correctamente.
 * F2 añadirá Login, Catálogo, Solicitud, Comprobante, Reseñas, Mis préstamos
 * y Panel del bibliotecario, cada una con @Preview.
 */
@Composable
fun App(resumen: String = "", esSeguro: Boolean = false) {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("BiblioSec — F1: base de datos sembrada", style = MaterialTheme.typography.titleLarge)
                Text(if (esSeguro) "Variante: SEGURA" else "Variante: VULNERABLE")
                Text(resumen, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}
