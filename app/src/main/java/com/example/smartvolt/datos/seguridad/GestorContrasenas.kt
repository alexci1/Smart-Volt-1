package com.example.smartvolt.datos.seguridad

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object GestorContrasenas {

    private const val ITERACIONES = 120000
    private const val LONGITUD_HASH = 256
    private const val LONGITUD_SAL = 16

    private const val SHA256 = "PBKDF2WithHmacSHA256"
    private const val SHA1 = "PBKDF2WithHmacSHA1"

    fun generarHash(contrasena: String): String {

        val sal = ByteArray(LONGITUD_SAL)
        SecureRandom().nextBytes(sal)

        // Seleccionamos el algoritmo disponible en el dispositivo.
        val algoritmo = obtenerAlgoritmoDisponible()

        val hash = calcularHash(
            contrasena = contrasena,
            sal = sal,
            algoritmo = algoritmo
        )

        val salCodificada = Base64.encodeToString(
            sal,
            Base64.NO_WRAP
        )

        val hashCodificado = Base64.encodeToString(
            hash,
            Base64.NO_WRAP
        )

        // Guardamos también el algoritmo y las iteraciones.
        return "v2\$${algoritmo}\$${ITERACIONES}\$${salCodificada}\$${hashCodificado}"
    }

    fun verificar(
        contrasena: String,
        hashGuardado: String
    ): Boolean {

        return try {

            val partes = hashGuardado.split("$")

            if (partes.size != 5 || partes[0] != "v2") {
                return false
            }

            val algoritmo = partes[1]
            val iteraciones = partes[2].toInt()

            if (algoritmo != SHA256 && algoritmo != SHA1) {
                return false
            }

            if (iteraciones <= 0) {
                return false
            }

            val sal = Base64.decode(
                partes[3],
                Base64.NO_WRAP
            )

            val hashOriginal = Base64.decode(
                partes[4],
                Base64.NO_WRAP
            )

            val hashIngresado = calcularHash(
                contrasena = contrasena,
                sal = sal,
                algoritmo = algoritmo,
                iteraciones = iteraciones
            )

            MessageDigest.isEqual(
                hashOriginal,
                hashIngresado
            )

        } catch (e: Exception) {
            false
        }
    }

    private fun obtenerAlgoritmoDisponible(): String {

        return try {
            SecretKeyFactory.getInstance(SHA256)
            SHA256
        } catch (e: Exception) {
            SHA1
        }
    }

    private fun calcularHash(
        contrasena: String,
        sal: ByteArray,
        algoritmo: String,
        iteraciones: Int = ITERACIONES
    ): ByteArray {

        val especificacion = PBEKeySpec(
            contrasena.toCharArray(),
            sal,
            iteraciones,
            LONGITUD_HASH
        )

        return try {

            SecretKeyFactory
                .getInstance(algoritmo)
                .generateSecret(especificacion)
                .encoded

        } finally {
            especificacion.clearPassword()
        }
    }
}