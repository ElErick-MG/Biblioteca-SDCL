# Matriz de Seguridad — BiblioSec

> **Version:** F4 (pentesting inicial sobre `lab-vulnerable`), 24/11/2026.
> **Origen:** sección §3 de [`SeguimientoEntregables.md`](SeguimientoEntregables.md), ampliada con los resultados reales de Semgrep, Detekt y las evidencias en [`evidencias/`](evidencias/).
> **Metodología obligatoria:** Identificar activos → Identificar amenazas → Detectar vulnerabilidades → Valorar riesgos → Proponer controles → Definir contingencia.

## 1. Inventario de activos

| ID | Activo | Tipo | Valor C/I/D |
| :--- | :--- | :--- | :--- |
| A1 | Base de datos SQLite local (`biblioteca.db`) | Datos | Alta / Alta / Media |
| A2 | Credenciales de usuarios (contraseñas) | Datos | Alta / Alta / Media |
| A3 | Sesión del usuario (token / preferencias) | Datos | Alta / Alta / Baja |
| A4 | Catálogo de libros restringidos | Información | Alta / Media / Baja |
| A5 | Registros de préstamos (datos personales + código de estudiante) | Datos personales | Alta / Alta / Media |
| A6 | Panel de administración y sus funciones | Funcionalidad | Alta / Alta / Media |
| A7 | Comprobante / reseñas renderizados en `WebView` | Componente | Media / Alta / Baja |
| A8 | Código fuente y binario `.apk` | Software | Media / Alta / Media |

\* C = Confidencialidad, I = Integridad, D = Disponibilidad.

## 2. Matriz de riesgos

Clasificación: **Bajo 1-6 · Medio 7-14 · Alto 15-25**. `P×I` es el riesgo inicial; `P×I residual` se completará en F8 tras aplicar los controles.

