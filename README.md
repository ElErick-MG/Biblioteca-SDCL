# BiblioSec

Sistema de biblioteca móvil con laboratorio de vulnerabilidades (versión **vulnerable** vs. versión **segura**), desarrollado con **Kotlin Multiplatform + Compose Multiplatform + SQLDelight**, 100 % local y sin Android Studio.

> Asignatura: **Desarrollo de Software Seguro**.
> Especificación maestra: `.docs/sistemaBiblioteca.md` (no versionada).
> Onboarding para agentes: [`AGENTS.md`](AGENTS.md) y [`docs/`](docs/).

---

## Estado del proyecto

| Fase | Estado |
| :--- | :--- |
| F0. Setup | ✅ Completada y verificada (dos APK con `applicationId` distinto) |
| F1. Dominio y BD | ✅ Completada y verificada (seed + tests `allTests` en verde) |
| F2. UI base | ✅ Completada (7 pantallas, `@Preview` y navegación por rol) |
| F3. Implementación vulnerable | ✅ Completada (payloads verificados; evidencia gráfica en F4) |
| F4. Matriz y pentesting | ⬜ Pendiente |
| F5. Implementación segura | ⬜ Pendiente |
| F6. Pruebas | ⬜ Pendiente |
| F7. CI/CD | ⬜ Pendiente |
| F8. Re-test y reportes | ⬜ Pendiente |
| F9. Informe y defensa | ⬜ Pendiente |

El avance detallado (matriz, entregables, evidencias y métricas) se lleva en [`docs/SeguimientoEntregables.md`](docs/SeguimientoEntregables.md).

---

## Requisitos del entorno

- **JDK 17 o superior** (verificado con JDK 21).
- **Android SDK Command Line Tools**: `platforms;android-35` y `build-tools;35.0.0`, con `ANDROID_HOME` configurado o un archivo `local.properties` con `sdk.dir`.
- **IDE:** Google Antigravity o VS Code con soporte Kotlin (NO se requiere Android Studio).
- Herramientas de auditoría (para fases posteriores): `adb`, `scrcpy`, `jadx`, Docker (MobSF) y `semgrep`.

Instalación rápida del SDK si hace falta:

```bash
sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"
```

---

## Compilación (F0)

```bash
./gradlew assembleVulnerableDebug assembleSeguraDebug
```

Se generan dos APK con distinto `applicationId` que pueden convivir en el mismo dispositivo:

- `composeApp/build/outputs/apk/vulnerable/debug/composeApp-vulnerable-debug.apk` → `ec.edu.bibliosec.vulnerable`
- `composeApp/build/outputs/apk/segura/debug/composeApp-segura-debug.apk` → `ec.edu.bibliosec.segura`

Instalación y proyección en dispositivo propio:

```bash
adb install -r composeApp/build/outputs/apk/vulnerable/debug/composeApp-vulnerable-debug.apk
adb install -r composeApp/build/outputs/apk/segura/debug/composeApp-segura-debug.apk
scrcpy
```

Pruebas y análisis estático (fases F6/F7):

```bash
./gradlew allTests
./gradlew detekt
```

---

## Credenciales de demostración — SOLO LABORATORIO

Estas credenciales son ficticias y se usan únicamente en el emulador o en un dispositivo propio. **Está prohibido usarlas o probarlas contra sistemas de terceros.**

| Usuario | Contraseña | Rol | Código de estudiante |
| :--- | :--- | :--- | :--- |
| `lector1` | `lector123` | LECTOR | `E2024001` |
| `lector2` | `lector456` | LECTOR | `E2024002` |
| `biblio1` | `admin123` | BIBLIOTECARIO | — |

---

## Versiones fijadas

Definidas en `gradle/libs.versions.toml`:

| Componente | Versión |
| :--- | :--- |
| Gradle (wrapper) | 9.5.0 |
| Kotlin | 2.4.10 |
| Android Gradle Plugin | 9.0.1 |
| Compose Multiplatform | 1.11.1 |
| SQLDelight | 2.4.1 |
| androidx.activity | 1.13.0 |

> AGP 9.0 mantiene temporalmente el modelo clásico (`com.android.application` + KMP en el mismo módulo) mediante las banderas `android.builtInKotlin=false` y `android.newDsl=false` en `gradle.properties`. Esto permite los flavors y source sets por variante que exige el laboratorio. Revisar antes de migrar a AGP 10.0.
