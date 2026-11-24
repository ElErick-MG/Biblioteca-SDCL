package ec.edu.bibliosec.ui.vulnerable

import android.webkit.WebView

/** VULN-03: JavaScript y acceso a archivos habilitados en la variante vulnerable. */
fun configurarWebViewVulnerable(webView: WebView) {
    webView.settings.apply {
        javaScriptEnabled = true
        allowFileAccess = true
        allowContentAccess = true
    }
}
