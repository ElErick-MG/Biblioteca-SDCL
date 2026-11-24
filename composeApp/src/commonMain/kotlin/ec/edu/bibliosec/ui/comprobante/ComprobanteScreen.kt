package ec.edu.bibliosec.ui.comprobante

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.bibliosec.security.HtmlSanitizer

@Preview
@Composable
private fun ComprobantePreview() {
    ComprobanteScreen(
        libro = "Clean Code",
        usuario = "Ana Lector",
        dias = 7,
        observacion = "Para el laboratorio",
        estado = "PENDIENTE",
        onVolver = {},
    )
}

@Composable
fun ComprobanteScreen(
    libro: String,
    usuario: String,
    dias: Long,
    observacion: String,
    estado: String,
    onVolver: () -> Unit,
) {
    val html = """
        <html><head><meta charset="utf-8">
        <meta http-equiv="Content-Security-Policy" content="default-src 'none'; style-src 'unsafe-inline'">
        <style>body{font-family:sans-serif;padding:16px}.t{border:1px solid #888;border-radius:8px;padding:12px}</style>
        </head><body><div class="t">
        <h2>Comprobante de préstamo</h2>
        <p><b>Libro:</b> ${HtmlSanitizer.escape(libro)}</p>
        <p><b>Lector:</b> ${HtmlSanitizer.escape(usuario)}</p>
        <p><b>Días:</b> $dias</p>
        <p><b>Estado:</b> ${HtmlSanitizer.escape(estado)}</p>
        <p><b>Observación:</b> ${HtmlSanitizer.escape(observacion)}</p>
        </div></body></html>
    """.trimIndent()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Comprobante digital", style = MaterialTheme.typography.headlineSmall)
        TextButton(onClick = onVolver) { Text("Volver al catálogo") }
        ComprobanteWebView(
            html = html,
            modifier = Modifier.fillMaxWidth().height(320.dp),
        )
    }
}
