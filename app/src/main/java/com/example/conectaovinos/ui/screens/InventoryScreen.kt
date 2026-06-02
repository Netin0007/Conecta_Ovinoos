package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    onSwitchToBuyer: () -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )

    val produtos by viewModel.produtos.collectAsState()
    val valorTotalCusto = produtos.sumOf { it.custoTotal }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("MEU ESTOQUE", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TerraBarro, titleContentColor = Color.White),
                actions = {
                    IconButton(onClick = onSwitchToBuyer) {
                        Icon(
                            Icons.Rounded.Storefront,
                            contentDescription = "Ir para Feira",
                            tint = SolNordeste
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_product") },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Lote")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            Box(modifier = Modifier.fillMaxWidth().background(TerraBarro).padding(24.dp)) {
                Column {
                    Text("Investimento em Produção", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
                    Text(
                        text = formatCurrency(valorTotalCusto),
                        color = SolNordeste,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            if (produtos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Rounded.Inventory2, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Seu estoque está vazio.", color = Color.Gray, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Toque no + para registrar.", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(produtos, key = { it.id }) { product ->
                        InventoryItemCard(
                            product = product,
                            onEditClick = {
                                navController.navigate("add_product/${product.id}")
                            },
                            onSellClick = {
                                if (product is AnimalLote) {
                                    navController.navigate("create_ad/${product.id}")
                                }
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun InventoryItemCard(
    product: Produto,
    onEditClick: () -> Unit,
    onSellClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(CinzaAreia),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (product is AnimalLote) Icons.Rounded.Pets else Icons.Rounded.Inventory2,
                    contentDescription = null,
                    tint = TerraBarro
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nomeAmigavel.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Custo Total: ${formatCurrency(product.custoTotal)}",
                    fontSize = 12.sp,
                    color = TerraBarro,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Editar", tint = Color.Gray)
                }

                if (product is AnimalLote) {
                    Button(
                        onClick = onSellClick,
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeCaatinga),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("VENDER", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}