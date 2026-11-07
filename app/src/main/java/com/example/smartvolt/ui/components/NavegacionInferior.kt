package com.example.smartvolt.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data class OpcionNavegacion(
    val etiqueta: String,
    val simbolo: String
)

@Composable
fun NavegacionInferior(
    opciones: List<OpcionNavegacion>,
    indiceSeleccionado: Int,
    onSeleccionar: (Int) -> Unit
) {
    NavigationBar {
        opciones.forEachIndexed { indice, opcion ->

            NavigationBarItem(
                selected = indiceSeleccionado == indice,
                onClick = {
                    onSeleccionar(indice)
                },
                icon = {
                    Text(text = opcion.simbolo)
                },
                label = {
                    Text(text = opcion.etiqueta)
                },
                alwaysShowLabel = true
            )
        }
    }
}
