# VULN-09 — Control de acceso roto en el panel admin (bonus)

**Estado:** explotación verificada por test.

## Código vulnerable

`ui/vulnerable/AccesoVulnerable.kt:7`

```kotlin
// VULN-09: la navegación admin se muestra sin verificar el rol.
fun puedeVerAdmin(rol: Rol?): Boolean = true
```

Además, `PrestamoRepositoryVulnerable.actualizarEstado` no verifica el rol de quien aprueba/rechaza/devuelve.

## Comando ejecutado

```bash
./gradlew jvmTest --tests "ec.edu.bibliosec.VulnerabilidadTest"
```

## Resultado real

`VULN-09 el acceso admin vulnerable ignora el rol` → **PASADO** (`AccesoVulnerable.puedeVerAdmin(null)` y con `Rol.LECTOR` devuelven `true`).

## Mitigación (F5)

`AccessPolicy.require(Rol.BIBLIOTECARIO)` en navegación, casos de uso y repositorio; rol leído de BD por `userId`. Re-test en F8.
