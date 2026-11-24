# Requisitos del Proyecto — BiblioSec

> **Propósito de este archivo:** responder *qué debe cumplir la aplicación*, con criterios verificables y trazabilidad desde las historias de usuario hasta las evidencias.
> **Estado:** requisitos derivados de `.docs/sistemaBiblioteca.md` (secciones 1, 3, 4, 5, 6, 7, 9, 10, 11); implementación pendiente (F0).
> **Reglas de trabajo:** ver [`../AGENTS.md`](../AGENTS.md) · **Estado del proyecto:** ver [`ContextoProyecto.md`](ContextoProyecto.md).

---

## 1. Convención de identificadores

| Prefijo | Categoría |
| :--- | :--- |
| `HU-xx` | Historia de usuario (dada en la especificación) |
| `RF-xx` | Requisito funcional |
| `RN-xx` | Requisito no funcional |
| `RS-xx` | Requisito de seguridad (vinculado a `VULN-xx`) |
| `RD-xx` | Requisito de datos |
| `RT-xx` | Requisito de pruebas |
| `RC-xx` | Requisito de CI/CD y auditoría |
| `RE-xx` | Requisito de documentación y evidencias |
| `F#` | Fase de implementación donde se satisface |
| `M#` | Módulo funcional |

Todo requisito se marca con **P0** (obligatorio para la entrega) o **P1** (valor agregado / bonus).

---

## 2. Requisitos funcionales

| ID | Requisito | Rol | Origen | Fase | Prioridad |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **RF-01** | Iniciar sesión con usuario y contraseña; la app resuelve el rol (`LECTOR` / `BIBLIOTECARIO`) y muestra mensaje de error **genérico** ("Credenciales inválidas"). La barra superior indica la variante (`VULNERABLE` / `SEGURA`). | Todos | HU-01, M1 | F2 | P0 |
| **RF-02** | Buscar libros por título, autor o ISBN, con lista de resultados (título, autor, disponibilidad) y botón "Solicitar préstamo". En la versión segura, los libros `restringido = 1` solo son visibles para bibliotecario. | Lector / Biblio | HU-02, M2 | F2/F5 | P0 |
| **RF-03** | Solicitar préstamo indicando libro, días (1–15), código de estudiante y observación (con contador de caracteres y errores por campo). El registro queda en estado `PENDIENTE` con fecha de solicitud. | Lector | HU-03, M3 | F2/F5 | P0 |
| **RF-04** | Ver comprobante digital del préstamo en `WebView`, con libro, usuario, días, estado y observación. La observación se renderiza como texto (nunca como HTML activo) y la página incluye CSP. | Lector | HU-04, M4 | F2/F5 | P0 |
| **RF-05** | Crear y leer reseñas de un libro (tarjetas en `WebView` o Compose según variante) con comentario y fecha. | Lector | HU-05, M4 | F2/F5 | P0 |
| **RF-06** | Listar préstamos del lector con estado `PENDIENTE` / `APROBADO` / `RECHAZADO` / `DEVUELTO`. | Lector | HU-04/M5 | F2 | P0 |
| **RF-07** | Aprobar o rechazar préstamos pendientes desde el panel del bibliotecario, actualizando estado y (si aplica) fecha de devolución. | Bibliotecario | HU-06, M5 | F2 | P0 |
| **RF-08** | Registrar nuevos libros con ISBN, título, autor, categoría, copias disponibles y casilla "restringido / uso interno". | Bibliotecario | HU-07, M5 | F2 | P0 |
| **RF-09** | Marcar la devolución de un libro (estado `DEVUELTO` + fecha de devolución). | Bibliotecario | HU-08, M5 | F2 | P0 |
| **RF-10** | Impedir que un lector acceda a pantallas ni acciones del bibliotecario: la ruta `admin` y las operaciones `aprobar`, `rechazar`, `registrarLibro` y `marcarDevolucion` se deniegan para `LECTOR`. | Sistema | HU-09, M1/M5 | F2/F5 | P0 |
| **RF-11** | Mantener la sesión entre reinicios (`userId`, `rol`, expiración) y permitir cerrar sesión; la expiración fuerza un nuevo login. | Todos | M1 | F3/F5 | P0 |
| **RF-12** | Instalar y ejecutar simultáneamente ambas variantes (`...vulnerable` y `...segura`) en el mismo dispositivo, identificables por nombre de app distinto. | Todos | Secc. 3 | F0 | P0 |

