package ec.edu.bibliosec.ui.comprobante

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** JVM no tiene WebView: se muestra el HTML como texto (solo preview/tests). */
@Composable
actual fun ComprobanteWebView(html: String, modifier: Modifier) {
    Text(text = html, modifier = modifier)
}
