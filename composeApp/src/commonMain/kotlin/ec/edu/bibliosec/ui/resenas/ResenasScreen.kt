package ec.edu.bibliosec.ui.resenas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Resena
import ec.edu.bibliosec.ui.comprobante.ComprobanteWebView

@Composable
fun ResenasScreen(
    libro: Libro?,
    resenas: List<Resena>,
    comentario: String,
    onComentario: (String) -> Unit,
    onAgregar: () -> Unit,
    onVolver: () -> Unit,
    htmlVulnerable: String? = null,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Reseñas de ${libro?.titulo ?: "—"}", style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = onVolver) { Text("Volver al catálogo") }
        if (htmlVulnerable != null) {
            // VULN-03: reseñas inyectadas como HTML crudo en WebView con JS activo.
            ComprobanteWebView(htmlVulnerable, Modifier.fillMaxWidth().height(240.dp))
        } else {
            LazyColumn {
                items(resenas, key = { it.id }) { resena ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(resena.comentario)
                            Text(resena.fecha, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = comentario,
            onValueChange = onComentario,
            label = { Text("Nueva reseña") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )
        Button(onClick = onAgregar, modifier = Modifier.fillMaxWidth()) { Text("Publicar reseña") }
    }
}

@Preview
@Composable
private fun ResenasPreview() {
    val resena = Resena(1, 1, 1, "Excelente guía", "2026-11-22")
    val libro = Libro(1, "978-84-01-0001-1", "Clean Code", "Robert C. Martin", "Software", 3, false)
    ResenasScreen(libro, listOf(resena), "Muy útil", {}, {}, {})
}
