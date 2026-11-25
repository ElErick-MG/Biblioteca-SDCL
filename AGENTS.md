# AGENTS.md — Reglas de trabajo para agentes de IA

> **Proyecto:** `BiblioSec` — Sistema de biblioteca móvil con laboratorio de vulnerabilidades (versión *vulnerable* vs. versión *segura*).
> **Audiencia:** cualquier agente de IA (Google Antigravity / VS Code / CLI) que trabaje en este repositorio.
> **Estado verificado:** 25/11/2026 — **F0…F4 completadas**: 14 tests en verde, matriz exportada, Semgrep (7), Detekt (11) y MobSF ejecutado (vulnerable 50 → segura 60). Falta exportar `out/` de `jadx` (VULN-08). F5 pendiente.

---

## 1. Orden de lectura obligatorio al entrar al proyecto

1. **Este archivo** (`AGENTS.md`) → qué hacer y cómo trabajar.
2. `docs/ContextoProyecto.md` → qué proyecto es y dónde estamos parados.
3. `docs/Requisitos.md` → qué debe cumplir la aplicación.
4. `docs/Arquitectura.md` → cómo está construida / cómo se construirá.
5. `docs/SeguimientoEntregables.md` → **documento vivo**: matriz y entregables alimentados por fase según la rúbrica.
6. `.docs/sistemaBiblioteca.md` → **especificación maestra**, fuente de verdad de las 13 secciones (fases, VULN, matriz, código de referencia, rúbrica). `.docs/indicaciones.md` es el documento base de la asignatura.

> **Ojo con los nombres:** `.docs/` (con punto) es documentación personal, está en `.gitignore` y **no se versiona**. `docs/` (sin punto) es la carpeta de entregables del proyecto y **sí se versiona**. No las confundas ni muevas archivos entre ellas sin petición explícita.

---

## 2. Reglas de oro (no negociables)

1. **Trabaja fase por fase** (sección 4). No avances a la siguiente sin cumplir los criterios de aceptación de la actual.
2. **Aísla el código vulnerable del seguro.** Vulnerable solo en `...data.vulnerable` / `...ui.vulnerable`; seguro solo en `...data.segura` / `...ui.segura`. Lógica común y neutral en `commonMain`.
3. **Marca cada vulnerabilidad** con el comentario `// VULN-<n>: <descripción>` justo en el código vulnerable. Usa los IDs `VULN-01` … `VULN-09` de la especificación.
4. **Cada VULN se entrega con cuatro artefactos:** (a) implementación vulnerable, (b) payload/evidencia de explotación, (c) implementación segura, (d) re-test (manual + prueba automatizada cuando aplique).
5. **Evidencias en `docs/evidencias/VULN-0X/`**: capturas, logs, comandos usados y salida de herramientas. Los reportes van en `docs/reportes/`; la matriz en `docs/matriz-seguridad.md`.
6. **No inventes dependencias.** Usa las versiones definidas en `gradle/libs.versions.toml` (a crearse en F0) y verifica con `./gradlew assembleVulnerableDebug assembleSeguraDebug`.
7. **Idioma:** UI y modelo de dominio en **español**. Identificadores técnicos en inglés o español, pero **consistentes** en todo el proyecto.
8. **Entorno autorizado:** pentesting **solo** en emulador o dispositivo físico propio. **Prohibido** apuntar pruebas a sistemas de terceros.
9. **Sin Android Studio.** El flujo es JDK 17+ / Android SDK Command Line Tools / Gradle / Antigravity o VS Code.
10. **No modificar `.docs/`** salvo petición explícita del usuario; es la especificación de entrada, no un entregable.
11. **Verifica antes de decir "hecho"**: compila, ejecuta tests y revisa que el criterio de aceptación de la fase se cumpla de verdad.
12. **Trazabilidad:** al completar una fase o trabajar una VULN, actualiza `docs/ContextoProyecto.md` (estado de fases) y `docs/SeguimientoEntregables.md` (matriz, evidencias, métricas y registro de cambios). Usa commits convencionales (`fix(VULN-01): ...`, `test(VULN-03): ...`) y **un PR por vulnerabilidad**.

---

## 3. Convenciones de trabajo

