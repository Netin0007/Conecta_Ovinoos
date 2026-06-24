package com.example.conectaovinos.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryEvent
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navController: NavController
) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MEU ESTOQUE",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("feira") }) {
                        Icon(
                            Icons.Rounded.Storefront,
                            contentDescription = "Ir para a Feira",
                            tint = SolNordeste,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("add_product") },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                shape = RoundedCornerShape(16.dp),
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                text = {
                    Text(
                        "ADICIONAR PRODUTO",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            PainelResumoEstoque(
                valorTotal = uiState.valorTotalEstoque,
                qtdAnimais = uiState.animais.size,
                qtdDerivados = uiState.derivados.size
            )

            TabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                containerColor = Color.White,
                contentColor = TerraBarro,
                indicator = { tabPositions ->
                    if (uiState.selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[uiState.selectedTabIndex]
                            ),
                            color = TerraBarro,
                            height = 4.dp
                        )
                    }
                }
            ) {
                InventoryTab(
                    emoji = "🐑",
                    label = "Animais",
                    count = uiState.animais.size,
                    isSelected = uiState.selectedTabIndex == 0,
                    onClick = { viewModel.onEvent(InventoryEvent.SelectTab(0)) }
                )
                InventoryTab(
                    emoji = "🧀",
                    label = "Derivados",
                    count = uiState.derivados.size,
                    isSelected = uiState.selectedTabIndex == 1,
                    onClick = { viewModel.onEvent(InventoryEvent.SelectTab(1)) }
                )
            }

            when {
                uiState.isLoading -> {
                    LoadingEstoque()
                }
                uiState.selectedTabIndex == 0 && uiState.animais.isEmpty() -> {
                    EstoqueVazio(
                        emoji = "🐑",
                        mensagem = "Nenhum animal cadastrado ainda.",
                        dica = "Toque em ADICIONAR PRODUTO para incluir seu primeiro animal."
                    )
                }
                uiState.selectedTabIndex == 1 && uiState.derivados.isEmpty() -> {
                    EstoqueVazio(
                        emoji = "🧀",
                        mensagem = "Nenhum derivado cadastrado ainda.",
                        dica = "Toque em ADICIONAR PRODUTO para incluir queijo, leite, carne e mais."
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 120.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.selectedTabIndex == 0) {
                            items(
                                items = uiState.animais,
                                key = { it.id }
                            ) { animal ->
                                AnimalCardItem(
                                    animal = animal,
                                    onEditClick = {
                                        navController.navigate("add_product/${animal.id}")
                                    },
                                    onSellClick = {
                                        navController.navigate("create_ad/${animal.id}")
                                    },
                                    onDetailsClick = {
                                        navController.navigate("animal_details/${animal.id}")
                                    },
                                    onDeleteClick = {
                                        viewModel.onEvent(InventoryEvent.DeleteItem(animal.id))
                                    }
                                )
                            }
                        } else {
                            items(
                                items = uiState.derivados,
                                key = { it.id }
                            ) { derivado ->
                                DerivadoCardItem(
                                    derivado = derivado,
                                    onEditClick = {
                                        navController.navigate("add_product/${derivado.id}")
                                    },
                                    onSellClick = {
                                        navController.navigate("create_ad/${derivado.id}")
                                    },
                                    onDeleteClick = {
                                        viewModel.onEvent(InventoryEvent.DeleteItem(derivado.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PainelResumoEstoque(
    valorTotal: Double,
    qtdAnimais: Int,
    qtdDerivados: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(TerraBarro)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Column {
            Text(
                text = "Valor Estimado do Estoque",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatarMoeda(valorTotal),
                color = SolNordeste,
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiniContador(
                    emoji = "🐑",
                    label = "Animais",
                    valor = qtdAnimais.toString(),
                    modifier = Modifier.weight(1f)
                )
                MiniContador(
                    emoji = "🧀",
                    label = "Derivados",
                    valor = qtdDerivados.toString(),
                    modifier = Modifier.weight(1f)
                )
                MiniContador(
                    emoji = "📦",
                    label = "Total",
                    valor = (qtdAnimais + qtdDerivados).toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MiniContador(emoji: String, label: String, valor: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 18.sp)
            Text(
                text = valor,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun InventoryTab(
    emoji: String,
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$label ($count)",
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                color = if (isSelected) TerraBarro else Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AnimalCardItem(
    animal: AnimalLote,
    onEditClick: () -> Unit,
    onSellClick: () -> Unit,
    onDetailsClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CinzaAreia),
                    contentAlignment = Alignment.Center
                ) {
                    if (animal.imageUrls.isNotEmpty()) {
                        AsyncImage(
                            model = animal.imageUrls.first(),
                            contentDescription = animal.nomeAmigavel,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = emojiParaEspecie(animal.especie),
                            fontSize = 26.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = animal.nomeAmigavel,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${animal.especie} • ${animal.raca} • ${animal.sexo}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onEditClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "Editar animal",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Excluir animal",
                        tint = Color.Red.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CinzaAreia, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoChip(icone = "🏷️", texto = "Brinco: ${animal.brinco.ifBlank { "—" }}")
                InfoChip(icone = "⚖️", texto = "${animal.peso} kg")
                if (animal.vacinado) {
                    InfoChip(icone = "💉", texto = "Vacinado", destaque = true)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Valor estimado",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = formatarMoeda(animal.custoTotal),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = VerdeCaatinga
                    )
                }

                Button(
                    onClick = onSellClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCaatinga,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        Icons.Rounded.Storefront,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "VENDER",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DerivadoCardItem(
    derivado: ProdutoProcessado,
    onEditClick: () -> Unit,
    onSellClick: () -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CinzaAreia),
                    contentAlignment = Alignment.Center
                ) {
                    if (derivado.imageUrls.isNotEmpty()) {
                        AsyncImage(
                            model = derivado.imageUrls.first(),
                            contentDescription = derivado.tipoProduto,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = emojiParaDerivado(derivado.tipoProduto),
                            fontSize = 26.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = derivado.tipoProduto.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Lote: ${derivado.codigoLote.ifBlank { "—" }}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onEditClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "Editar derivado",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Excluir derivado",
                        tint = Color.Red.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CinzaAreia, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoChip(icone = "📦", texto = "${derivado.quantidade} ${derivado.unidadeMedida}")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Valor estimado",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = formatarMoeda(derivado.custoTotal),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = VerdeCaatinga
                    )
                }

                Button(
                    onClick = onSellClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCaatinga,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        Icons.Rounded.Storefront,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "VENDER",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoChip(
    icone: String,
    texto: String,
    destaque: Boolean = false,
    maxChars: Int = Int.MAX_VALUE
) {
    val textoExibido = if (texto.length > maxChars) "${texto.take(maxChars)}…" else texto

    Surface(
        color = if (destaque) VerdeCaatinga.copy(alpha = 0.12f) else CinzaAreia.copy(alpha = 0.6f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icone, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = textoExibido,
                fontSize = 12.sp,
                color = if (destaque) VerdeCaatinga else TextoPrincipal.copy(alpha = 0.75f),
                fontWeight = if (destaque) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun LoadingEstoque() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = TerraBarro,
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp
            )
            Text(
                text = "Carregando seu estoque...",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun EstoqueVazio(emoji: String, mensagem: String, dicsa: String, dica: String) { // dicsa typo in original or not? Let's check.
    // wait, I see "dicsa" in my previous write. I'll fix it to "dica".
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(emoji, fontSize = 72.sp)
            Text(
                text = mensagem,
                color = TextoPrincipal,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = SolNordeste.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "💡 $dica",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = TextoPrincipal.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// Helper to make it match previous function signature if needed
@Composable
private fun EstoqueVazio(emoji: String, mensagem: String, dica: String) {
    EstoqueVazio(emoji, mensagem, "", dica)
}

private fun emojiParaEspecie(animalType: String): String {
    return when {
        animalType.contains("Bovino", ignoreCase = true) -> "🐄"
        animalType.contains("Caprino", ignoreCase = true) -> "🐐"
        animalType.contains("Suíno", ignoreCase = true) -> "🐷"
        animalType.contains("Equino", ignoreCase = true) -> "🐴"
        else -> "🐑"
    }
}

private fun emojiParaDerivado(productType: String): String {
    return when {
        productType.contains("Queijo", ignoreCase = true) -> "🧀"
        productType.contains("Leite", ignoreCase = true) -> "🥛"
        productType.contains("Carne", ignoreCase = true) -> "🥩"
        productType.contains("Lã", ignoreCase = true) -> "🧶"
        productType.contains("Pele", ignoreCase = true) -> "🫙"
        productType.contains("Manta", ignoreCase = true) -> "🧣"
        productType.contains("Iogurte", ignoreCase = true) -> "🍶"
        productType.contains("Manteiga", ignoreCase = true) -> "🧈"
        else -> "📦"
    }
}

private fun formatarMoeda(valor: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}