### Criterios de aceptación funcionales (resumen)

- Un lector autenticado **no puede** abrir el panel admin ni invocar sus acciones (RF-10).
- Un bibliotecario **sí ve** los libros restringidos en el buscador; un lector **no** (RF-02).
- El comprobante con un payload `<img src=x onerror=...>` se muestra como texto, sin `alert` (RF-04).
- Todas las pantallas incluyen `@Preview` (RF-01…RF-09).

---

## 3. Requisitos no funcionales

| ID | Requisito | Verificación | Fase | Prioridad |
| :--- | :--- | :--- | :--- | :--- |
| **RN-01** | Arquitectura **100 % local**: sin backend ni servicios externos. Única excepción: recurso `http://` local/mock para la demo de VULN-06. | Revisión de dependencias y manifiesto | F0/F3 | P0 |
| **RN-02** | Compilación sin Android Studio: JDK 17+, Android SDK Command Line Tools con `platforms;android-35` y `build-tools;35.0.0`, `ANDROID_HOME` configurado. | `java -version` + `./gradlew ...` | F0 | P0 |
| **RN-03** | UI y modelo de dominio en **español**; identificadores técnicos consistentes. | Revisión de código | F0-F9 | P0 |
| **RN-04** | Navegación completa y funcional por rol, con Compose Previews en cada pantalla y soporte opcional de Hot Reload en desktop. | Ejecución en dispositivo/@Preview | F2 | P0 |
| **RN-05** | Mantenibilidad: dependencias hacia adentro (`ui → casos de uso → repositorios → BD`); reglas de seguridad centralizadas en `validation/` y `security/`, sin duplicación. | Detekt + revisión | F1-F5 | P0 |
| **RN-06** | Observabilidad segura: `AppLogger` no registra datos sensibles y es *no-op* en release; no se usan `Log.*`/`println` con credenciales. | Semgrep + `logcat` | F3/F5 | P0 |
| **RN-07** | Portabilidad: `androidMain` obligatorio; `desktopMain` opcional (Compose Desktop + `JdbcSqliteDriver`) para inspección directa de la BD. | Compilación | F0 | P1 |
| **RN-08** | Ambas apps comparten la lógica de `commonMain`; solo las capas de plataforma y los paquetes `vulnerable`/`segura` difieren. | Revisión de módulos | F0-F5 | P0 |

---

## 4. Requisitos de seguridad

Cada `RS-xx` sigue el ciclo de 6 fases: identificar activos → amenazas → vulnerabilidades → valorar riesgos → controles → contingencia (sección 5 de la especificación). La tabla completa (STRIDE, CWE/OWASP Mobile, P×I, responsable, estado, evidencia) se entrega en `docs/matriz-seguridad.md`.