| Tema | Convención |
| :--- | :--- |
| Paquete base | `ec.edu.bibliosec` |
| Gradle / AGP | AGP 9.0 con `android.builtInKotlin=false` y `android.newDsl=false` (modelo clásico, temporal hasta AGP 10); `compileSdk=36`, `targetSdk=35`, JVM target 17; `resValues=true` para `app_name` por flavor |
| Target JVM | `jvm()` sin UI, solo para `JdbcSqliteDriver` en memoria y tests (`jvmTest`); los `actual` de JVM viven en `jvmMain` |
| SQLDelight 2.4 | Los drivers están en `app.cash.sqldelight.driver.android.AndroidSqliteDriver` y `app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver`; la propiedad generada es `database.bibliotecaQueries` |
| UI / navegación | `ui/` con `Ruta` (sealed interface), `AppState` (Compose state) y `UiDataSource` temporal; las pantallas son stateless y llevan `@Preview` de `androidx.compose.ui.tooling.preview.Preview` con `compose.preview` |
| WebView | `expect @Composable ComprobanteWebView` en `commonMain`; actual Android decide por `BuildConfig.SECURE_MODE` entre `ui.vulnerable.configurarWebViewVulnerable` (JS on) y `ui.segura.configurarWebViewSegura` (JS off); actual JVM muestra el HTML como texto |
| Código vulnerable | Todo en paquetes `...data.vulnerable` / `...ui.vulnerable` y marcado con `// VULN-n`; `ServiceLocator.inicializar(context, db)` inyecta esos repositorios solo en el flavor vulnerable |
| Manifiesto vulnerable | Overlay en `src/vulnerable/AndroidManifest.xml` (NO `src/androidVulnerable/`): `allowBackup=true`, `usesCleartextTraffic=true`, INTERNET y `network_security_config` con cleartext |
| Auditoría | Semgrep con `p/kotlin` + `.semgrep/biblio-rules.yml` (con `PYTHONUTF8=1` en Windows); Detekt con source sets KMP declarados manualmente; MobSF vía Docker (`bibliosec-mobsf`, API key `bibliosec_mobsf_f4_2026`, endpoints `/api/v1/upload`, `/scan`, `/report_json`, `/download_pdf`). Reportes en `docs/reportes/`; evidencias en `docs/evidencias/VULN-0X/` |
| Módulo Gradle | `composeApp/` (único módulo de aplicación) |
| Dominio | `Usuario`, `Libro`, `Prestamo`, `Resena`, `Rol`, `EstadoPrestamo` (modelos en `domain/`) |
| Repositorios | Interfaces en `data/repo/`; implementaciones en `data/vulnerable/` y `data/segura/` |
| Inyección | `ServiceLocator` en `di/` elige implementación según `BuildConfig.SECURE_MODE` (vía `expect/actual`) |
| Seguridad compartida | `security/`: `HtmlSanitizer`, `PasswordHasher` (expect), `SessionStore` (expect), `AccessPolicy`, `AppLogger` |
| Validación | `validation/Validators.kt` con `ValidationResult` (sellado: `Ok` / `Error`) y allowlists, rangos y longitudes |
| Los roles | `LECTOR` y `BIBLIOTECARIO`. El rol se lee de la BD por `userId`; **nunca** de preferencias editables |
| Navegación | Rutas protegidas por `AccessPolicy` en navegación **y** casos de uso **y** repositorio |
| WebView | `javaScriptEnabled = false`, `allowFileAccess = false`, `allowContentAccess = false`, CSP `default-src 'none'` y HTML escapado con `HtmlSanitizer.escape()` |
| Logs | Solo `AppLogger`; **nunca** `Log.*`/`println` con contraseñas, tokens ni datos personales. No-op en release |
| Manifiesto | Segura: `allowBackup="false"`, `usesCleartextTraffic="false"`, `networkSecurityConfig`, `dataExtractionRules`. Componentes `exported="false"` salvo `MainActivity` (LAUNCHER) |
| Contraseñas | Vulnerable: texto plano. Segura: PBKDF2WithHmacSHA256, ≥120 000 iteraciones, salt 16 bytes, formato `iter:saltB64:hashB64` |
| Sesión | Guardar solo `userId`, `rol` y expiración; cifrada con `EncryptedSharedPreferences` (Keystore) en la versión segura |

---

## 4. Roadmap por fases (estado actual)

| Fase | Tareas | Criterio de aceptación | Estado |
| :--- | :--- | :--- | :--- |
| **F0. Setup** | Proyecto KMP, `libs.versions.toml`, SQLDelight, Compose, flavors `vulnerable`/`segura`, `.gitignore`, README | `./gradlew assembleVulnerableDebug assembleSeguraDebug` compila; ambas apps se instalan juntas | ✅ COMPLETADA (24/11/2026) |
| **F1. Dominio y BD** | Modelos, `Biblioteca.sq`, `DriverFactory`, seed, interfaces de repositorio, `ServiceLocator` | BD con seed (3 usuarios, 13 libros, 3 restringidos, 2 préstamos, 2 reseñas) | ✅ COMPLETADA (24/11/2026) |
| **F2. UI base** | Login, Catálogo, Solicitud, Comprobante, Reseñas, Mis préstamos, Panel admin + `@Preview` | Navegación completa por rol (lector → catálogo, bibliotecario → admin) | ✅ COMPLETADA (24/11/2026) |
| **F3. Implementación vulnerable** | Repositorios, sesión insegura, logs, manifiesto inseguro, WebView sin sanitizar, sin validación. Marcar `// VULN-n` | Payloads VULN-01/02/03/04/05/09 verificados por tests; VULN-06/07 verificados en manifiesto. Evidencia gráfica en F4 | ✅ COMPLETADA (24/11/2026) |
| **F4. Matriz y pentesting** | Ejecutar ataques, evidencias, matriz, MobSF/Semgrep/Detekt sobre `lab-vulnerable` | Matriz exportada + 8/9 evidencias + Semgrep/Detekt + MobSF 50→60 (VULN-08 pendiente de `out/`) | ✅ COMPLETADA (25/11/2026) |
| **F5. Implementación segura** | Repositorios seguros, validadores, sanitizador, hashing, `EncryptedSharedPreferences`, `AccessPolicy`, manifiesto seguro, R8, `AppLogger` | Los mismos payloads fallan en la app segura | ⬜ PENDIENTE |
| **F6. Pruebas** | `commonTest` completo (sección 9) | `./gradlew allTests` en verde; cobertura ≥80 % en `validation/` y `security/` | ⬜ PENDIENTE |
| **F7. CI/CD** | `devsecops.yml`, Detekt, reglas Semgrep, ramas y PRs por VULN | Pipeline rojo en `lab-vulnerable`, verde en `main`; APK descargable | ⬜ PENDIENTE |
| **F8. Re-test y reportes** | Repetir ataques sobre APK segura, MobSF y `jadx` comparativos, métricas | Evidencias "después" y tabla de métricas llena | ⬜ PENDIENTE |
| **F9. Informe y defensa** | Informe IEEE, diagrama final, presentación, ensayo con `scrcpy` | Informe en PDF; demo ensayada en <12 min | ⬜ PENDIENTE |

