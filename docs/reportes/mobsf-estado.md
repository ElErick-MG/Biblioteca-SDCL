# Estado de MobSF (F4)

**Resultado:** no ejecutado en este entorno.

## Motivo verificado

```bash
docker info --format '{{.ServerVersion}}'
# failed to connect to the docker API at npipe:////./pipe/dockerDesktopLinuxEngine
# El sistema no puede encontrar el archivo especificado.
```

El cliente Docker está instalado, pero el daemon de Docker Desktop Linux no está disponible, por lo que no se pudo levantar:

```bash
docker run -it --rm -p 8000:8000 opensecurity/mobile-security-framework-mobsf:latest
```

## Qué se hizo en su lugar

- Se verificó el manifiesto empaquetado de ambos APK con `aapt dump badging` y leyendo `AndroidManifest.xml` fusionado (evidencias VULN-06 y VULN-07).
- Se ejecutaron Semgrep y Detekt con reportes reales en esta carpeta.

## Pendiente (F8)

Con Docker disponible: subir ambos APK a `http://localhost:8000`, exportar `mobsf-vulnerable.pdf` y `mobsf-segura.pdf`, y comparar puntajes. **No se inventa ningún puntaje.**
