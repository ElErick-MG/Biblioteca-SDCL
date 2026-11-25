# VULN-02 — SQL Injection en buscador de catálogo

**Estado:** explotación verificada en entorno local (BD en memoria).

## Payload

```
%' OR restringido=1 --
```

## Código vulnerable

`composeApp/src/commonMain/kotlin/ec/edu/bibliosec/data/vulnerable/LibroRepositoryVulnerable.kt:15-23`

```kotlin
// VULN-02 + VULN-09: sin filtro por rol ni por `restringido`
val sql = """
    SELECT * FROM Libro
    WHERE (titulo LIKE '%$texto%'
      OR autor LIKE '%$texto%'
      OR isbn LIKE '%$texto%'
    )
""".trimIndent()
```

## Comando ejecutado

```bash
./gradlew jvmTest --tests "ec.edu.bibliosec.VulnerabilidadTest"
```

## Resultado real

`VULN-02 SQLi en buscador filtra libros restringidos` → **PASADO**. Aparecen los 3 libros con `restringido = 1` ("Manual interno de adquisiciones", "Archivo histórico reservado", "Tesis embargada") para un usuario LECTOR.

## SAST

Semgrep `kotlin-sql-concatenation` en `LibroRepositoryVulnerable.kt:19,20,21` → `docs/reportes/semgrep.json`.

## Mitigación (F5)

`buscarLibrosSeguro(q)` parametrizado + filtro `restringido = 0` decidido por rol en el repositorio + longitud ≤ 60. Re-test en F8.
