# Contexto del Proyecto — BiblioSec

> **Propósito de este archivo:** responder *qué proyecto es este y dónde estamos parados* para que un agente de IA pueda retomar el trabajo sin reconstruir el contexto desde cero.
> **Estado verificado:** 24/11/2026.
> **Reglas de trabajo:** ver [`../AGENTS.md`](../AGENTS.md).

---

## 1. Ficha de identidad

| Campo | Valor |
| :--- | :--- |
| Asignatura | Desarrollo de Software Seguro |
| Nombre del proyecto | `BiblioSec` |
| Naturaleza | Sistema de biblioteca móvil **con laboratorio de vulnerabilidades**: versión *vulnerable* vs. versión *segura* de la misma app |
| Objetivo general | Diseñar, desarrollar, auditar y asegurar una aplicación de biblioteca integrando seguridad en todo el SDLC (DevSecOps), análisis de riesgos, pentesting controlado y CI/CD |
| Dominio | Biblioteca universitaria: **lectores** buscan libros y solicitan préstamos; el **bibliotecario** aprueba/rechaza, registra libros y marca devoluciones |
| Stack | Kotlin Multiplatform + Compose Multiplatform + SQLDelight + SQLite; **100 % local, sin backend externo, sin Android Studio** |
| IDE / entorno | Google Antigravity o VS Code + JDK 17+ + Android SDK Command Line Tools |
| Repositorio | GitHub con ramas `lab-vulnerable` (hallazgos) y `main` (parches, pipeline en verde) |

### Regla ética

Todo pentesting se realiza **estrictamente en entorno local controlado** (emulador o dispositivo físico propio). **Está prohibido probar sobre sistemas de terceros.** Las credenciales de demo son **solo de laboratorio** y deben quedar documentadas en el README.

### Cronograma de referencia

- **Primer avance** (aula virtual + mini presentación): 13/11/2026 — **ya transcurrido**. Si no se entregó, priorizar F1–F4 como base de la entrega final.
- **Entrega final y defensa técnica:** jueves 21 y viernes 22 de enero de 2027.

---

## 2. Estado actual del repositorio

Contenido real verificado en la raíz:

```
Biblioteca-SDCL/
├── .docs/                 # documentación personal (IGNORADA por Git)
├── docs/                  # entregables versionados (onboarding + seguimiento)
├── composeApp/            # módulo KMP: commonMain + androidMain, flavors, SQLDelight
├── gradle/                # wrapper + libs.versions.toml
├── gradlew / gradlew.bat
├── .gitignore / gradle.properties / local.properties (ignorado)
├── settings.gradle.kts / build.gradle.kts
├── AGENTS.md / README.md
└── build/                 # reports y salidas de Gradle (ignorado)
```

**F0 verificada:** `./gradlew assembleVulnerableDebug assembleSeguraDebug` termina en `BUILD SUCCESSFUL`. Se generan:

- `composeApp/build/outputs/apk/vulnerable/debug/composeApp-vulnerable-debug.apk` → `ec.edu.bibliosec.vulnerable`, label `BiblioSec (VULN)`, `SECURE_MODE=false`.
- `composeApp/build/outputs/apk/segura/debug/composeApp-segura-debug.apk` → `ec.edu.bibliosec.segura`, label `BiblioSec (SEGURA)`, `SECURE_MODE=true`.

**F1 verificada:** `./gradlew allTests` termina en `BUILD SUCCESSFUL` (4 tests). El seed crea 3 usuarios, 13 libros (3 restringidos), 2 préstamos y 2 reseñas; `PasswordHasher` produce hashes PBKDF2 con salt distinto y `verify` funciona.

**F2 verificada:** 7 pantallas funcionales con `@Preview` (Login, Catálogo, Solicitud, Comprobante con WebView, Reseñas, Mis préstamos y Panel admin), navegación por rol y 4 tests de navegación. `./gradlew allTests` pasa con **8 tests** en total.

**No existe todavía:** repositorios concretos (`data.vulnerable` / `data.segura`), sesión persistente, validadores, `AccessPolicy`, logs inseguros, manifiestos por flavor, workflow de GitHub Actions, reportes ni evidencias.

**Conclusión:** el proyecto está en **F2 completada**; el siguiente paso de implementación es **F3 (implementación vulnerable)**.

> Distinción importante: `.docs/` (ignorado, especificación de entrada) ≠ `docs/` (versionado, entregables del proyecto).

---

## 3. Fuentes de verdad y mapa de documentación