| ID | VULN | Activo | Amenaza / CWE | Control obligatorio | Re-test | Riesgo inicial | Fase | Prioridad |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **RS-01** | VULN-01 | A1/A2 BD y credenciales | Spoofing / CWE-89 | Consulta parametrizada `loginSeguro(username)`; contraseña **no** va en SQL, se verifica hash en código; error genérico; límite de 5 intentos → bloqueo 30 s | `' OR '1'='1' --` y `biblio1'--` fallan | 25 Alto | F3/F5 | P0 |
| **RS-02** | VULN-02 | A4 Libros restringidos | Information Disclosure / CWE-89 | `buscarLibrosSeguro(q)` parametrizado; filtro `restringido = 0` decidido por **rol** en repositorio; longitud ≤ 60 y caracteres permitidos | `%' OR restringido=1 --` y `UNION SELECT` sin fuga | 16 Alto | F3/F5 | P0 |
| **RS-03** | VULN-03 | A7 WebView | Tampering / CWE-79 | `HtmlSanitizer.escape()` en `commonMain`; `javaScriptEnabled=false`; `allowFileAccess=false`; `allowContentAccess=false`; meta CSP | `<img src=x onerror="alert('XSS')">` se ve como texto | 16 Alto | F3/F5 | P0 |
| **RS-04** | VULN-04 | A5 Préstamos | Tampering / DoS / CWE-20 | Validadores en `commonMain` (días 1–15, `^E\d{7}$`, observación ≤200 sin caracteres de control, ISBN con checksum, `copiasDisponibles > 0`, máx. 3 préstamos activos) + `CHECK` en BD | Datos vacíos/fuera de rango bloqueados en UI y lógica | 15 Alto | F3/F5 | P0 |
| **RS-05** | VULN-05 | A2/A3 Credenciales y sesión | Information Disclosure / CWE-256/312 | PBKDF2WithHmacSHA256 ≥120 000 iteraciones, salt 16 B, formato `iter:saltB64:hashB64`, comparación en tiempo constante; `EncryptedSharedPreferences` (Keystore) solo con `userId`, `rol`, expiración | Extracción vía ADB: hash ilegible y prefs cifradas | 20 Alto | F3/F5 | P0 |
| **RS-06** | VULN-06 | A1 Comunicaciones | MitM / CWE-319 | `usesCleartextTraffic="false"` + `network_security_config` con `cleartextTrafficPermitted="false"` | MobSF sin hallazgo; petición HTTP falla con `CLEARTEXT communication not permitted` | 6 Bajo | F3/F5 | P0 |
| **RS-07** | VULN-07 | A1/A3 Datos y logs | Information Disclosure / CWE-532/921 | `allowBackup="false"` + `dataExtractionRules` + `fullBackupContent` restrictivos; `AppLogger` sin sensibles y no-op en release; regla Semgrep/Detekt | `adb logcat` sin contraseñas; `adb backup` vacío/rechazado | 16 Alto | F3/F5 | P0 |
| **RS-08** | VULN-08 | A8 APK | Reverse engineering / CWE-693 | R8 (`isMinifyEnabled=true`, `isShrinkResources=true`) + `proguard-rules.pro` para SQLDelight/Compose/serialization; sin secretos en código | `jadx` sobre APK segura: clases ofuscadas, código muerto eliminado | 12 Medio | F5/F8 | P0 |
| **RS-09** | VULN-09 | A6 Panel admin | Elevation of Privilege / CWE-862/285 | `AccessPolicy.require(Rol.BIBLIOTECARIO)` en **navegación + casos de uso + repositorio**; rol leído de BD por `userId`, no de prefs; sesión cifrada con expiración | Lector recibe `AccesoDenegado` al abrir ruta `admin` o ejecutar acción | 20 Alto | F3/F5 | P1 |

### Contingencia obligatoria (columna de la matriz)

Cada riesgo debe documentar su contingencia: invalidar sesiones, restaurar BD desde copia, forzar cambio de contraseñas, purgar registros contaminados, rotar secretos/claves, revertir aprobaciones indebidas y parchear. Ver sección 5.2 de la especificación.

### Métricas de seguridad a reportar

Nº de hallazgos por severidad antes/después · diferencias Semgrep/Detekt entre `lab-vulnerable` y `main` · puntaje MobSF antes/después · cobertura de tests de validación/sanitización/hashing · estado y duración del pipeline · riesgo residual (P×I tras el control).

---

## 5. Requisitos de datos

| ID | Requisito | Fase | Prioridad |
| :--- | :--- | :--- | :--- |
| **RD-01** | Esquema SQLite/SQLDelight con 4 tablas: `Usuario`, `Libro`, `Prestamo`, `Resena`, con `PRIMARY KEY AUTOINCREMENT`, `UNIQUE` en `username` e `isbn`, `FOREIGN KEY` y `CHECK` en `rol` y `estado`. | F1 | P0 |
| **RD-02** | Consultas seguras parametrizadas en `Biblioteca.sq`: `loginSeguro`, `buscarLibrosSeguro`, `insertarPrestamo`, `prestamosPendientes`, `actualizarEstadoPrestamo`. | F1/F5 | P0 |
| **RD-03** | Seed obligatorio: usuarios `lector1/lector123` (LECTOR, `E2024001`), `lector2/lector456`, `biblio1/admin123` (BIBLIOTECARIO); ≥12 libros, de los cuales ≥3 con `restringido = 1` ("Manual interno de adquisiciones", "Archivo histórico reservado", "Tesis embargada"); ≥2 préstamos y ≥2 reseñas. | F1 | P0 |
| **RD-04** | Contraseñas sembradas en **texto plano** en la variante vulnerable y **hasheadas con salt** en la segura. | F1/F3/F5 | P0 |
| **RD-05** | Integridad referencial y validación en BD como última línea (`CHECK` de `estado`, `CHECK` de `rol`, FK a `Libro`/`Usuario`). | F1 | P0 |
| **RD-06** | La BD se crea y siembra automáticamente al primer arranque; el archivo es local (`biblioteca.db`). | F1 | P0 |

