# VULN-04 — Input Validation deficiente

**Estado:** explotación verificada por test.

## Payload

```
días = -5 ; código = "" ; observación = 50 000 caracteres
```

## Código vulnerable

`ui/vulnerable/ValidacionVulnerable.kt:5`

```kotlin
// VULN-04: se acepta cualquier valor.
fun aceptar(dias: String, codigoEstudiante: String, observacion: String): Boolean = true
```

`PrestamoScreen` recibe `permitirObservacionLarga = !esSeguro`, de modo que en vulnerable no hay tope de 200 caracteres.

## Comando ejecutado

```bash
./gradlew jvmTest --tests "ec.edu.bibliosec.VulnerabilidadTest"
```

## Resultado real

`VULN-04 el formulario vulnerable acepta datos invalidos` → **PASADO**.

## Mitigación (F5)

`Validators` con días 1–15, `^E\d{7}$`, observación ≤200 sin caracteres de control, ISBN con checksum, máx. 3 préstamos activos y `CHECK` en BD. Re-test en F8.
