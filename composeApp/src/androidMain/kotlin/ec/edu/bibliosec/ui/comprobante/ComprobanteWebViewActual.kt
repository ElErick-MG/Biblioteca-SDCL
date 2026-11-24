package ec.edu.bibliosec.ui.comprobante

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import ec.edu.bibliosec.BuildConfig
import ec.edu.bibliosec.ui.segura.configurarWebViewSegura
import ec.edu.bibliosec.ui.vulnerable.configurarWebViewVulnerable

/** WebView del comprobante: configuración según el flavor (VULN-03). */
@Composable
actual fun ComprobanteWebView(html: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                if (BuildConfig.SECURE_MODE) {
                    configurarWebViewSegura(this)
                } else {
                    // VULN-03: JavaScript habilitado para demostrar XSS.
                    configurarWebViewVulnerable(this)
                }
                loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            }
        },
        update = { view -> view.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null) },
    )
}
