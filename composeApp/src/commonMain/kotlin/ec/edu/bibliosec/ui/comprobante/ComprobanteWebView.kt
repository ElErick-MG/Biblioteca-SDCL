package ec.edu.bibliosec.ui.comprobante

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Visor de comprobante: WebView en Android, texto en JVM (F2). */
@Composable
expect fun ComprobanteWebView(html: String, modifier: Modifier = Modifier)
