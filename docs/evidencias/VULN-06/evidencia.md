# VULN-06 — Tráfico en texto claro

**Estado:** configuración insegura verificada en el manifiesto empaquetado del APK. Captura de tráfico con proxy local: pendiente.

## Comando ejecutado

```powershell
Get-Content "composeApp\build\intermediates\packaged_manifests\vulnerableDebug\processVulnerableDebugManifestForPackage\AndroidManifest.xml" |
  Select-String -Pattern "allowBackup|usesCleartextTraffic|networkSecurityConfig|INTERNET|<application"
```

## Resultado real (vulnerable)

```xml
<uses-permission android:name="android.permission.INTERNET" />
<application
    android:allowBackup="true"
    android:networkSecurityConfig="@xml/network_security_config"
    android:usesCleartextTraffic="true" >
```

`src/vulnerable/res/xml/network_security_config.xml`:

```xml
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```

## Resultado real (segura)

```xml
<application android:allowBackup="false" >  <!-- sin usesCleartextTraffic -->
```

## MobSF (25/11/2026)

El informe [`../../reportes/mobsf-vulnerable.pdf`](../../reportes/mobsf-vulnerable.pdf) reporta dos hallazgos **High**:

- `Base config is insecurely configured to permit clear text traffic to all domains`
- `Clear text traffic is Enabled For App[android:usesCleartextTraffic=true]`

El APK segura ya no los reporta (puntaje 50 → 60). Detalle en [`../../reportes/mobsf-estado.md`](../../reportes/mobsf-estado.md).

## Mitigación (F5)

`usesCleartextTraffic="false"` + `network_security_config` con `cleartextTrafficPermitted="false"`. Re-test con MobSF en F8.
