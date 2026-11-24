package ec.edu.bibliosec

import ec.edu.bibliosec.security.PasswordHasher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Verifica el hashing usado por el seed de la variante segura (VULN-05). */
class PasswordHasherTest {

    @Test
    fun `el hash es distinto del texto plano y usa salt`() {
        val hasher = PasswordHasher()
        val primero = hasher.hash("lector123")
        val segundo = hasher.hash("lector123")

        assertNotEquals("lector123", primero)
        assertNotEquals(primero, segundo)
        assertEquals(3, primero.split(":").size)
        assertEquals("120000", primero.split(":").first())
    }

    @Test
    fun `verify acepta la clave correcta y rechaza otra`() {
        val hasher = PasswordHasher()
        val almacenado = hasher.hash("admin123")

        assertTrue(hasher.verify("admin123", almacenado))
        assertFalse(hasher.verify("admin124", almacenado))
        assertFalse(hasher.verify("admin123", "formato-invalido"))
    }
}
