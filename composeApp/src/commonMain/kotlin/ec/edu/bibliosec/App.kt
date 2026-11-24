package ec.edu.bibliosec

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Punto de entrada compartido de la UI.
 *
 * F0: pantalla mínima de verificación del setup. Las pantallas funcionales
 * (Login, Catálogo, Préstamo, etc.) se añaden en F2.
 */
@Composable
fun App() {
    MaterialTheme {
        Surface {
            Text("BiblioSec — F0 Setup")
        }
    }
}
