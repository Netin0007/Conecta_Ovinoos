package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    navController: NavController,
    onLogout: () -> Unit = {},
    onSwitchToProducer: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Animais", "Derivados", "Equipamentos")

    val anunciosDisponiveis = rebanhoGlobal.filter {
        (categoriaSelecionada == "Todos" ||
                (categoriaSelecionada == "Animais" && it is Animal) ||
                (categoriaSelecionada == "Derivados" && it !is Animal)) &&
                it.nome.contains(searchQuery, ignoreCase = true)
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
                    // BOTÃO: VOLTAR PARA A PROPRIEDADE DO PRODUTOR
                    IconButton(onClick = onSwitchToProducer, modifier = Modifier.size(48.dp)) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Ir para a minha propriedade",
                            tint = SolNordeste,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // BOTÃO: SAIR
                    IconButton(onClick = onLogout, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair da Feira", modifier = Modifier.size(28.dp))
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(TerraBarro, TerraBarro.copy(alpha = 0.9f))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Bom dia, comprador! ☀️",
                        color = SolNordeste,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "O melhor do Sertão,\ndireto para si.",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 36.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Ex: Ovelha, Queijo...", color = Color.Gray, fontSize = 16.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = TerraBarro, modifier = Modifier.size(28.dp)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = TerraBarro
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    // CORREÇÃO: Removido o caminho gigante. O Kotlin entende qual 'items' usar!
                    items(categorias) { categoria ->
                        FilterChip(
                            selected = categoriaSelecionada == categoria,
                            onClick = { categoriaSelecionada = categoria },
                            label = {
                                Text(
                                    categoria,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (categoriaSelecionada == categoria) TextoPrincipal else Color.DarkGray
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SolNordeste,
                                containerColor = Color.White
                            ),
                            border = null,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.height(48.dp)
                        )
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = VerdeCaatinga),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🚚", fontSize = 48.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Apoie o Produtor Local!", color = SolNordeste, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Portes grátis para entregas na região de Iguatu.", color = Color.White, fontSize = 14.sp, lineHeight = 18.sp)
                        }
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Destaques da Região",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = TextoPrincipal,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            if (anunciosDisponiveis.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Nenhum produto encontrado.", color = Color.Gray, fontSize = 16.sp)
                    }
                }
            } else {
                items(anunciosDisponiveis) { produto ->
                    MarketplaceGridCard(
                        produto = produto,
                        // NAVEGAÇÃO: Envia o ID do produto para a nova ecrã de detalhes
                        onProductClick = { id ->
                            navController.navigate("product_details/$id")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MarketplaceGridCard(produto: com.example.conectaovinos.models.Produto, onProductClick: (String) -> Unit) {
    val precoVenda = produto.custo * 1.5
    val isAnimal = produto is Animal
    val raca = if (produto is Animal) produto.raca else "Derivado"
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        // O cartão inteiro é clicável e abre os detalhes
        modifier = Modifier.fillMaxWidth().clickable { onProductClick(produto.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Brush.verticalGradient(colors = listOf(CinzaAreia, Color(0xFFE2DED4)))),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isAnimal) "🐑" else "🧀", fontSize = 72.sp)

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar produto",
                        tint = if (isFavorite) VermelhoBarro else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = produto.nome.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = SolNordeste, modifier = Modifier.size(16.dp))
                        Text("4.9", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 2.dp))
                    }
                }

                Text(text = raca, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(precoVenda),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = VerdeCaatinga
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Fazenda Esperança", fontSize = 12.sp, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    // O botão também abre os detalhes
                    onClick = { onProductClick(produto.id) },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TerraBarro, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("VER DETALHES", fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}