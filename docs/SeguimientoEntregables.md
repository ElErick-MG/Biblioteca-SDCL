# Seguimiento de Entregables y Matriz de Seguridad — Documento vivo

> **Propósito:** concentrar en un solo lugar el avance de la **matriz de seguridad** y de **todos los entregables exigidos por la rúbrica**, para ir alimentándolos fase por fase sin perder trazabilidad.
> **Estado verificado:** 24/11/2026 — F0 pendiente; todo lo demás está en borrador.
> **Reglas de trabajo:** [`../AGENTS.md`](../AGENTS.md) · **Contexto:** [`ContextoProyecto.md`](ContextoProyecto.md) · **Requisitos:** [`Requisitos.md`](Requisitos.md) · **Arquitectura:** [`Arquitectura.md`](Arquitectura.md) · **Especificación maestra:** `.docs/sistemaBiblioteca.md`.

---

## 0. Cómo alimentar este documento (protocolo para agentes)

1. **Después de cada fase o de cada trabajo sobre una VULN**, actualiza: la fila correspondiente de la matriz (§3), el índice de evidencias (§4), el checklist de entregables (§2), la tabla de métricas (§5) y el registro de cambios (§7).
2. **Estado de la matriz:** `⬜ Pendiente` → `🟨 En progreso` → `✅ Verificado`. **Nunca marcar ✅ sin evidencia real** guardada en `docs/evidencias/VULN-0X/` o `docs/reportes/`.
3. **Cada fila de la matriz debe mantenerse trazable:** Activo → Amenaza → Vulnerabilidad → Riesgo → Control → Contingencia → Evidencia. Si cambias un valor (P, I, control), anótalo en §7 con la razón.
4. **Riesgo inicial vs. residual:** la columna `P×I` es el riesgo *antes* del control; `P×I residual` se calcula después de aplicar la mitigación y se completa en F8. Clasificación: Bajo 1-6 · Medio 7-14 · Alto 15-25.
5. **Cuando llegue F4**, exporta la sección §3 a `docs/matriz-seguridad.md` (entregable formal de la rúbrica). Este archivo sigue siendo el tablero de control.
6. **Cuando llegue F9**, las secciones §3, §5 y §6 alimentan el informe IEEE: matriz (sección 6), pentesting y resultados (7), mitigación y re-test (8), métricas (10).
7. **No inventes resultados.** Si un dato aún no se midió, déjalo como `Sin medir` o `—`.
8. Todo lo que se escriba aquí debe poder verificarse con un comando, una captura, un log o un reporte almacenado.

**Leyenda:** ⬜ Pendiente · 🟨 En progreso · ✅ Verificado · ⛔ Bloqueado

---

## 1. Mapa rúbrica → entregables (10,0 puntos)

| # | Criterio de la rúbrica | Puntos | Dónde se demuestra (evidencia) | Entregable / archivo | Fase | Estado |
| :-: | :--- | :---: | :--- | :--- | :--- | :--- |
| 1 | Análisis de activos, amenazas, vulnerabilidades, riesgos y matriz | **2,0** | Inventario A1–A8 (§3.1), STRIDE, CWE/OWASP, P×I, matriz ampliada | `docs/matriz-seguridad.md` (desde §3 de este archivo) | F4 | ⬜ |
| 2 | Pentesting controlado (SQLi, XSS, Input Validation y controles móviles) | **2,0** | Payloads ejecutados y capturados por VULN | `docs/evidencias/VULN-01 … VULN-09/` | F4/F8 | ⬜ |
| 3 | Diseño/arquitectura y aplicación de controles | **1,5** | `commonMain`, `AccessPolicy`, flavors, repositorios seguros, diagrama | `docs/Arquitectura.md` + código + diagrama Mermaid | F1/F2/F5 | ⬜ |
| 4 | Corrección, mitigación, contingencia y re-test | **1,5** | Columna *Contingencia* (§3) + re-test manual y automatizado | §3, §4 y §6 de este archivo | F5/F6/F8 | ⬜ |
| 5 | Reporte de análisis de seguridad (SAST/DAST) | **1,0** | Semgrep + Detekt + MobSF, comparativo antes/después | `docs/reportes/` | F4/F7/F8 | ⬜ |
| 6 | Pipeline CI/CD básico | **1,0** | `devsecops.yml`: rojo en `lab-vulnerable`, verde en `main`; APK descargable | `.github/workflows/devsecops.yml` + capturas de Actions | F7 | ⬜ |
| 7 | Informe IEEE, presentación y defensa | **1,0** | Informe PDF con 11 secciones + demo en vivo <12 min con `scrcpy` | `docs/informe-ieee/` + presentación + guion (§6) | F9 | ⬜ |

