package ec.edu.bibliosec.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** F2: barrera de navegación por rol. F5 la sustituirá por AccessPolicy. */
@Composable
fun AccesoDenegadoScreen(onVolver: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Acceso denegado", style = MaterialTheme.typography.headlineSmall)
        Text("Tu rol no permite abrir el panel del bibliotecario.")
        Button(onClick = onVolver) { Text("Volver al catálogo") }
    }
}

@Preview
@Composable
private fun AccesoDenegadoPreview() {
    AccesoDenegadoScreen {}
}