---

## 6. Requisitos de pruebas

| ID | Requisito | Casos mínimos | Fase | Prioridad |
| :--- | :--- | :--- | :--- | :--- |
| **RT-01** | `ValidatorsTest` | días `-5, 0, 1, 15, 16, 9999, "abc", ""`; código vacío/inválido/válido; observación 0/200/201 y caracteres de control; búsqueda con payload SQLi y longitud | F6 | P0 |
| **RT-02** | `HtmlSanitizerTest` | `<script>`, `<img onerror>`, comillas simples/dobles, `&` previo, cadena vacía, Unicode | F6 | P0 |
| **RT-03** | `PasswordHasherTest` | hash ≠ texto plano, salts distintos para igual clave, verify OK/KO, formato `iter:saltB64:hashB64` | F6 | P0 |
| **RT-04** | `AccessPolicyTest` | cada combinación `Rol × Accion` (`BUSCAR`, `SOLICITAR_PRESTAMO`, `VER_ADMIN`, `APROBAR_PRESTAMO`, `REGISTRAR_LIBRO`, `MARCAR_DEVOLUCION`, `VER_RESTRINGIDOS`) | F6 | P0 |
| **RT-05** | `SqlInjectionTest` (BD en memoria / JDBC desktop) | payloads `' OR '1'='1`, `' --`, `%' OR restringido=1 --` contra repositorios **seguros**: sin elevación ni fuga | F6 | P0 |
| **RT-06** | Ejecución y cobertura | `./gradlew allTests` en verde; cobertura ≥80 % en `validation/` y `security/` | F6 | P0 |
| **RT-07** | Re-test manual por VULN | Los mismos payloads de explotación ejecutados sobre la app segura, con evidencia | F8 | P0 |

---

## 7. Requisitos de CI/CD y auditoría

| ID | Requisito | Fase | Prioridad |
| :--- | :--- | :--- | :--- |
| **RC-01** | Workflow `.github/workflows/devsecops.yml` con `push` a `main` y `lab-vulnerable` y `pull_request`; jobs `sast`, `detekt-and-tests`, `build`. | F7 | P0 |
| **RC-02** | SAST: Semgrep con `p/kotlin`, `p/android` y `.semgrep/biblio-rules.yml`, salida SARIF como artefacto. En `lab-vulnerable`, `continue-on-error` conserva el reporte con hallazgos. | F7 | P0 |
| **RC-03** | Detekt (`config/detekt/detekt.yml`) y pruebas (`./gradlew testSeguraDebugUnitTest allTests`) con reportes como artefacto. | F7 | P0 |
| **RC-04** | Build de `assembleVulnerableDebug assembleSeguraDebug` y publicación de los APK como artefacto descargable. | F7 | P0 |
| **RC-05** | Estrategia de ramas: `lab-vulnerable` (reportes iniciales en rojo) y `main` (parches, pipeline en verde); exclusión documentada de `**/vulnerable/**` en el SAST de `main` o eliminación del paquete en esa rama. | F7 | P0 |
| **RC-06** | Convención de commits (`fix(VULN-01): ...`, `test(VULN-03): ...`) y **un PR por vulnerabilidad**; tags `v0.1-vulnerable` y `v1.0-segura`. | F7 | P0 |
| **RC-07** | DAST/binario: MobSF vía Docker sobre ambos APK, exportando informes a `docs/reportes/`; revisión de permisos, componentes exportados, `allowBackup`, cleartext, `debuggable`, secretos. | F4/F8 | P0 |
| **RC-08** | `jadx` comparativo (vulnerable sin ofuscar vs. segura ofuscada) con evidencia en `docs/evidencias/VULN-08/`. | F8 | P0 |

---

## 8. Requisitos de documentación y evidencias