> **Nota:** las VULN-01…08 cubren las 7 vulnerabilidades exigidas (SQLi dividida en login y búsqueda); VULN-09 es bonus.

---

## 2. Checklist maestro de entregables

| ID | Entregable | Archivo / ubicación | Criterio de aceptación | Fase | Estado | Última actualización |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| E-01 | Repositorio con código e historial de correcciones | GitHub (`lab-vulnerable`, `main`) | Ambas ramas, commits convencionales, PR por VULN, tags `v0.1-vulnerable` / `v1.0-segura` | F0/F7 | ⬜ | — |
| E-02 | Diagrama de arquitectura / topología | `docs/Arquitectura.md` §3 + `docs/arquitectura.md` | Mermaid adaptado al flujo local y CI/CD, incluido en el informe | F1/F9 | 🟨 | 24/11/2026 (diagrama listo en Arquitectura.md §3; falta incluirlo en el informe F9) |
| E-03 | Matriz de seguridad completa | `docs/matriz-seguridad.md` (borrador en §3) | Columnas completas, 9 filas, trazabilidad y riesgo inicial/residual | F4 | ⬜ | 24/11/2026 (borrador inicial) |
| E-04 | Evidencias de explotación, mitigación y re-test | `docs/evidencias/VULN-0X/` | 4 artefactos por VULN + antes/después (ver §4) | F4/F8 | ⬜ | — |
| E-05 | Reportes SAST/DAST antes y después | `docs/reportes/` | `semgrep.sarif`, `detekt.html`, `mobsf.pdf` Comparativa vulnerable vs. segura | F4/F7/F8 | ⬜ | — |
| E-06 | Pipeline CI/CD funcionando | `.github/workflows/devsecops.yml` | Capturas: rojo en `lab-vulnerable`, verde en `main`; APK descargable | F7 | ⬜ | — |
| E-07 | Demo en vivo con ambas apps | Dispositivo + `scrcpy` | 8 pasos del guion (§6), <12 min, ambas apps instaladas | F9 | ⬜ | — |
| E-08 | Informe técnico IEEE | `docs/informe-ieee/` + PDF | 11 secciones + referencias; incluye matriz, diagrama y métricas | F9 | ⬜ | — |
| E-09 | README con credenciales de demo | `README.md` | Usuarios/contraseñas documentados y marcados **solo laboratorio** | F0 | ✅ | 24/11/2026 |
| E-10 | Documentación de onboarding para agentes | `AGENTS.md`, `docs/ContextoProyecto.md`, `docs/Requisitos.md`, `docs/Arquitectura.md`, este archivo | Cuatro preguntas respondidas: cómo trabajar, dónde estamos, qué cumplir, cómo se construye | — | ✅ | 24/11/2026 |

---

## 3. Matriz de seguridad (borrador vivo → exportar a `docs/matriz-seguridad.md` en F4)

**Columnas obligatorias:** Activo · Amenaza (STRIDE) · Vulnerabilidad (CWE / OWASP Mobile) · Probabilidad (1-5) · Impacto (1-5) · Riesgo P×I · Control / Mitigación · Contingencia · Responsable · Estado · Evidencia.

