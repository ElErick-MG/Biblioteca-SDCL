package ec.edu.bibliosec.ui.comprobante

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/** WebView del comprobante. JavaScript y acceso a archivos deshabilitados (VULN-03). */
@Composable
actual fun ComprobanteWebView(html: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
                settings.allowFileAccess = false
                settings.allowContentAccess = false
                loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            }
        },
        update = { view -> view.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null) },
    )
}