| Documento | Rol |
| :--- | :--- |
| `.docs/sistemaBiblioteca.md` | **Fuente maestra.** Secciones 0–13: instrucciones al agente, alcance, arquitectura, entorno, modelo de datos, metodología, catálogo VULN, pantallas, esqueletos de código, pruebas, CI/CD, rúbrica, fases y checklist. |
| `.docs/indicaciones.md` | Guía académica original (sin el dominio de biblioteca aún aplicado). |
| `AGENTS.md` | Cómo debe trabajar el agente dentro del repo. |
| `docs/ContextoProyecto.md` | Este archivo: dónde estamos. |
| `docs/Requisitos.md` | Qué debe cumplir la app (HU, RF, RN, RS, RD, RT, RC, RE). |
| `docs/Arquitectura.md` | Cómo está construida: capas, flavors, BD, seguridad, decisiones. |
| `docs/SeguimientoEntregables.md` | **Documento vivo**: matriz borrador, checklist de entregables, evidencias, métricas y registro de cambios; se alimenta fase por fase según la rúbrica. |
| `docs/matriz-seguridad.md` | Matriz de riesgos formal (pendiente, F4; se exporta desde `SeguimientoEntregables.md` §3). |
| `docs/evidencias/VULN-0X/` | Evidencias por vulnerabilidad (pendiente, F4/F8). |
| `docs/reportes/` | Semgrep / Detekt / MobSF (pendiente, F4/F7/F8). |

---

## 4. Roadmap y estado de fases

| Fase | Nombre | Estado |
| :--- | :--- | :--- |
| F0 | Setup (KMP, SQLDelight, Compose, flavors) | ✅ COMPLETADA y verificada (24/11/2026) |
| F1 | Dominio y base de datos + seed | ✅ COMPLETADA y verificada (24/11/2026) |
| F2 | UI base y navegación por rol | ✅ COMPLETADA y verificada (24/11/2026) |
| F3 | Implementación vulnerable (`// VULN-n`) | ⬜ PENDIENTE — **siguiente paso de implementación** |
| F4 | Matriz de seguridad y pentesting inicial | ⬜ PENDIENTE |
| F5 | Implementación segura y controles | ⬜ PENDIENTE |
| F6 | Pruebas automatizadas | ⬜ PENDIENTE |
| F7 | CI/CD (GitHub Actions) | ⬜ PENDIENTE |
| F8 | Re-test, reportes y métricas | ⬜ PENDIENTE |
| F9 | Informe IEEE, presentación y defensa | ⬜ PENDIENTE |

---

## 5. Dominio funcional

### Módulos (M1–M5)

| Módulo | Descripción | Vulnerabilidades que provoca |
| :--- | :--- | :--- |
| **M1. Login y roles** | Autenticación con roles Lector / Bibliotecario | SQLi en login, almacenamiento inseguro, logs con credenciales, control de acceso roto |
| **M2. Catálogo y buscador** | Búsqueda por título, autor o ISBN | SQLi → fuga de libros restringidos |
| **M3. Solicitud de préstamo** | Formulario con días, código de estudiante y observación | Input validation deficiente |
| **M4. Comprobante y reseñas** | Ticket digital en `WebView` y reseñas de libros | XSS |
| **M5. Panel del bibliotecario** | Aprobación, alta de libros y devoluciones | RBAC roto, exposición de datos |
| **BD local** | SQLite + SQLDelight | Almacenamiento inseguro, backup, ingeniería inversa |

### Historias de usuario mínimas

| ID | Rol | Historia | Módulo |
| :--- | :--- | :--- | :--- |
| HU-01 | Todos | Iniciar sesión con usuario y contraseña según el rol | M1 |
| HU-02 | Lector | Buscar libros por título, autor o ISBN | M2 |
| HU-03 | Lector | Solicitar préstamo indicando días (1–15), código de estudiante y observación | M3 |
| HU-04 | Lector | Ver comprobante digital con título y observación | M4 |
| HU-05 | Lector | Dejar y leer reseñas de un libro | M4 |
| HU-06 | Bibliotecario | Aprobar o rechazar préstamos pendientes | M5 |
| HU-07 | Bibliotecario | Registrar libros (incluyendo *restringidos / uso interno*) | M5 |
| HU-08 | Bibliotecario | Marcar la devolución de un libro | M5 |
| HU-09 | Sistema | Un lector **no** debe acceder a pantallas ni acciones del bibliotecario | M1/M5 |

---

## 6. Catálogo de vulnerabilidades (VULN-01 … VULN-09)