| # | Activo | Amenaza (STRIDE) | Vulnerabilidad | P | I | Riesgo | Control / Mitigación | Contingencia | Responsable | P×I residual | Estado | Evidencia |
| :-: | :--- | :--- | :--- | :-: | :-: | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | A1/A2 SQLite, credenciales | Spoofing, Elevation of Privilege | **SQLi en login** (CWE-89; M4/M7) · VULN-01 | 5 | 5 | **25 Alto** | Consultas parametrizadas (`WHERE username = ?`) + verificación de hash en código; error genérico; límite de intentos | Invalidar sesiones, restaurar BD desde copia, forzar cambio de contraseñas, parche inmediato | Por asignar | — | ⬜ | `VULN-01/` |
| 2 | A4 Libros restringidos | Information Disclosure | **SQLi en buscador** (CWE-89): `%' OR restringido=1 --`, `UNION SELECT` · VULN-02 | 4 | 4 | **16 Alto** | Query parametrizada + filtro `restringido = 0` por rol en repositorio; longitud ≤ 60 | Auditar accesos, rotar/reclasificar libros expuestos, parche | Por asignar | — | ⬜ | `VULN-02/` |
| 3 | A7 `WebView` | Tampering, Information Disclosure | **XSS** en comprobante y reseñas (CWE-79) · VULN-03 | 4 | 4 | **16 Alto** | `HtmlSanitizer.escape()` + `javaScriptEnabled=false` + CSP `default-src 'none'` | Purgar registros contaminados, eliminar reseñas maliciosas, parche | Por asignar | — | ⬜ | `VULN-03/` |
| 4 | A5 Préstamos | Tampering, Denial of Service | **Input Validation** deficiente (CWE-20) · VULN-04 | 5 | 3 | **15 Alto** | Validadores allowlist/rangos en `commonMain` + tests + `CHECK` en BD | Revertir registros inválidos, reglas de integridad en BD | Por asignar | — | ⬜ | `VULN-04/` |
| 5 | A2/A3 Credenciales y sesión | Information Disclosure | **Almacenamiento inseguro** (CWE-256/312; M9) · VULN-05 | 4 | 5 | **20 Alto** | PBKDF2/SHA-256 + salt; `EncryptedSharedPreferences` (Keystore) | Forzar reset masivo de contraseñas y cierre de sesiones | Por asignar | — | ⬜ | `VULN-05/` |
| 6 | A1 Comunicaciones | Information Disclosure (MitM) | **Cleartext traffic** (CWE-319; M5) · VULN-06 | 2 | 3 | **6 Bajo** | `usesCleartextTraffic="false"` + `network_security_config` | Bloquear dominios, rotar secretos expuestos | Por asignar | — | ⬜ | `VULN-06/` |
| 7 | A1/A3 Datos locales y logs | Information Disclosure | **Backup habilitado + logs sensibles** (CWE-532/921; M9) · VULN-07 | 4 | 4 | **16 Alto** | `allowBackup="false"` + `dataExtractionRules` + eliminar logs en release (`AppLogger`) | Revocar credenciales filtradas, depurar logs | Por asignar | — | ⬜ | `VULN-07/` |
| 8 | A8 `.apk` | Information Disclosure, Tampering | **Ingeniería inversa** sin ofuscación (CWE-693; M7) · VULN-08 | 4 | 3 | **12 Medio** | R8/ProGuard (`isMinifyEnabled`, `isShrinkResources`) | Rotar constantes/claves expuestas y republicar build | Por asignar | — | ⬜ | `VULN-08/` |
| 9 | A6 Panel admin | Elevation of Privilege | **Control de acceso roto** (CWE-862/285; M3) · VULN-09 *(bonus)* | 4 | 5 | **20 Alto** | `AccessPolicy` + RBAC en UI **y** repositorio/casos de uso; rol leído de BD | Auditar acciones administrativas, revertir aprobaciones indebidas | Por asignar | — | ⬜ | `VULN-09/` |

### 3.1 Inventario de activos (A1–A8)

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

### 3.2 Ciclo metodológico por hallazgo (obligatorio)

`Identificar activos → Identificar amenazas → Detectar vulnerabilidades → Valorar riesgos → Proponer controles → Definir contingencia`

Antes de marcar una fila en ✅, verifica que las 6 etapas estén documentadas en la evidencia de esa VULN.

---

## 4. Índice de evidencias por VULN (4 artefactos obligatorios)

Ruta base: `docs/evidencias/VULN-XX/`. Nombres de archivo sugeridos (ajustar según el artefacto real).

