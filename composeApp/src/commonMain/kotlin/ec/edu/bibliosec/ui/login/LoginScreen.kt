package ec.edu.bibliosec.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    esSeguro: Boolean,
    mensaje: String,
    onLogin: (String, String) -> Unit,
    onLoginAdmin: (String, String) -> Unit,
) {
    val usuario = remember { mutableStateOf("") }
    val clave = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("BiblioSec", style = MaterialTheme.typography.headlineMedium)
        Text(if (esSeguro) "Variante: SEGURA" else "Variante: VULNERABLE")
        Text("Demo: lector1/lector123 · biblio1/admin123", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = usuario.value,
            onValueChange = { usuario.value = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = clave.value,
            onValueChange = { clave.value = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        if (mensaje.isNotBlank()) {
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onLogin(usuario.value, clave.value) }, modifier = Modifier.fillMaxWidth()) {
            Text("Inicio de sesion usuario")
        }
        Button(
            onClick = { onLoginAdmin(usuario.value, clave.value) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Inicio de sesion admin")
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    LoginScreen(
        esSeguro = true,
        mensaje = "Credenciales inválidas",
        onLogin = { _, _ -> },
        onLoginAdmin = { _, _ -> },
    )
}