| # | Activo | Amenaza (STRIDE) | Vulnerabilidad (CWE / OWASP Mobile) | P | I | Riesgo | Control / Mitigación | Contingencia | Responsable | P×I residual | Estado | Evidencia |
| :-: | :--- | :--- | :--- | :-: | :-: | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | A1/A2 SQLite, credenciales | Spoofing, Elevation of Privilege | **SQLi en login** (CWE-89; M4/M7) · VULN-01 | 5 | 5 | **25 Alto** | Consultas parametrizadas (`WHERE username = ?`) + verificación de hash en código; error genérico; límite de intentos | Invalidar sesiones, restaurar BD desde copia, forzar cambio de contraseñas, parche inmediato | Por asignar | — | ✅ | [`evidencias/VULN-01/`](evidencias/VULN-01/evidencia.md) |
| 2 | A4 Libros restringidos | Information Disclosure | **SQLi en buscador** (CWE-89): `%' OR restringido=1 --` · VULN-02 | 4 | 4 | **16 Alto** | Query parametrizada + filtro `restringido = 0` por rol en repositorio; longitud ≤ 60 | Auditar accesos, rotar/reclasificar libros expuestos, parche | Por asignar | — | ✅ | [`evidencias/VULN-02/`](evidencias/VULN-02/evidencia.md) |
| 3 | A7 `WebView` | Tampering, Information Disclosure | **XSS** en comprobante y reseñas (CWE-79) · VULN-03 | 4 | 4 | **16 Alto** | `HtmlSanitizer.escape()` + `javaScriptEnabled=false` + CSP `default-src 'none'` | Purgar registros contaminados, eliminar reseñas maliciosas, parche | Por asignar | — | ✅ | [`evidencias/VULN-03/`](evidencias/VULN-03/evidencia.md) |
| 4 | A5 Préstamos | Tampering, Denial of Service | **Input Validation** deficiente (CWE-20) · VULN-04 | 5 | 3 | **15 Alto** | Validadores allowlist/rangos en `commonMain` + tests + `CHECK` en BD | Revertir registros inválidos, reglas de integridad en BD | Por asignar | — | ✅ | [`evidencias/VULN-04/`](evidencias/VULN-04/evidencia.md) |
| 5 | A2/A3 Credenciales y sesión | Information Disclosure | **Almacenamiento inseguro** (CWE-256/312; M9) · VULN-05 | 4 | 5 | **20 Alto** | PBKDF2/SHA-256 + salt; `EncryptedSharedPreferences` (Keystore) | Forzar reset masivo de contraseñas y cierre de sesiones | Por asignar | — | ✅ | [`evidencias/VULN-05/`](evidencias/VULN-05/evidencia.md) |
| 6 | A1 Comunicaciones | Information Disclosure (MitM) | **Cleartext traffic** (CWE-319; M5) · VULN-06 | 2 | 3 | **6 Bajo** | `usesCleartextTraffic="false"` + `network_security_config` restrictiva | Bloquear dominios, rotar secretos expuestos | Por asignar | — | ✅ | [`evidencias/VULN-06/`](evidencias/VULN-06/evidencia.md) |
| 7 | A1/A3 Datos locales y logs | Information Disclosure | **Backup habilitado + logs sensibles** (CWE-532/921; M9) · VULN-07 | 4 | 4 | **16 Alto** | `allowBackup="false"` + `dataExtractionRules` + eliminar logs en release (`AppLogger`) | Revocar credenciales filtradas, depurar logs | Por asignar | — | ✅ | [`evidencias/VULN-07/`](evidencias/VULN-07/evidencia.md) |
| 8 | A8 `.apk` | Information Disclosure, Tampering | **Ingeniería inversa** sin ofuscación (CWE-693; M7) · VULN-08 | 4 | 3 | **12 Medio** | R8/ProGuard (`isMinifyEnabled`, `isShrinkResources`) | Rotar constantes/claves expuestas y republicar build | Por asignar | — | 🟨 | [`evidencias/VULN-08/`](evidencias/VULN-08/evidencia.md) |
| 9 | A6 Panel admin | Elevation of Privilege | **Control de acceso roto** (CWE-862/285; M3) · VULN-09 *(bonus)* | 4 | 5 | **20 Alto** | `AccessPolicy` + RBAC en UI **y** repositorio/casos de uso; rol leído de BD | Auditar acciones administrativas, revertir aprobaciones indebidas | Por asignar | — | ✅ | [`evidencias/VULN-09/`](evidencias/VULN-09/evidencia.md) |

## 3. Resultados SAST/DAST de F4

| Herramienta | Comando | Hallazgos | Reporte |
| :--- | :--- | :--- | :--- |
| Semgrep | `semgrep scan --config p/kotlin --config .semgrep/biblio-rules.yml --error composeApp/src` | **7** (4 SQLi, 1 WebView JS, 1 log sensible, 1 SharedPreferences password) | [`semgrep.json`](reportes/semgrep.json), [`semgrep.sarif`](reportes/semgrep.sarif) |
| Detekt | `./gradlew detekt` | **11** (estilo/complejidad sobre KMP; la config excluye marcadores `VULN-` obligatorios) | [`detekt.html`](reportes/detekt.html), [`detekt.sarif`](reportes/detekt.sarif) |
| MobSF | `docker run -p 8000:8000 opensecurity/mobile-security-framework-mobsf:latest` | **No ejecutado**: daemon Docker no disponible | [`mobsf-estado.md`](reportes/mobsf-estado.md) |

> Nota: `p/android` devolvió HTTP 404 en el registro de Semgrep, por lo que se usó `p/kotlin` + reglas personalizadas. El comparativo antes/después y el puntaje MobSF se completan en F8.

## 4. Trazabilidad rápida

`Activo → Amenaza → Vulnerabilidad → Riesgo → Control → Contingencia → Evidencia`: cada fila de la sección 2 cumple las 7 columnas y apunta a una carpeta de evidencia. Las contingencias están prellenadas; el riesgo residual y el re-test se completan en F8.
