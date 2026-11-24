package ec.edu.bibliosec.ui.prestamo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.bibliosec.domain.Libro

@Composable
fun PrestamoScreen(
    libro: Libro?,
    dias: String,
    codigo: String,
    observacion: String,
    onDias: (String) -> Unit,
    onCodigo: (String) -> Unit,
    onObservacion: (String) -> Unit,
    onEnviar: () -> Unit,
    onVolver: () -> Unit,
    permitirObservacionLarga: Boolean = false,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Solicitud de préstamo", style = MaterialTheme.typography.headlineSmall)
        Text("Libro: ${libro?.titulo ?: "—"}")
        TextButton(onClick = onVolver) { Text("Volver al catálogo") }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = dias,
            onValueChange = onDias,
            label = { Text("Días (1-15)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = codigo,
            onValueChange = onCodigo,
            label = { Text("Código de estudiante") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = observacion,
            onValueChange = { if (permitirObservacionLarga || it.length <= 200) onObservacion(it) },
            label = { Text("Observación") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
        )
        Text("${observacion.length}/200 caracteres")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onEnviar, modifier = Modifier.fillMaxWidth()) {
            Text("Enviar solicitud")
        }
    }
}

@Preview
@Composable
private fun PrestamoPreview() {
    val libro = Libro(1, "978-84-01-0001-1", "Clean Code", "Robert C. Martin", "Software", 3, false)
    PrestamoScreen(libro, "7", "E2024001", "Para el laboratorio", {}, {}, {}, {}, {})
}
