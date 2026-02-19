package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
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
import com.example.conectaovinos.models.ProdutoDerivado
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController, onLogout: () -> Unit = {}) {
    val valorTotalCusto = rebanhoGlobal.sumOf { it.custo }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("O MEU INVENTÁRIO", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_product_form") },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Filled.Add, "Novo", modifier = Modifier.size(32.dp))
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
                    Text("Investimento em Produção", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(
                        text = formatCurrency(valorTotalCusto),
                        color = SolNordeste,
                        fontSize = 32.sp,
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
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)).background(CinzaAreia),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (product is Animal) "🐑" else "🧀", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.nome.uppercase(), fontWeight = FontWeight.Black, color = TextoPrincipal, fontSize = 14.sp)
                Text(
                    text = if (product is Animal) "Raça: ${product.raca}" else "Derivado",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = formatCurrency(product.custo),
                fontWeight = FontWeight.Bold,
                color = VerdeCaatinga,
                fontSize = 16.sp
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}