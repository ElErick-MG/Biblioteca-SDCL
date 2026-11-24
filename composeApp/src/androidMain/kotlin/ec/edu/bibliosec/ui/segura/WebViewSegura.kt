package ec.edu.bibliosec.ui.segura

import android.webkit.WebView

/** Configuración endurecida del WebView (se usará también en F5). */
fun configurarWebViewSegura(webView: WebView) {
    webView.settings.apply {
        javaScriptEnabled = false
        allowFileAccess = false
        allowContentAccess = false
    }
}