| VULN | (a) Código vulnerable | (b) Payload / explotación | (c) Implementación segura | (d) Re-test manual + automático | Estado |
| :--- | :--- | :--- | :--- | :--- | :--- |
| VULN-01 | ⬜ `01-codigo-vulnerable.md` | ⬜ `02-payload.md` + capturas | ⬜ `03-mitigacion.md` | ⬜ `04-retest.md` + `ValidatorsTest`/`SqlInjectionTest` | ⬜ |
| VULN-02 | ⬜ ídem | ⬜ `%' OR restringido=1 --`, `UNION SELECT` | ⬜ ídem | ⬜ ídem | ⬜ |
| VULN-03 | ⬜ ídem | ⬜ `<img src=x onerror=...>` | ⬜ ídem | ⬜ `HtmlSanitizerTest` | ⬜ |
| VULN-04 | ⬜ ídem | ⬜ días `-5`/`9999`, obs. 50 000 chars | ⬜ ídem | ⬜ `ValidatorsTest` | ⬜ |
| VULN-05 | ⬜ ídem | ⬜ `adb shell run-as ...` | ⬜ ídem | ⬜ `PasswordHasherTest` | ⬜ |
| VULN-06 | ⬜ ídem | ⬜ captura HTTP local | ⬜ ídem | ⬜ MobSF + `CLEARTEXT ... not permitted` | ⬜ |
| VULN-07 | ⬜ ídem | ⬜ `adb logcat -s AUTH`, `adb backup` | ⬜ ídem | ⬜ log sin credenciales + backup vacío | ⬜ |
| VULN-08 | ⬜ ídem | ⬜ `jadx -d out_vuln ...` | ⬜ ídem | ⬜ `jadx` sobre APK segura ofuscada | ⬜ |
| VULN-09 | ⬜ ídem | ⬜ editar `rol` en prefs / ruta `admin` | ⬜ ídem | ⬜ `AccessPolicyTest` | ⬜ |

> Cada carpeta debe contener además capturas, logs, los comandos usados y la salida de las herramientas. Los reportes globales van en `docs/reportes/`.

---

## 5. Métricas para el informe IEEE (completar con datos reales)

| Métrica | Antes (`lab-vulnerable`) | Después (`main`) | Fuente | Estado |
| :--- | :--- | :--- | :--- | :--- |
| Nº de hallazgos por severidad (crítico/alto/medio/bajo) | Sin medir | Sin medir | Semgrep + Detekt + MobSF | ⬜ |
| Hallazgos Semgrep | Sin medir | Sin medir | `docs/reportes/semgrep.sarif` | ⬜ |
| Hallazgos Detekt | Sin medir | Sin medir | `docs/reportes/detekt.html` | ⬜ |
| Puntaje de seguridad MobSF | Sin medir | Sin medir | `docs/reportes/mobsf.pdf` | ⬜ |
| Cobertura de tests (`validation/` + `security/`) | Sin medir | Sin medir (objetivo ≥80 %) | `./gradlew allTests` | ⬜ |
| Estado del pipeline | Rojo esperado | Verde | GitHub Actions (capturas) | ⬜ |
| Tiempo de ejecución del pipeline | Sin medir | Sin medir | GitHub Actions | ⬜ |
| Riesgo residual por VULN (P×I tras control) | Ver §3 | Completar §3 | Matriz de seguridad | ⬜ |

---

## 6. Guion de demostración en vivo (≈10–12 min) — checklist

| # | Paso | Tiempo | Estado |
| :-: | :--- | :---: | :--- |
| 1 | Pipeline: rojo en `lab-vulnerable`, verde en `main` | 1 min | ⬜ |
| 2 | App VULN → SQLi login (`' OR '1'='1' --`) → entra como bibliotecario | 1 min | ⬜ |
| 3 | Buscador con `%' OR restringido=1 --` → libros restringidos | 1 min | ⬜ |
| 4 | Préstamo con días `-5`/`9999`; observación con `<img onerror>` → `alert` | 2 min | ⬜ |
| 5 | `adb logcat` (contraseña visible) + extracción de prefs/BD | 2 min | ⬜ |
| 6 | Lector accediendo al panel admin (si aplica) | 1 min | ⬜ |
| 7 | App SEGURA → repetir los mismos ataques → bloqueados | 3 min | ⬜ |
| 8 | `jadx` ofuscado vs. no ofuscado y puntaje MobSF | 1 min | ⬜ |

---

