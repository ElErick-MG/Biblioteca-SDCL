# Índice de evidencias

Todas las pruebas se ejecutaron **solo en entorno local** (BD SQLite en memoria y manifiestos empaquetados). No se apuntó ningún payload a sistemas de terceros. No había dispositivo/emulador conectado (`adb devices` → lista vacía), por lo que las capturas de pantalla y los volcados ADB quedan pendientes para la defensa en vivo.

| VULN | Evidencia | Estado |
| :--- | :--- | :--- |
| VULN-01 | [`VULN-01/evidencia.md`](VULN-01/evidencia.md) | ✅ Explotación verificada por test |
| VULN-02 | [`VULN-02/evidencia.md`](VULN-02/evidencia.md) | ✅ Explotación verificada por test |
| VULN-03 | [`VULN-03/evidencia.md`](VULN-03/evidencia.md) | ✅ HTML crudo + WebView JS on |
| VULN-04 | [`VULN-04/evidencia.md`](VULN-04/evidencia.md) | ✅ Explotación verificada por test |
| VULN-05 | [`VULN-05/evidencia.md`](VULN-05/evidencia.md) | ✅ Sesión en plano; ADB pendiente |
| VULN-06 | [`VULN-06/evidencia.md`](VULN-06/evidencia.md) | ✅ Manifiesto vulnerable verificado |
| VULN-07 | [`VULN-07/evidencia.md`](VULN-07/evidencia.md) | ✅ Manifiesto + log code; logcat pendiente |
| VULN-08 | [`VULN-08/evidencia.md`](VULN-08/evidencia.md) | 🟨 `jadx` no disponible |
| VULN-09 | [`VULN-09/evidencia.md`](VULN-09/evidencia.md) | ✅ Explotación verificada por test |

Reportes SAST en [`../reportes/`](../reportes/).