> El primer avance (13/11/2026) ya transcurrió: si no se entregó, prioriza F1–F4 como base de la entrega final (21–22 enero de 2027).

---

## 5. Comandos de referencia

```bash
# Compilación (ambas variantes deben coexistir)
./gradlew assembleVulnerableDebug assembleSeguraDebug

# Pruebas y análisis estático
./gradlew allTests
./gradlew detekt
semgrep scan --config p/kotlin --config p/android --config .semgrep/biblio-rules.yml \
  --sarif --output semgrep.sarif --error composeApp/src

# Instalación y proyección (dispositivo propio)
adb install -r composeApp/build/outputs/apk/vulnerable/debug/composeApp-vulnerable-debug.apk
adb install -r composeApp/build/outputs/apk/segura/debug/composeApp-segura-debug.apk
scrcpy

# Evidencias (solo entorno local)
adb logcat -s AUTH
adb shell run-as ec.edu.bibliosec.vulnerable ls databases shared_prefs
jadx -d out_vuln composeApp-vulnerable-release.apk
docker run -it --rm -p 8000:8000 opensecurity/mobile-security-framework-mobsf:latest
```

---

## 6. Definition of Done (antes de marcar una fase como completada)

- [ ] El criterio de aceptación de la fase se verificó con un comando o evidencia real.
- [ ] Compila `./gradlew assembleVulnerableDebug assembleSeguraDebug` y pasan `./gradlew allTests` / `detekt` según aplique.
- [ ] El código vulnerable está aislado y marcado con `// VULN-n`; el seguro no contiene atajos inseguros.
- [ ] Cada VULN tocada tiene sus cuatro artefactos y su carpeta de evidencia.
- [ ] No hay secretos reales, credenciales de terceros ni logs sensibles.
- [ ] `docs/ContextoProyecto.md` refleja el nuevo estado de la fase.
- [ ] `docs/SeguimientoEntregables.md` tiene actualizados el checklist, la matriz, las evidencias, las métricas y el registro de cambios (§7).

---

## 7. Prohibido expresamente

- Ejecutar ataques contra sistemas, IPs o dominios que no sean propios o de laboratorio local.
- Mezclar implementaciones vulnerable y segura en el mismo archivo/paquete.
- "Arreglar" una vulnerabilidad eliminando el código vulnerable: es material didáctico y debe conservarse en su variante.
- Añadir librerías sin declararlas en `libs.versions.toml` ni justificar su versión.
- Inventar evidencias: capturas, logs y puntajes deben provenir de ejecuciones reales.
- Borrar o reescribir `.docs/sistemaBiblioteca.md` sin instrucción explícita.

---

## 8. Mapa rápido de archivos

| Archivo / carpeta | Para qué sirve |
| :--- | :--- |
| `AGENTS.md` | Reglas de trabajo (este archivo) |
| `docs/ContextoProyecto.md` | Identidad, estado, cronograma, dominio, VULN y entregables |
| `docs/Requisitos.md` | Requisitos funcionales, no funcionales, de seguridad, datos, pruebas y CI/CD |
| `docs/Arquitectura.md` | Capas, módulos, flavors, BD, componentes de seguridad, decisiones |
| `docs/SeguimientoEntregables.md` | Documento vivo: matriz, checklist de entregables, evidencias, métricas y registro de cambios |
| `.docs/sistemaBiblioteca.md` | Especificación maestra (13 secciones) |
| `docs/matriz-seguridad.md` | Matriz de riesgos (a crear en F4) |
| `docs/evidencias/VULN-0X/` | Evidencia de explotación y re-test (a crear en F4/F8) |
| `docs/reportes/` | Salidas de Semgrep, Detekt y MobSF (a crear en F4/F7/F8) |