| ID | Requisito | Fase | Prioridad |
| :--- | :--- | :--- | :--- |
| **RE-01** | `docs/matriz-seguridad.md` con columnas Activo · Amenaza (STRIDE) · Vulnerabilidad (CWE/OWASP Mobile) · P · I · Riesgo · Control · Contingencia · Responsable · Estado · Evidencia; clasificación Bajo 1-6 / Medio 7-14 / Alto 15-25. El borrador vivo se alimenta en `docs/SeguimientoEntregables.md` §3 y se exporta en F4. | F4 | P0 |
| **RE-02** | `docs/evidencias/VULN-0X/` por cada vulnerabilidad: capturas, logs, comandos y salida de herramientas (antes y después). | F4/F8 | P0 |
| **RE-03** | `docs/reportes/` con `semgrep.sarif`/`.json`, `detekt.html`, informes MobSF PDF/JSON, comparativo vulnerable vs. segura. | F4/F7/F8 | P0 |
| **RE-04** | Diagrama de arquitectura en Mermaid (sección 2.1 de la especificación) incluido en `docs/Arquitectura.md` y en el informe. | F1/F9 | P0 |
| **RE-05** | Informe IEEE con 11 secciones + referencias: resumen, introducción, marco teórico (OWASP Mobile Top 10, STRIDE, DevSecOps), arquitectura, metodología de 6 fases, matriz, pentesting, mitigación/re-test, CI/CD, métricas, conclusiones. | F9 | P0 |
| **RE-06** | README con credenciales de demo marcadas como **solo laboratorio** y comandos de instalación/verificación. | F0 | P0 |
| **RE-07** | Guion de demo en vivo de 10–12 min con 8 pasos (pipeline → ataques en VULN → mismos ataques bloqueados en SEGURA → jadx/MobSF). | F9 | P0 |
| **RE-08** | Checklist final: ninguna prueba contra terceros, cuatro artefactos por VULN, trazabilidad de la matriz, ambas apps conviviendo, `main` en verde, informe con diagrama/matriz/métricas. | F9 | P0 |

---

## 9. Matriz de trazabilidad (HU → requisito → VULN → fase → evidencia)

| HU | Módulo | Requisitos | VULN asociadas | Fase | Evidencia |
| :--- | :--- | :--- | :--- | :--- | :--- |
| HU-01 | M1 | RF-01, RF-11, RS-01, RS-05 | VULN-01, VULN-05 | F2/F3/F5 | `VULN-01/`, `VULN-05/` |
| HU-02 | M2 | RF-02, RS-02 | VULN-02 | F2/F3/F5 | `VULN-02/` |
| HU-03 | M3 | RF-03, RS-04 | VULN-04 | F2/F3/F5 | `VULN-04/` |
| HU-04 | M4 | RF-04, RF-06, RS-03 | VULN-03 | F2/F3/F5 | `VULN-03/` |
| HU-05 | M4 | RF-05, RS-03 | VULN-03 | F2/F3/F5 | `VULN-03/` |
| HU-06 | M5 | RF-07, RS-09 | VULN-09 | F2/F3/F5 | `VULN-09/` |
| HU-07 | M5 | RF-08, RS-09 | VULN-09 | F2/F3/F5 | `VULN-09/` |
| HU-08 | M5 | RF-09, RS-09 | VULN-09 | F2/F3/F5 | `VULN-09/` |
| HU-09 | M1/M5 | RF-10, RS-09 | VULN-09 | F2/F3/F5 | `VULN-09/` |
| — | BD / build | RF-12, RD-01…06, RS-06…08, RC-01…08 | VULN-06, VULN-07, VULN-08 | F0/F1/F3/F5/F7/F8 | `VULN-06/`, `VULN-07/`, `VULN-08/`, `docs/reportes/` |

---

## 10. Criterios globales de aceptación

La entrega se considera completa solo si:

1. Todos los requisitos **P0** están satisfechos y verificados con comando o evidencia real.
2. `./gradlew assembleVulnerableDebug assembleSeguraDebug` compila y ambas apps conviven en el dispositivo.
3. `./gradlew allTests` y `./gradlew detekt` pasan en `main`.
4. Cada VULN tiene los cuatro artefactos y su carpeta de evidencia (antes/después).
5. La matriz mantiene trazabilidad completa y el informe IEEE incluye métricas y diagrama.
6. El pipeline está rojo en `lab-vulnerable` y verde en `main`.
7. **Ninguna prueba se ejecutó contra sistemas de terceros.**
