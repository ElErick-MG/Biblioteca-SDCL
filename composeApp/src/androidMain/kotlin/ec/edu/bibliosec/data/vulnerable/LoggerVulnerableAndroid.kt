package ec.edu.bibliosec.data.vulnerable

import android.util.Log

/** VULN-07: credenciales y datos de sesión enviados a logcat. */
actual fun logSensible(mensaje: String) {
    Log.d("AUTH", mensaje)
}
