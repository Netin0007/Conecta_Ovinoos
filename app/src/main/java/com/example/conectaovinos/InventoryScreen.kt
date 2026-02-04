package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoDerivado
import java.text.NumberFormat
import java.util.*
import com.example.conectaovinos.ui.theme.*

val dummyProductList = listOf<Produto>(
    Animal(id = "1", nome = "Mococa 01", raca = "Santa Inês", dataNascimento = "10/05/2023", custo = 250.0),
    ProdutoDerivado(id = "p1", nome = "Queijo de Cabra", unidadeDeMedida = "Peça de 500g", custo = 15.0),
    Animal(id = "2", nome = "Brinco 142", raca = "Dorper", dataNascimento = "02/01/2024", custo = 300.0),
    ProdutoDerivado(id = "p2", nome = "Leite de Ovelha", unidadeDeMedida = "Garrafa de 1L", custo = 5.0),
    Animal(id = "3", nome = "Fumacinha", raca = "SRD", dataNascimento = "25/08/2022", custo = 220.0),
    ProdutoDerivado(id = "p3", nome = "Mel Silvestre", unidadeDeMedida = "Pote 500g", custo = 25.0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController) {
    val totalItens = dummyProductList.size
    val valorTotalCusto = dummyProductList.sumOf { it.custo }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Meu Inventário",
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_product_form") },
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Adicionar Produto",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
        ) {
            InventorySummaryHeader(totalItens, valorTotalCusto)

            // LISTA DE ITENS
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Patrimônio Cadastrado",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }

                items(dummyProductList) { product ->
                    EnhancedProductListItem(product = product, onClick = {
                        if (product is Animal) {
                            navController.navigate("animal_details/${product.id}")
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun InventorySummaryHeader(total: Int, custoTotal: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF4CAF50),
                            Color(0xFF388E3C)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Patrimônio Total",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatCurrency(custoTotal),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Surface(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = total.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Itens",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedProductListItem(product: Produto, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone visual rápido adaptativo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (product is Animal) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when(product) {
                        is Animal -> "🐑"
                        is ProdutoDerivado -> {
                            if (product.nome.contains("Mel", ignoreCase = true)) "🍯"
                            else if (product.nome.contains("Queijo", ignoreCase = true)) "🧀"
                            else "📦"
                        }
                        else -> "🔹"
                    },
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Badge de Categoria
                Surface(
                    color = if (product is Animal) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color(0xFFFFB74D).copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = if (product is Animal) "ANIMAL" else "PRODUTO",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (product is Animal) Color(0xFF2E7D32) else Color(0xFFE65100)
                    )
                }

                Text(
                    text = product.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF212121)
                )

                Text(
                    text = when (product) {
                        is Animal -> "Raça: ${product.raca}"
                        is ProdutoDerivado -> "Unidade: ${product.unidadeDeMedida}"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatCurrency(product.custo),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "VALOR CUSTO",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}