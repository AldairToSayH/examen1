package com.example.examen1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(
    primary = AzulTinta,
    onPrimary = SobreAzulTinta,
    primaryContainer = AzulClaro,
    onPrimaryContainer = SobreAzulClaro,
    secondary = GrisEditorial,
    onSecondary = SobreAzulTinta,
    secondaryContainer = GrisEditorialClaro,
    onSecondaryContainer = TextoTinta,
    tertiary = AmbarLibro,
    onTertiary = SobreAzulTinta,
    tertiaryContainer = AmbarClaro,
    onTertiaryContainer = TextoTinta,
    background = FondoPapel,
    onBackground = TextoTinta,
    surface = FondoPapel,
    onSurface = TextoTinta,
    surfaceVariant = SuperficieSuave,
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = BordeSuave,
    error = Error,
    onError = SobreAzulTinta,
    errorContainer = ErrorClaro,
    onErrorContainer = TextoTinta
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulTintaOscuro,
    onPrimary = SobreAzulTintaOscuro,
    primaryContainer = AzulContenedorOscuro,
    onPrimaryContainer = SobreAzulContenedorOscuro,
    secondary = GrisEditorialOscuro,
    onSecondary = TextoTinta,
    secondaryContainer = GrisContenedorOscuro,
    onSecondaryContainer = TextoNoche,
    tertiary = AmbarOscuro,
    onTertiary = TextoTinta,
    tertiaryContainer = AmbarContenedorOscuro,
    onTertiaryContainer = AmbarClaro,
    background = FondoNoche,
    onBackground = TextoNoche,
    surface = FondoNoche,
    onSurface = TextoNoche,
    surfaceVariant = SuperficieNoche,
    onSurfaceVariant = TextoSecundarioNoche,
    outline = BordeNoche,
    outlineVariant = BordeSuaveNoche,
    error = ErrorOscuro,
    onError = TextoTinta,
    errorContainer = ErrorContenedorOscuro,
    onErrorContainer = ErrorClaro
)

private val FormasAplicacion = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(22.dp)
)

@Composable
fun Examen1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        shapes = FormasAplicacion,
        content = content
    )
}