| ID | Vulnerabilidad | Riesgo (P×I) | Artefactos pendientes |
| :--- | :--- | :--- | :--- |
| VULN-01 | SQLi en login (CWE-89) | 25 Alto | Código, payload, mitigación, re-test |
| VULN-02 | SQLi en buscador → libros restringidos (CWE-89) | 16 Alto | Ídem |
| VULN-03 | XSS en comprobante y reseñas (CWE-79) | 16 Alto | Ídem |
| VULN-04 | Input validation deficiente en préstamo (CWE-20) | 15 Alto | Ídem |
| VULN-05 | Almacenamiento inseguro de credenciales y sesión (CWE-256/312) | 20 Alto | Ídem |
| VULN-06 | Tráfico en claro / cleartext (CWE-319) | 6 Bajo | Ídem |
| VULN-07 | Backup habilitado + logs sensibles (CWE-532/921) | 16 Alto | Ídem |
| VULN-08 | Ingeniería inversa sin ofuscación (CWE-693) | 12 Medio | Ídem |
| VULN-09 *(bonus)* | Control de acceso roto en panel admin (CWE-862/285) | 20 Alto | Ídem |

Las 7 vulnerabilidades exigidas por la guía se cubren con VULN-01 a VULN-08 (SQLi dividida en login y búsqueda); VULN-09 es valor agregado.

---

## 7. Entregables y rúbrica (10,0 puntos)

**Entregables:** repo con historial de correcciones · diagrama de arquitectura · matriz de seguridad · evidencias de explotación/mitigación/re-test · reportes SAST/DAST (antes y después) · pipeline CI/CD con evidencia · demo en vivo con ambas apps + `scrcpy` · informe técnico IEEE con métricas.

| Criterio | Puntos |
| :--- | :---: |
| Análisis de activos, amenazas, vulnerabilidades, riesgos y matriz | 2,0 |
| Pentesting controlado (SQLi, XSS, Input Validation y controles móviles) | 2,0 |
| Diseño/arquitectura y aplicación de controles | 1,5 |
| Corrección, mitigación, contingencia y re-test | 1,5 |
| Reporte SAST/DAST (Semgrep, Detekt, MobSF) | 1,0 |
| Pipeline CI/CD | 1,0 |
| Informe IEEE, presentación y defensa | 1,0 |

---

## 8. Glosario rápido

| Término | Significado en este proyecto |
| :--- | :--- |
| **Flavor** | Variante de build (`vulnerable` / `segura`) con distinto `applicationIdSuffix`: permite instalar ambas apps a la vez |
| **Product flavors vs. build types** | Se usan *product flavors* en la dimensión `modo` en lugar de build types para poder diferenciar también el `AndroidManifest` |
| **ServiceLocator** | Punto de composición en `commonMain/di` que elige repositorios según `SECURE_MODE` |
| **Expect/actual** | Mecanismo KMP para declarar en `commonMain` (`PasswordHasher`, `SessionStore`, `DriverFactory`) e implementar en `androidMain` |
| **VULN-n** | Identificador de vulnerabilidad; también etiqueta del comentario en el código y del folder de evidencias |
| **Re-test** | Repetir el mismo payload sobre la versión segura y verificar que falla |
| **Matriz** | Tabla con trazabilidad Activo → Amenaza (STRIDE) → Vulnerabilidad (CWE/OWASP) → P×I → Control → Contingencia → Evidencia |
| **lab-vulnerable / main** | Rama con hallazgos (pipeline rojo esperado) / rama con parches (pipeline verde) |

---

## 9. Próximo paso concreto para un agente

F2 está cerrada. Si el usuario pide avanzar, empezar por **F3. Implementación vulnerable**:

1. Crear `data/vulnerable/` con repositorios que usen **concatenación SQL** en login y búsqueda; marcar cada punto con `// VULN-01` y `// VULN-02`.
2. Añadir el buscador vulnerable (`%' OR restringido=1 --`) y el formulario sin validación (`// VULN-04`).
3. Renderizar comprobante y reseñas sin sanitizar, con `javaScriptEnabled = true` en la variante vulnerable (`// VULN-03`) mediante un actual específico del flavor.
4. Guardar sesión en `SharedPreferences` estándar y credenciales en texto plano (`// VULN-05`), logs con `Log.d`/`println` sensibles (`// VULN-07`).
5. Manifiesto overlay vulnerable: `allowBackup="true"` y `usesCleartextTraffic="true"` (`// VULN-06`), y ruta `admin` sin verificación de rol (`// VULN-09`).
6. Verificar que los payloads de la sección 6 funcionan en la app vulnerable y actualizar F3 aquí y en `docs/SeguimientoEntregables.md`.
