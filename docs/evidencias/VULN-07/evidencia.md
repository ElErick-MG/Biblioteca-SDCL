# VULN-07 — Backup habilitado y logs sensibles

**Estado:** manifiesto y código de logging verificados; `adb logcat`/`adb backup` pendientes de dispositivo propio.

## Manifiesto vulnerable (empaquetado)

```xml
<application android:allowBackup="true" ... >
```

## Código de logging

`androidMain/data/vulnerable/LoggerVulnerableAndroid.kt`

```kotlin
// VULN-07: credenciales y datos de sesión enviados a logcat.
actual fun logSensible(mensaje: String) { Log.d("AUTH", mensaje) }
```

Invocado desde `AuthRepositoryVulnerable.login`: `logSensible("AUTH login user=$username pass=$password")`.

## Comandos ADB previstos (solo dispositivo propio)

```bash
adb logcat -s AUTH
adb backup -f backup.ab ec.edu.bibliosec.vulnerable
```

## SAST

Semgrep `kotlin-log-sensitive` en `AuthRepositoryVulnerable.kt:16` → `docs/reportes/semgrep.json`.

## Mitigación (F5)

`allowBackup="false"` + `dataExtractionRules` + `AppLogger` no-op en release. Re-test en F8.
