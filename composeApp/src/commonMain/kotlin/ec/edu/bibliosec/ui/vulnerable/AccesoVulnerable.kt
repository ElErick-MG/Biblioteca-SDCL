package ec.edu.bibliosec.ui.vulnerable

import ec.edu.bibliosec.domain.Rol

/** VULN-09: la navegación al panel admin no verifica el rol en el flavor vulnerable. */
object AccesoVulnerable {
    fun puedeVerAdmin(rol: Rol?): Boolean = true
}
