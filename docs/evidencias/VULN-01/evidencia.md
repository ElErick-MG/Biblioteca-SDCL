# VULN-01 — SQL Injection en login

**Estado:** explotación verificada en entorno local (BD en memoria). Evidencia gráfica en dispositivo: pendiente.

## Payload

```
Usuario:  ' OR '1'='1' --
Contraseña: cualquiera
```

## Explotación desde el menú de login

La pantalla de inicio de sesión tiene **dos botones**: `Inicio de sesion usuario` e `Inicio de sesion admin`.

- Con `biblio1'--` como usuario y **cualquier contraseña**, el SQL queda
  `SELECT * FROM Usuario WHERE username = 'biblio1'--' AND password = '...'` → entra como BIBLIOTECARIO.
- Con `' OR '1'='1' --` y cualquier contraseña, entra con el primer usuario de la BD.
- Pulsando **Inicio de sesion admin** con el mismo payload se abre el panel aunque la fila devuelta sea `lector1` (VULN-09).
- Con `biblio1'--` y cualquier contraseña entra directamente como BIBLIOTECARIO.

La mitigación de rol **no se ha aplicado**: está prevista para F5 junto con `AccessPolicy`.

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
