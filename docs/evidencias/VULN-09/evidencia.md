# VULN-09 — Control de acceso roto en el panel admin (bonus)

**Estado:** ✅ Explotación verificada por test (sin mitigación aún; la corrección corresponde a F5).

## Código vulnerable

- `ui/vulnerable/AccesoVulnerable.kt:7` → `puedeVerAdmin(rol: Rol?) = true`
- `AppState.loginAdmin` en vulnerable no verifica el rol: con `' OR '1'='1' --` y cualquier contraseña navega a `Ruta.Admin`.
- `App.kt` usa `AccesoVulnerable` en el flavor vulnerable; en segura sí exige `Rol.BIBLIOTECARIO`.

## Explotación desde el menú de login

El menú tiene dos botones: `Inicio de sesion usuario` e `Inicio de sesion admin`.

- **Usuario** + `' OR '1'='1' --` / cualquier clave → entra al catálogo (`VULN-01`).
- **Admin** + `' OR '1'='1' --` / cualquier clave → entra al panel aunque la fila devuelta sea `lector1` (`VULN-01` + `VULN-09`).

El botón "Panel admin" **ya no aparece en el catálogo**; el acceso se demuestra desde el login, que es lo que pidió el docente/usuario.

## Comandos ejecutados

```bash
./gradlew jvmTest --tests "ec.edu.bibliosec.VulnerabilidadTest"
./gradlew jvmTest --tests "ec.edu.bibliosec.NavegacionTest"
```

## Resultado real

- `VULN-09 el acceso admin vulnerable ignora el rol` → **PASADO** (`puedeVerAdmin(null)` y con `Rol.LECTOR` devuelven `true`).
- `loginAdmin vulnerable entra al panel sin verificar rol` → **PASADO** (`lector1/lector123` → `Ruta.Admin`).
- `VULN-01 SQLi permite entrar como bibliotecario sin password` → **PASADO** (`biblio1'--`).

## Mitigación (F5, aún no aplicada)

`AccessPolicy.require(Rol.BIBLIOTECARIO)` en navegación, casos de uso y repositorio; rol leído de BD por `userId`. Re-test en F8.
