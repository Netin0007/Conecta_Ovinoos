package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.Anuncio
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    navController: NavController,
    onLogout: () -> Unit = {},
    onSwitchToProducer: () -> Unit = {},
    viewModel: AnuncioViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Animais", "Derivados")

    // Apenas anúncios ativos, publicados pelo produtor
    val anunciosAtivos by viewModel.anunciosAtivos.collectAsState()
    val anunciosFiltrados = anunciosAtivos.filter { anuncio ->
        anuncio.nomeAnimal.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("FEIRA LIVRE", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onSwitchToProducer, modifier = Modifier.size(56.dp)) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Minha propriedade",
                            tint = SolNordeste,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    IconButton(onClick = onLogout, modifier = Modifier.size(56.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner + busca
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(TerraBarro, TerraBarro.copy(alpha = 0.9f))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        "Bom dia, comprador! ☀️",
                        color = SolNordeste,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "O melhor do Sertão,\ndireto para si.",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 36.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text("Ex: Ovelha, Mococa...", color = Color.Gray, fontSize = 16.sp)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = TerraBarro,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            }

            // Chips de categoria
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(count = categorias.size) { index ->
                        val categoria = categorias[index]
                        FilterChip(
                            selected = categoriaSelecionada == categoria,
                            onClick = { categoriaSelecionada = categoria },
                            label = {
                                Text(
                                    categoria,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (categoriaSelecionada == categoria) TextoPrincipal else Color.DarkGray
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SolNordeste,
                                containerColor = Color.White
                            ),
                            border = null,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.height(56.dp)
                        )
                    }
                }
            }

            // Lista ou estado vazio
            if (anunciosFiltrados.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🏪", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Nenhum produto disponível",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                items(count = anunciosFiltrados.size) { index ->
                    AnuncioMarketplaceCard(
                        anuncio = anunciosFiltrados[index],
                        onVerDetalhes = { id ->
                            navController.navigate("product_details/$id")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnuncioMarketplaceCard(anuncio: Anuncio, onVerDetalhes: (String) -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVerDetalhes(anuncio.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .background(CinzaAreia),
                contentAlignment = Alignment.Center
            ) {
                Text("🐑", fontSize = 72.sp)
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.8f), androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            androidx.compose.material.icons.Icons.Filled.Favorite
                        else
                            androidx.compose.material.icons.Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar",
                        tint = if (isFavorite) VermelhoBarro else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    anuncio.nomeAnimal.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = TextoPrincipal,
                    maxLines = 1
                )
                Text(anuncio.racaAnimal, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                        .format(anuncio.precoVenda),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = VerdeCaatinga
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        androidx.compose.material.icons.Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = TerraBarro,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tauá, CE", fontSize = 13.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { onVerDetalhes(anuncio.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TerraBarro,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("VER DETALHES", fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
        }
    }
}