## 7. Registro de actualizaciones (append-only)

| Fecha | Fase | Qué se actualizó | Archivos tocados | Por quién |
| :--- | :--- | :--- | :--- | :--- |
| 24/11/2026 | — | Creación del documento vivo: mapa rúbrica→entregables, checklist E-01…E-10, matriz borrador VULN-01…09, inventario A1–A8, índice de evidencias, métricas y guion de demo. Todo en estado ⬜ salvo E-10. | `docs/SeguimientoEntregables.md` | Agente de IA |
| 24/11/2026 | F0 | Setup completado y verificado: proyecto KMP, wrapper Gradle 9.5.0, `libs.versions.toml`, SQLDelight configurado, flavors `vulnerable`/`segura`, `MainActivity`, `App` mínima, `.gitignore`, `local.properties` y README con credenciales de demo. `assembleVulnerableDebug assembleSeguraDebug` en verde; APKs con `applicationId` `ec.edu.bibliosec.vulnerable` / `.segura`, labels y `SECURE_MODE` correctos. E-09 ✅, E-02 🟨. | `settings.gradle.kts`, `build.gradle.kts`, `gradle/`, `gradlew`, `composeApp/`, `.gitignore`, `README.md`, `AGENTS.md`, `docs/*` | Agente de IA |
| 24/11/2026 | F1 | Dominio y BD completados y verificados: modelos, `Biblioteca.sq` (4 tablas + consultas parametrizadas + helpers de seed), `DriverFactory` y `PasswordHasher` expect/actual (Android + JVM), `BuildConfigProvider`, seed de 3 usuarios / 13 libros (3 restringidos) / 2 préstamos / 2 reseñas, interfaces de repositorio y `ServiceLocator`. `assembleVulnerableDebug assembleSeguraDebug` y `allTests` en verde (4 tests: seed, filtro de restringidos y hashing PBKDF2). Matriz sin cambios aún: las VULN se implementan en F3/F5. | `composeApp/src/commonMain/**`, `composeApp/src/androidMain/**`, `composeApp/src/jvmMain/**`, `composeApp/src/jvmTest/**`, `composeApp/build.gradle.kts`, `gradle/libs.versions.toml`, `AGENTS.md`, `README.md`, `docs/*` | Agente de IA |
| 24/11/2026 | F2 | UI base completada y verificada: 7 pantallas con `@Preview` (Login, Catálogo, Solicitud, Comprobante, Reseñas, Mis préstamos, Admin), `Ruta`/`AppState`/`UiDataSource`, WebView multiplataforma (`expect @Composable`), `HtmlSanitizer` aplicado al comprobante y barrera de rol en navegación. `assembleVulnerableDebug assembleSeguraDebug` y `allTests` en verde (8 tests: +4 de navegación por rol). La matriz sigue sin filas marcadas: F3/F5 implementarán las VULN. | `composeApp/src/commonMain/kotlin/ec/edu/bibliosec/ui/**`, `composeApp/src/commonMain/kotlin/ec/edu/bibliosec/security/HtmlSanitizer.kt`, `composeApp/src/androidMain/**/ui/**`, `composeApp/src/jvmMain/**/ui/**`, `composeApp/src/jvmTest/NavegacionTest.kt`, `composeApp/build.gradle.kts`, `AGENTS.md`, `README.md`, `docs/*` | Agente de IA |

> **Instrucción:** agrega una fila nueva al final cada vez que alimentes este documento. No sobrescribas las filas anteriores.

---

## 8. Checklist final de calidad (sección 13 de la especificación)

- [ ] Ninguna prueba se ejecutó contra sistemas de terceros.
- [ ] Cada `VULN-n` tiene código vulnerable, payload, evidencia, mitigación y re-test.
- [ ] La matriz mantiene trazabilidad Activo → Amenaza → Vulnerabilidad → Riesgo → Control → Contingencia.
- [ ] Ambas apps conviven en el teléfono (sufijos `.vulnerable` / `.segura`).
- [ ] `main` pasa el pipeline; `lab-vulnerable` conserva los reportes con hallazgos.
- [ ] Informe IEEE con diagrama, matriz y métricas.
- [ ] Credenciales de demo documentadas en el README y marcadas como **solo laboratorio**.
