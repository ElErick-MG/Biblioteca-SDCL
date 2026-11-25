# MobSF — ejecución real (F4)

**Estado:** ✅ Ejecutado el 25/11/2026 sobre ambos APK.

## Levantamiento del contenedor

```powershell
docker run -d --name bibliosec-mobsf -p 8000:8000 `
  -e MOBSF_API_KEY=bibliosec_mobsf_f4_2026 `
  opensecurity/mobile-security-framework-mobsf:latest
# http://localhost:8000  (credenciales por defecto: mobsf / mobsf)
```

MobSF v4.6.9, healthcheck `healthy` tras ~25 s.

## Comandos REST utilizados

```bash
# vulnerable
curl -X POST http://localhost:8000/api/v1/upload -H "Authorization: bibliosec_mobsf_f4_2026" \
  -F "file=@composeApp/build/outputs/apk/vulnerable/debug/composeApp-vulnerable-debug.apk"
# → hash 1fa1ae1c2de829fbf4cd308255dcaab1

curl -X POST http://localhost:8000/api/v1/scan  -H "Authorization: bibliosec_mobsf_f4_2026" -d "hash=<hash>"
curl -X POST http://localhost:8000/api/v1/report_json -H "Authorization: bibliosec_mobsf_f4_2026" -d "hash=<hash>" -o mobsf-vulnerable.json
curl -X POST http://localhost:8000/api/v1/download_pdf  -H "Authorization: bibliosec_mobsf_f4_2026" -d "hash=<hash>" -o mobsf-vulnerable.pdf

# segura (mismo procedimiento)
# → hash 5e91d3b89f47e4769c91f8a0e7576236
```

## Resultados

| APK | Puntaje MobSF | High | Warning | Info | Reportes |
| :--- | :---: | :---: | :---: | :---: | :--- |
| `composeApp-vulnerable-debug.apk` | **50** | 5 | 4 | 2 | [`mobsf-vulnerable.json`](mobsf-vulnerable.json), [`mobsf-vulnerable.pdf`](mobsf-vulnerable.pdf) |
| `composeApp-segura-debug.apk` | **60** | 3 | 3 | 2 | [`mobsf-segura.json`](mobsf-segura.json), [`mobsf-segura.pdf`](mobsf-segura.pdf) |

### Hallazgos destacados del APK vulnerable

**High**
- Application signed with debug certificate
- Base config insegura: cleartext permitido para todos los dominios
- `android:usesCleartextTraffic=true`
- `android:debuggable=true`
- Debug configuration enabled en `BuildConfig.java`

**Warning**
- `minSdk=26` (Android 8.0)
- **Application Data can be Backed up (`allowBackup=true`)** → evidencia de VULN-07
- `ProfileInstallReceiver` exportado con permiso `DUMP`
- Hardcoded sensitive information en `AuthRepositoryVulnerable.java`, `DatosSesionVulnerable.java`, `db/Usuario.java` → evidencia de VULN-05

**Info**
- "The App logs information. Sensitive information should never be logged." → `LoggerVulnerableAndroidKt.java` → evidencia de VULN-07
- Uso de SQL Cipher en `AndroidSqliteDriver.java`

El APK **segura** ya no reporta cleartext ni backup (sus high bajan de 5 a 3), pero **aún contiene el código vulnerable** porque F5 no se ha implementado; por eso la comparativa definitiva se repetirá en F8.

> MobSF ejecutó internamente **JADX** ("Decompiling APK to Java with JADX" en el log) y el JSON incluye el análisis de código decompiledo, aunque no se exportó un directorio `out/` con fuentes. VULN-08 sigue marcado 🟨 hasta exportar ese resultado de forma explícita.

## Detener el servicio

```bash
docker stop bibliosec-mobsf
```
