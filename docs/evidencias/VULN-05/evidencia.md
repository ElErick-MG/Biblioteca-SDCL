# VULN-05 — Almacenamiento inseguro de credenciales y sesión

**Estado:** sesión en texto plano verificada por test; extracción con ADB pendiente de dispositivo propio.

## Código vulnerable

- `androidMain/data/vulnerable/SesionVulnerableAndroid.kt:18` → `putString("password", datos.password)` en `SharedPreferences` estándar (`MODE_PRIVATE`).
- `data/vulnerable/AuthRepositoryVulnerable.kt:18` → guarda la contraseña en la sesión.
- Seed vulnerable: contraseñas en texto plano en `Usuario.password`.

## Comandos ADB previstos (solo dispositivo propio)

```bash
adb shell run-as ec.edu.bibliosec.vulnerable ls databases shared_prefs
adb exec-out run-as ec.edu.bibliosec.vulnerable cat databases/biblioteca.db > biblioteca_vuln.db
adb shell run-as ec.edu.bibliosec.vulnerable cat shared_prefs/session.xml
```

## Resultado real (sin dispositivo)

`VULN-05 la sesión vulnerable guarda la contraseña en plano` → **PASADO** (`admin123` recuperado por la API de sesión vulnerable).

## SAST

Semgrep `kotlin-shared-prefs-password` en `SesionVulnerableAndroid.kt:18` → `docs/reportes/semgrep.json`.

## Mitigación (F5)

PBKDF2 ya implementado + `EncryptedSharedPreferences` con MasterKey en Keystore, guardando solo `userId`, `rol` y expiración. Re-test en F8.
