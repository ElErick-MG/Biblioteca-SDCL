package ec.edu.bibliosec.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.bibliosec.domain.Libro
import ec.edu.bibliosec.domain.Prestamo

@Composable
fun AdminScreen(
    pendientes: List<Prestamo>,
    aprobados: List<Prestamo>,
    libros: List<Libro>,
    titulos: Map<Long, String>,
    onAprobar: (Long) -> Unit,
    onRechazar: (Long) -> Unit,
    onDevolver: (Long) -> Unit,
    onRegistrar: (String, String, String, String, Long, Boolean) -> Unit,
    onVolver: () -> Unit,
) {
    val pestaña = remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Panel del bibliotecario", style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = onVolver) { Text("Volver al catálogo") }
        Row {
            Button(onClick = { pestaña.value = 0 }) { Text("Pendientes") }
            Button(onClick = { pestaña.value = 1 }) { Text("Libros") }
            Button(onClick = { pestaña.value = 2 }) { Text("Devoluciones") }
        }

        when (pestaña.value) {
            0 -> LazyColumn {
                items(pendientes, key = { it.id }) { prestamo ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(titulos[prestamo.libroId] ?: "Libro ${prestamo.libroId}")
                            Text("${prestamo.codigoEstudiante} · ${prestamo.dias} días")
                            Row {
                                Button(onClick = { onAprobar(prestamo.id) }) { Text("Aprobar") }
                                TextButton(onClick = { onRechazar(prestamo.id) }) { Text("Rechazar") }
                            }
                        }
                    }
                }
            }

            1 -> LibrosTab(libros, onRegistrar)

            else -> LazyColumn {
                items(aprobados, key = { it.id }) { prestamo ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(titulos[prestamo.libroId] ?: "Libro ${prestamo.libroId}", modifier = Modifier.weight(1f))
                            Button(onClick = { onDevolver(prestamo.id) }) { Text("Marcar devuelto") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibrosTab(
    libros: List<Libro>,
    onRegistrar: (String, String, String, String, Long, Boolean) -> Unit,
) {
    val isbn = remember { mutableStateOf("") }
    val titulo = remember { mutableStateOf("") }
    val autor = remember { mutableStateOf("") }
    val categoria = remember { mutableStateOf("") }
    val copias = remember { mutableStateOf("1") }
    val restringido = remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(value = isbn.value, onValueChange = { isbn.value = it }, label = { Text("ISBN") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = titulo.value, onValueChange = { titulo.value = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = autor.value, onValueChange = { autor.value = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = categoria.value, onValueChange = { categoria.value = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = copias.value, onValueChange = { copias.value = it }, label = { Text("Copias") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = restringido.value, onCheckedChange = { restringido.value = it })
            Text("Restringido / uso interno")
        }
        Button(
            onClick = {
                onRegistrar(
                    isbn.value, titulo.value, autor.value, categoria.value,
                    copias.value.toLongOrNull() ?: 1L, restringido.value,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Registrar libro") }

        LazyColumn {
            items(libros, key = { it.id }) { libro ->
                Text("${libro.titulo}" + if (libro.restringido) " · RESTRINGIDO" else "")
            }
        }
    }
}

@Preview
@Composable
private fun AdminPreview() {
    AdminScreen(emptyList(), emptyList(), emptyList(), emptyMap(), {}, {}, {}, { _, _, _, _, _, _ -> }, {})
}
