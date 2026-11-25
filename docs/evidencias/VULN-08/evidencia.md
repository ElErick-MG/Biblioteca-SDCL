# VULN-08 — Ingeniería inversa sin ofuscación

**Estado:** 🟨 PENDIENTE de ejecutar `jadx`.

## Motivo

No hay daemon de Docker ni la herramienta `jadx` instalada en este entorno, por lo que aún no se puede decompilar el APK para capturar nombres de clases legibles. La configuración de R8 ya existe en `composeApp/build.gradle.kts` para release (`isMinifyEnabled=true`, `isShrinkResources=true`), pero la mitigación definitiva y su re-test se harán en F5/F8.

## Comando previsto (cuando haya herramienta)

```bash
jadx -d out_vuln composeApp/build/outputs/apk/vulnerable/release/composeApp-vulnerable-release.apk
jadx -d out_seg  composeApp/build/outputs/apk/segura/release/composeApp-segura-release.apk
```

## Evidencia disponible hoy

APK vulnerable debug generado en `composeApp/build/outputs/apk/vulnerable/debug/composeApp-vulnerable-debug.apk` (sin ofuscar, porque debug no aplica R8).

MobSF sí ejecutó **JADX** internamente durante el análisis (`Decompiling APK to Java with JADX` en el log) y su JSON incluye hallazgos de código decompilado (por ejemplo `ec/edu/bibliosec/data/vulnerable/AuthRepositoryVulnerable.java`). Sin embargo no se exportó un directorio `out/` con las fuentes, así que la fila sigue 🟨: para cerrarla hay que instalar `jadx` como herramienta independiente y decompilar ambos APK.
