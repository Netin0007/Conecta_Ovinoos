package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    onLogout: () -> Unit = {},
    onSwitchToConsumer: () -> Unit = {} // NOVA FUNÇÃO: Mudar para a Feira
) {
    val valorTotalCusto = rebanhoGlobal.sumOf { it.custo }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("O MEU INVENTÁRIO", fontWeight = FontWeight.Black, fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                ),
                actions = {
                    // BOTÃO: IR PARA A FEIRA LIVRE (Mudar de Chapéu)
                    // ACESSIBILIDADE: Tamanho 48dp para toque fácil
                    IconButton(onClick = onSwitchToConsumer, modifier = Modifier.size(48.dp)) {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            contentDescription = "Ir para a Feira Livre",
                            tint = SolNordeste, // Cor de destaque
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // BOTÃO: SAIR DA CONTA
                    IconButton(onClick = onLogout, modifier = Modifier.size(48.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            // ACESSIBILIDADE: Botão Novo maior e mais visível
            FloatingActionButton(
                onClick = { navController.navigate("add_product_form") },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Filled.Add, "Novo Registo", modifier = Modifier.size(36.dp))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TerraBarro)
                    .padding(20.dp)
            ) {
                Column {
                    Text("Investimento em Produção", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    Text(
                        text = formatCurrency(valorTotalCusto),
                        color = SolNordeste,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rebanhoGlobal) { product ->
                    InventoryCard(product) {
                        if (product is Animal) navController.navigate("animal_details/${product.id}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryCard(product: Produto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp), // Cantos mais arredondados
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)).background(CinzaAreia),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (product is Animal) "🐑" else "🧀", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.nome.uppercase(), fontWeight = FontWeight.Black, color = TextoPrincipal, fontSize = 16.sp)
                Text(
                    text = if (product is Animal) "Raça: ${product.raca}" else "Derivado",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = formatCurrency(product.custo),
                fontWeight = FontWeight.Black,
                color = VerdeCaatinga,
                fontSize = 18.sp
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}