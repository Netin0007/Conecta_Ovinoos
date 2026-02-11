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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.database.mappers.toAnimalModel
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.viewmodel.AnimalViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    animalViewModel: AnimalViewModel
) {

    // 🔥 BUSCA OS ANIMAIS DO BANCO (ROOM)
    val animalEntities = animalViewModel.animals.collectAsState(initial = emptyList()).value

    // Converte AnimalEntity -> Animal (Model UI)
    val products: List<Produto> = animalEntities.map { it.toAnimalModel() }

    val totalItens = products.size
    val valorTotalCusto = products.sumOf { it.custo }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Meu Inventário", fontWeight = FontWeight.ExtraBold)
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
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Produto", tint = Color.White)
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

                items(products) { product ->
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

// ================== HEADER ======================

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
                        colors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C))
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
                    Text("Patrimônio Total", color = Color.White.copy(alpha = 0.85f))
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
                        Text(total.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Itens", color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// ================== ITEM LISTA ======================

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

            // MINIATURA DA IMAGEM (substitui o emoji)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                val animalFotoUri = (product as? Animal)?.fotoUri

                if (!animalFotoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = animalFotoUri,
                        contentDescription = "Foto do animal",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback caso não tenha foto
                    Text(
                        text = if (product is Animal) "🐑" else "📦",
                        fontSize = 32.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Surface(
                    color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "ANIMAL",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32)
                    )
                }

                Text(
                    text = product.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                if (product is Animal) {
                    Text(
                        text = "Raça: ${product.raca}",
                        color = Color.Gray
                    )
                }
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
                    color = Color.LightGray
                )
            }
        }
    }
}

// ================== FUNÇÃO DINHEIRO ======================

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}
