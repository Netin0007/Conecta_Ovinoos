package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.ui.theme.*

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(TerraBarro, Color(0xFF5D4037))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "🐑",
            fontSize = 80.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            "CONECTA:OVINOS",
            color = SolNordeste,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        
        Text(
            "O campo e a cidade em um só lugar.",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        RoleCard(
            title = "SOU PRODUTOR",
            description = "Quero gerenciar meu rebanho e vender meus produtos.",
            icon = "🚜",
            color = VerdeCaatinga,
            onClick = {
                navController.navigate("inventory") {
                    popUpTo("selection") { inclusive = true }
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        RoleCard(
            title = "QUERO COMPRAR",
            description = "Quero encontrar os melhores produtos do Sertão.",
            icon = "🛍️",
            color = SolNordeste,
            textColor = TextoPrincipal,
            onClick = {
                navController.navigate("marketplace_buyer") {
                    popUpTo("selection") { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: String,
    color: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 40.sp)
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = description,
                    color = textColor.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
