package ec.edu.bibliosec.data.vulnerable

import ec.edu.bibliosec.data.repo.AuthRepository
import ec.edu.bibliosec.data.toDomain
import ec.edu.bibliosec.domain.Usuario

/** Login con SQL concatenado, logs sensibles y sesión en texto plano. */
class AuthRepositoryVulnerable(private val sesion: SesionVulnerable) : AuthRepository {

    override fun login(username: String, password: String): Usuario? {
        // VULN-01: SQLi por concatenación directa. Payload: ' OR '1'='1' --
        val sql = "SELECT * FROM Usuario WHERE username = '$username' AND password = '$password'"
        val fila = SqlVulnerable.usuarios(sql).firstOrNull() ?: return null

        // VULN-07: credenciales escritas en logs.
        logSensible("AUTH login user=$username pass=$password")

        // VULN-05: contraseña persistida en la sesión sin cifrar.
        sesion.guardar(
            DatosSesionVulnerable(fila.id, fila.rol, fila.username, fila.password),
        )
        return fila.toDomain()
    }

    override fun usuarioPorId(id: Long): Usuario? {
        // VULN-01: también concatenado.
        val sql = "SELECT * FROM Usuario WHERE id = $id"
        return SqlVulnerable.usuarios(sql).firstOrNull()?.toDomain()
    }

    override fun cerrarSesion() {
        sesion.limpiar()
        logSensible("AUTH logout")
    }
}
