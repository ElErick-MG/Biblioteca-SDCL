package ec.edu.bibliosec.data.vulnerable

/** Equivalente JVM del log inseguro (VULN-07) para las pruebas. */
actual fun logSensible(mensaje: String) {
    println("AUTH $mensaje")
}
