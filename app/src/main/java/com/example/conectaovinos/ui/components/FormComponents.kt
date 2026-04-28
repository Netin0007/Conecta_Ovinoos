package com.example.conectaovinos.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectaovinos.ui.theme.*

/**
 * Cartão de seleção grande, ideal para escolher entre grandes categorias (ex: Animal vs Produto).
 *
 * @param title Texto exibido no cartão.
 * @param icon Ícone (Emoji ou Texto) exibido ao lado do título.
 * @param isSelected Define se o cartão está com a cor de destaque (selecionado).
 * @param modifier Modificador para ajustes de layout.
 * @param onClick Função disparada ao clicar no cartão.
 */
@Composable
fun TypeSelectionCard(title: String, icon: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TerraBarro else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Black,
                color = if (isSelected) Color.White else TerraBarro
            )
        }
    }
}

/**
 * Chip de seleção arredondado, utilizado para listas de opções horizontais (ex: Raças, Unidades).
 *
 * @param text Nome da opção.
 * @param isSelected Define se o chip está pintado com a cor de seleção.
 * @param onClick Função disparada ao selecionar o chip.
 */
@Composable
fun SelectableChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) VerdeCaatinga else Color.White,
        border = if (!isSelected) BorderStroke(1.dp, Color.LightGray) else null,
        modifier = Modifier.height(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = text, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) Color.White else TextoPrincipal)
        }
    }
}

/**
 * Título padronizado para dividir seções dentro de um formulário.
 *
 * @param text O texto do título da seção.
 */
@Composable
fun FormSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

/**
 * Campo de texto customizado com a identidade visual do app "Sertão".
 *
 * @param value Valor atual do campo de texto.
 * @param onValueChange Função disparada sempre que o texto muda.
 * @param label Rótulo flutuante do campo.
 * @param keyboardType Tipo de teclado a ser aberto (Numérico, Texto, etc).
 * @param icon Ícone opcional à esquerda do texto.
 * @param placeholder Texto de dica opcional (cinza).
 * @param helperText Texto pequeno de ajuda exibido abaixo do campo.
 */
@Composable
fun SertaoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null,
    placeholder: String? = null,
    helperText: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = if (placeholder != null) { { Text(placeholder) } } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = if (icon != null) { { Icon(icon, null, tint = TerraBarro) } } else null,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TerraBarro,
                focusedLabelColor = TerraBarro,
                cursorColor = TerraBarro,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )
        if (helperText != null) {
            Text(text = helperText, style = MaterialTheme.typography.bodySmall, color = VerdeCaatinga, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
        }
    }
}