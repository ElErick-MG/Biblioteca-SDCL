package ec.edu.bibliosec.ui.catalogo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun CatalogoScreen(
    libros: List<Libro>,
    busqueda: String,
    onBuscar: (String) -> Unit,
    onSolicitar: (Long) -> Unit,
    onResenas: (Long) -> Unit,
    onMisPrestamos: () -> Unit,
    onCerrarSesion: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Catálogo de libros", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = busqueda,
            onValueChange = onBuscar,
            label = { Text("Buscar por título, autor o ISBN") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Row {
            TextButton(onClick = onMisPrestamos) { Text("Mis préstamos") }
            TextButton(onClick = onCerrarSesion) { Text("Salir") }
        }
        LazyColumn {
            items(libros, key = { it.id }) { libro ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(libro.titulo, style = MaterialTheme.typography.titleMedium)
                        Text("${libro.autor} · ${libro.categoria}")
                        Text("Copias: ${libro.copiasDisponibles}" + if (libro.restringido) " · RESTRINGIDO" else "")
                        Row {
                            Button(onClick = { onSolicitar(libro.id) }) { Text("Solicitar préstamo") }
                            TextButton(onClick = { onResenas(libro.id) }) { Text("Reseñas") }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CatalogoPreview() {
    val libro = Libro(1, "978-84-01-0001-1", "Clean Code", "Robert C. Martin", "Software", 3, false)
    CatalogoScreen(
        libros = listOf(libro),
        busqueda = "",
        onBuscar = {},
        onSolicitar = {},
        onResenas = {},
        onMisPrestamos = {},
        onCerrarSesion = {},
    )
}
