# VULN-01 — SQL Injection en login

**Estado:** explotación verificada en entorno local (BD en memoria). Evidencia gráfica en dispositivo: pendiente.

## Payload

```
Usuario:  ' OR '1'='1' --
Contraseña: cualquiera
```

## Código vulnerable

`composeApp/src/commonMain/kotlin/ec/edu/bibliosec/data/vulnerable/AuthRepositoryVulnerable.kt:12`

```kotlin
// VULN-01: SQLi por concatenación directa. Payload: ' OR '1'='1' --
val sql = "SELECT * FROM Usuario WHERE username = '$username' AND password = '$password'"
```

## Comando ejecutado

```bash
./gradlew jvmTest --tests "ec.edu.bibliosec.VulnerabilidadTest"
```

## Resultado real

`VulnerabilidadTest.VULN-01 SQLi en login otorga acceso sin credenciales` → **PASADO**.
Se obtiene un `Usuario` con rol `LECTOR` o `BIBLIOTECARIO` sin credenciales válidas.
Reporte: `composeApp/build/test-results/jvmTest/TEST-ec.edu.bibliosec.VulnerabilidadTest.xml`.

## SAST

Semgrep `kotlin-sql-concatenation` en `AuthRepositoryVulnerable.kt:12` → ver `docs/reportes/semgrep.json`.

## Mitigación (F5)

`loginSeguro(username)` parametrizado + `PasswordHasher.verify` en código + error genérico + límite de intentos. Re-test en F8.
