# VULN-03 — XSS en comprobante y reseñas

**Estado:** HTML crudo verificado por test; ejecución en WebView con JS activo verificada por configuración y SAST. Captura de `alert` en dispositivo: pendiente.

## Payload

```html
<img src=x onerror="alert('XSS')">
```

## Código vulnerable

- `ui/vulnerable/ComprobanteHtmlVulnerable.kt`: construye el ticket y las tarjetas de reseñas sin escapar.
- `androidMain/ui/vulnerable/WebViewVulnerable.kt:8`: `javaScriptEnabled = true`.

```kotlin
// VULN-03: JavaScript y acceso a archivos habilitados en la variante vulnerable.
javaScriptEnabled = true
```

## Comando ejecutado

```bash
./gradlew jvmTest --tests "ec.edu.bibliosec.VulnerabilidadTest"
```

## Resultado real

`VULN-03 el HTML del comprobante no escapa el payload` → **PASADO**: el payload queda tal cual en el HTML.

## SAST

Semgrep `android-webview-js-enabled` en `WebViewVulnerable.kt:8` → `docs/reportes/semgrep.json`.

## Mitigación (F5)

`HtmlSanitizer.escape()` + `javaScriptEnabled=false` + CSP `default-src 'none'`. Re-test en F8.
