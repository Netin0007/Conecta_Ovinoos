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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.ProdutosEntity
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoDerivado
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.viewmodel.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController, onLogout: () -> Unit = {}) {
    val vm: InventoryViewModel = viewModel()
    val items = vm.items.collectAsState().value

    val totalItens = items.size
    val valorTotalCusto = items.sumOf {
        when (it) {
            is InventoryItem.AnimalItem -> it.animal.preco
            is InventoryItem.ProdutoItem -> it.produto.preco
        }
    }

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
            val context = LocalContext.current

//            Button(
//                onClick = {
//                    context.deleteDatabase("conectaovinos.db")
//                }
//            ) {
//                Text("Resetar Banco")
//            }

            LazyColumn(
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

                items(items) { item ->
                    when (item) {
                        is InventoryItem.AnimalItem -> {
                            EnhancedAnimalEntityItem(
                                animal = item.animal,
                                onClick = {
                                    navController.navigate("animal_details/${item.animal.id}")
                                }
                            )
                        }

                        is InventoryItem.ProdutoItem -> {
                            EnhancedProdutoEntityItem(
                                produto = item.produto,
                                onClick = { /* detalhes do produto se quiser */ }
                            )
                        }
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



@Composable
fun EnhancedAnimalEntityItem(animal: AnimalEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ✅ MINIATURA (ANIMAL)
            val uri = animal.fotoUri
            if (!uri.isNullOrBlank()) {
                AsyncImage(
                    model = uri,
                    contentDescription = "Foto do animal",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🐑", fontSize = 32.sp)
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Surface(
                    color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50),
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
                    text = animal.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF212121)
                )

                Text(
                    text = "Raça: ${animal.raca}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatCurrency(animal.preco),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "VALOR",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun EnhancedProdutoEntityItem(produto: ProdutosEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ✅ MINIATURA (PRODUTO)
            val uri = produto.fotoUri
            if (!uri.isNullOrBlank()) {
                AsyncImage(
                    model = uri,
                    contentDescription = "Foto do produto",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFF3E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📦", fontSize = 32.sp)
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Surface(
                    color = Color(0xFFFFB74D).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "PRODUTO",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE65100)
                    )
                }

                Text(
                    text = produto.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF212121)
                )

                Text(
                    text = "Qtd: ${produto.quantidade}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatCurrency(produto.preco),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "VALOR",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
        }
    }
}