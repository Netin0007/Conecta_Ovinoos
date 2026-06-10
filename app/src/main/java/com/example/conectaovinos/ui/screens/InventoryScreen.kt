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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.AnimalModel
import com.example.conectaovinos.models.DerivadoModel
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryEvent
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController,
    viewModel: InventoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("MEU ESTOQUE", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("marketplace") }) {
                        Icon(Icons.Rounded.Storefront, contentDescription = "Feira", tint = Color.White)
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
                Icon(Icons.Filled.Add, contentDescription = "Adicionar")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            // 1. PAINEL FINANCEIRO
            Box(modifier = Modifier.fillMaxWidth().background(TerraBarro).padding(24.dp)) {
                Column {
                    Text("Valor Estimado do Estoque", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                    Text(
                        text = formatarMoeda(uiState.valorTotalEstoque),
                        color = SolNordeste,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // 2. ABAS (TABS) DE NAVEGAÇÃO
            TabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                containerColor = Color.White,
                contentColor = TerraBarro,
                indicator = { tabPositions ->
                    if (uiState.selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTabIndex]),
                            color = TerraBarro,
                            height = 3.dp
                        )
                    }
                }
            ) {
                Tab(
                    selected = uiState.selectedTabIndex == 0,
                    onClick = { viewModel.onEvent(InventoryEvent.SelectTab(0)) },
                    text = { Text("🐑 Animais (${uiState.animais.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = uiState.selectedTabIndex == 1,
                    onClick = { viewModel.onEvent(InventoryEvent.SelectTab(1)) },
                    text = { Text("🧀 Derivados (${uiState.derivados.size})", fontWeight = FontWeight.Bold) }
                )
            }

            // 3. CONTEÚDO (LISTA OU LOADING)
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TerraBarro)
                }
            } else {
                val listaVazia = (uiState.selectedTabIndex == 0 && uiState.animais.isEmpty()) ||
                        (uiState.selectedTabIndex == 1 && uiState.derivados.isEmpty())

                if (listaVazia) {
                    Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Rounded.Inventory2, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Nenhum item nesta categoria.", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.selectedTabIndex == 0) {
                            items(uiState.animais, key = { it.id }) { animal ->
                                AnimalCardItem(
                                    animal = animal,
                                    onEditClick = { navController.navigate("add_product/${animal.id}") },
                                    onSellClick = { navController.navigate("create_ad/${animal.id}") }
                                )
                            }
                        } else {
                            items(uiState.derivados, key = { it.id }) { derivado ->
                                DerivadoCardItem(
                                    derivado = derivado,
                                    onEditClick = { navController.navigate("add_product/${derivado.id}") },
                                    onSellClick = { navController.navigate("create_ad/${derivado.id}") }
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalCardItem(animal: AnimalModel, onEditClick: () -> Unit, onSellClick: () -> Unit) {
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
                Text(if (animal.animalType.contains("Bovino", true)) "🐄" else "🐑", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                val titulo = animal.name.ifBlank { "${animal.animalType} ${animal.breed}" }
                Text(titulo.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextoPrincipal, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Brinco: ${animal.earTag} • ${animal.weight}kg", fontSize = 12.sp, color = Color.Gray)
                Text(formatarMoeda(animal.salePrice), fontSize = 14.sp, color = VerdeCaatinga, fontWeight = FontWeight.Black)
            }

            AcoesCard(onEditClick, onSellClick)
        }
    }
}

@Composable
fun DerivadoCardItem(derivado: DerivadoModel, onEditClick: () -> Unit, onSellClick: () -> Unit) {
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
                Text(if (derivado.productType.contains("Queijo", true)) "🧀" else "🥩", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(derivado.productType.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextoPrincipal, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Lote: ${derivado.batchCode} • ${derivado.quantity} ${derivado.unit}", fontSize = 12.sp, color = Color.Gray)
                Text(formatarMoeda(derivado.salePrice), fontSize = 14.sp, color = VerdeCaatinga, fontWeight = FontWeight.Black)
            }

            AcoesCard(onEditClick, onSellClick)
        }
    }
}

@Composable
fun AcoesCard(onEditClick: () -> Unit, onSellClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onEditClick) {
            Icon(Icons.Rounded.Edit, contentDescription = "Editar", tint = Color.Gray)
        }
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

private fun formatarMoeda(valor: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}