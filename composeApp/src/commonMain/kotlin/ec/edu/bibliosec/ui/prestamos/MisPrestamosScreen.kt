package ec.edu.bibliosec.ui.prestamos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.bibliosec.domain.EstadoPrestamo
import ec.edu.bibliosec.domain.Prestamo

@Composable
fun MisPrestamosScreen(
    prestamos: List<Prestamo>,
    titulos: Map<Long, String>,
    onVolver: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Mis préstamos", style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = onVolver) { Text("Volver al catálogo") }
        LazyColumn {
            items(prestamos, key = { it.id }) { prestamo ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(titulos[prestamo.libroId] ?: "Libro ${prestamo.libroId}")
                        Text("Estado: ${prestamo.estado.name} · Días: ${prestamo.dias}")
                        Text(prestamo.observacion.orEmpty(), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MisPrestamosPreview() {
    val prestamo = Prestamo(
        id = 1, libroId = 1, usuarioId = 1, codigoEstudiante = "E2024001",
        dias = 7, observacion = "Laboratorio", estado = EstadoPrestamo.PENDIENTE,
        fechaSolicitud = "2026-11-20", fechaDevolucion = null,
    )
    MisPrestamosScreen(listOf(prestamo), mapOf(1L to "Clean Code")) {}
}
