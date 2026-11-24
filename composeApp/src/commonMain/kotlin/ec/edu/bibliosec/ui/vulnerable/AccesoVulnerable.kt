package ec.edu.bibliosec.ui.vulnerable

import ec.edu.bibliosec.domain.Rol

/** La navegación admin se muestra sin verificar el rol (VULN-09). */
object AccesoVulnerable {
    fun puedeVerAdmin(rol: Rol?): Boolean = true
}
