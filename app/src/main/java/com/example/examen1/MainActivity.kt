package com.example.examen1

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
fun BarraSuperior(
    titulo: String,
    alVolver: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() }
            )
        },
        navigationIcon = {
            if (alVolver != null) {
                TextButton(
                    onClick = alVolver,
                    modifier = Modifier.heightIn(min = 48.dp)
                ) {
                    Text("Volver")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    alAgregar: () -> Unit,
    alListar: () -> Unit,
    alCerrar: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraSuperior("Biblioteca de Libros") }
    ) { espacioInterno ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(espacioInterno)
                .fillMaxSize()
        ) {
            val margen = margenResponsive(maxWidth)

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = margen, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = Color.White,
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Image(
                        painter = painterResource(R.drawable.imgi_hp),
                        contentDescription = "Escudo de Hogwarts, logotipo de la biblioteca",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(width = 156.dp, height = 220.dp)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "TU BIBLIOTECA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tus libros, en orden",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.semantics { heading() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Guarda y administra tu colección personal de forma sencilla.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = alAgregar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Text("Agregar libro")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = alListar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Text("Ver libros guardados")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = alCerrar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Cerrar aplicación")
                }
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
    var mostrarErrores by remember { mutableStateOf(false) }
    val generos = listOf("Ficción", "Ciencia", "Historia", "Aventura", "Otro")
    val tituloInvalido = mostrarErrores && titulo.isBlank()
    val autorInvalido = mostrarErrores && autor.isBlank()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BarraSuperior(
                titulo = if (libroParaEditar == null) "Agregar libro" else "Editar libro",
                alVolver = alVolver
            )
        }
    ) { espacioInterno ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(espacioInterno)
                .fillMaxSize()
                .imePadding()
        ) {
            val margen = margenResponsive(maxWidth)

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = margen, vertical = 24.dp)
            ) {
                Text(
                    text = if (libroParaEditar == null) {
                        "Nuevo libro"
                    } else {
                        "Actualiza la información"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.semantics { heading() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Completa los campos para mantener organizada tu colección.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título del libro") },
                    supportingText = if (tituloInvalido) {
                        { Text("Ingresa el título del libro") }
                    } else {
                        null
                    },
                    isError = tituloInvalido,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    supportingText = if (autorInvalido) {
                        { Text("Ingresa el nombre del autor") }
                    } else {
                        null
                    },
                    isError = autorInvalido,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                        supportingText = { Text("Selecciona una opción") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto)
                        },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
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
                        mostrarErrores = true

                        if (titulo.isBlank() || autor.isBlank()) {
                            Toast.makeText(
                                contexto,
                                "Revisa los campos señalados",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
                ) {
                    Text(if (libroParaEditar == null) "Guardar libro" else "Guardar cambios")
                }

                Spacer(modifier = Modifier.height(24.dp))
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BarraSuperior(
                titulo = "Libros guardados",
                alVolver = alVolver
            )
        }
    ) { espacioInterno ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(espacioInterno)
                .fillMaxSize()
        ) {
            val margen = margenResponsive(maxWidth)

            if (libros.isEmpty()) {
                EstadoSinLibros(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .widthIn(max = 520.dp)
                        .padding(horizontal = margen)
                )
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxSize()
                        .widthIn(max = 760.dp)
                        .padding(horizontal = margen)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Tu colección",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.semantics { heading() }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (libros.size == 1) "1 libro guardado" else "${libros.size} libros guardados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp),
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
    }
}

@Composable
fun EstadoSinLibros(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo_biblioteca),
            contentDescription = null,
            modifier = Modifier.size(88.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Tu biblioteca está vacía",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agrega tu primer libro desde la pantalla principal.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TarjetaLibro(
    libro: Libro,
    alEditar: () -> Unit,
    alEliminar: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "de ${libro.autor}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = libro.genero,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = alEditar,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = alEliminar,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

fun margenResponsive(ancho: Dp): Dp {
    return when {
        ancho < 360.dp -> 16.dp
        ancho < 600.dp -> 24.dp
        else -> 32.dp
    }
}
