package com.example.examen1

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.examen1.ui.theme.Examen1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Examen1Theme {
                AplicacionLibros()
            }
        }
    }
}

@Composable
fun AplicacionLibros() {
    val contexto = LocalContext.current
    val baseDatos = remember { BaseDatosLibros(contexto) }
    var pantallaActual by remember { mutableStateOf("principal") }
    var libroParaEditar by remember { mutableStateOf<Libro?>(null) }

    when (pantallaActual) {
        "principal" -> PantallaPrincipal(
            alAgregar = {
                libroParaEditar = null
                pantallaActual = "agregar"
            },
            alListar = { pantallaActual = "listar" },
            alCerrar = { (contexto as Activity).finish() }
        )

        "agregar" -> PantallaAgregarLibro(
            baseDatos = baseDatos,
            libroParaEditar = libroParaEditar,
            alVolver = { pantallaActual = "principal" },
            alGuardar = { pantallaActual = "listar" }
        )

        "listar" -> PantallaListarLibros(
            baseDatos = baseDatos,
            alVolver = { pantallaActual = "principal" },
            alEditar = { libro ->
                libroParaEditar = libro
                pantallaActual = "agregar"
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    alAgregar: () -> Unit,
    alListar: () -> Unit,
    alCerrar: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Biblioteca de Libros") })
        }
    ) { espacioInterno ->
        Column(
            modifier = Modifier
                .padding(espacioInterno)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo_biblioteca),
                contentDescription = "Logotipo de la aplicación",
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Mi colección de libros",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = alAgregar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar libro")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = alListar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Listar libros")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = alCerrar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar aplicación")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarLibro(
    baseDatos: BaseDatosLibros,
    libroParaEditar: Libro?,
    alVolver: () -> Unit,
    alGuardar: () -> Unit
) {
    val contexto = LocalContext.current
    var titulo by remember(libroParaEditar) {
        mutableStateOf(libroParaEditar?.titulo ?: "")
    }
    var autor by remember(libroParaEditar) {
        mutableStateOf(libroParaEditar?.autor ?: "")
    }
    var generoSeleccionado by remember(libroParaEditar) {
        mutableStateOf(libroParaEditar?.genero ?: "Ficción")
    }
    var menuAbierto by remember { mutableStateOf(false) }
    val generos = listOf("Ficción", "Ciencia", "Historia", "Aventura", "Otro")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (libroParaEditar == null) "Agregar libro" else "Editar libro")
                },
                navigationIcon = {
                    TextButton(onClick = alVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { espacioInterno ->
        Column(
            modifier = Modifier
                .padding(espacioInterno)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Complete los datos del libro",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del libro") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = menuAbierto,
                onExpandedChange = { menuAbierto = !menuAbierto },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = generoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    generos.forEach { genero ->
                        DropdownMenuItem(
                            text = { Text(genero) },
                            onClick = {
                                generoSeleccionado = genero
                                menuAbierto = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (titulo.isBlank() || autor.isBlank()) {
                        Toast.makeText(
                            contexto,
                            "Complete el título y el autor",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val guardadoCorrectamente = if (libroParaEditar == null) {
                            baseDatos.agregarLibro(
                                titulo.trim(),
                                autor.trim(),
                                generoSeleccionado
                            )
                        } else {
                            baseDatos.editarLibro(
                                libroParaEditar.id,
                                titulo.trim(),
                                autor.trim(),
                                generoSeleccionado
                            )
                        }

                        if (guardadoCorrectamente) {
                            val mensaje = if (libroParaEditar == null) {
                                "Libro guardado correctamente"
                            } else {
                                "Libro editado correctamente"
                            }
                            Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                            alGuardar()
                        } else {
                            Toast.makeText(
                                contexto,
                                "No se pudo guardar el libro",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (libroParaEditar == null) "Guardar libro" else "Guardar cambios")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListarLibros(
    baseDatos: BaseDatosLibros,
    alVolver: () -> Unit,
    alEditar: (Libro) -> Unit
) {
    val contexto = LocalContext.current
    var libros by remember { mutableStateOf(baseDatos.obtenerLibros()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Libros guardados") },
                navigationIcon = {
                    TextButton(onClick = alVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { espacioInterno ->
        if (libros.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(espacioInterno)
                    .padding(24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Todavía no hay libros guardados",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(espacioInterno)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(libros, key = { it.id }) { libro ->
                    TarjetaLibro(
                        libro = libro,
                        alEditar = { alEditar(libro) },
                        alEliminar = {
                            if (baseDatos.eliminarLibro(libro.id)) {
                                libros = baseDatos.obtenerLibros()
                                Toast.makeText(
                                    contexto,
                                    "Libro eliminado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaLibro(
    libro: Libro,
    alEditar: () -> Unit,
    alEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text("Autor: ${libro.autor}")
            Text("Género: ${libro.genero}")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = alEditar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }

                OutlinedButton(
                    onClick = alEliminar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
