package com.example.conectaovinos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

/**
 * Componente reutilizável que representa o cartão de um anúncio na vitrine.
 * Segue os princípios de UX Rural com botões de área de toque expandida.
 *
 * @param produto A entidade Produto (pode ser Animal ou Derivado) a ser exibida.
 * @param onProductClick Função de callback disparada quando o usuário clica no cartão ou no botão de detalhes.
 */
@Composable
fun ProductCard(
    produto: Produto,
    onProductClick: (String) -> Unit
) {
    val precoVenda = produto.custo * 1.5
    val isAnimal = produto is Animal
    val raca = if (produto is Animal) produto.raca else "Derivado"

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(produto.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .background(Brush.verticalGradient(colors = listOf(CinzaAreia, Color(0xFFE2DED4)))),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isAnimal) "🐑" else "🧀", fontSize = 72.sp)

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar produto",
                        tint = if (isFavorite) VermelhoBarro else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = produto.nome.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = SolNordeste, modifier = Modifier.size(18.dp))
                        Text("4.9", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 2.dp))
                    }
                }

                Text(text = raca, color = Color.Gray, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(precoVenda),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = VerdeCaatinga
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tauá, CE", fontSize = 14.sp, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onProductClick(produto.id) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TerraBarro, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("VER DETALHES", fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}