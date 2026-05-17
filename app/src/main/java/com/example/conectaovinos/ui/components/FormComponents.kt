package com.example.conectaovinos.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
 * Título padronizado para dividir seções dentro de um formulário.
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

/**
 * Cartão de seleção de categoria focado em Acessibilidade Motora (Lei de Fitts).
 * Possui área de toque gigante (88dp) e feedback visual imediato de seleção.
 * Utiliza ícones do Material Design para um visual limpo e profissional.
 */
@Composable
fun CategorySelectionCard(
    titulo: String,
    icone: ImageVector,
    descricao: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SolNordeste.copy(alpha = 0.15f) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) SolNordeste else Color.LightGray.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = if (isSelected) SolNordeste else CinzaAreia,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icone,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else TerraBarro,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = if (isSelected) TextoPrincipal else Color.DarkGray
                )
                Text(
                    text = descricao,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = TerraBarro,
                    unselectedColor = Color.LightGray
                )
            )
        }
    }
}