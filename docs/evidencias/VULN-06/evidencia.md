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

## Mitigación (F5)

`usesCleartextTraffic="false"` + `network_security_config` con `cleartextTrafficPermitted="false"`. Re-test con MobSF en F8.
