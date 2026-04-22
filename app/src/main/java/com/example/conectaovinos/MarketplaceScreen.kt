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
                    // BOTÕES MAIORES (56dp em vez de 48dp)
                    IconButton(onClick = onSwitchToProducer, modifier = Modifier.size(56.dp)) {
                        Icon(Icons.Filled.Home, contentDescription = "Ir para a minha propriedade", tint = SolNordeste, modifier = Modifier.size(32.dp))
                    }
                    IconButton(onClick = onLogout, modifier = Modifier.size(56.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair da Feira", modifier = Modifier.size(32.dp))
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            // MÁGICA DA RESPONSIVIDADE: Calcula colunas automaticamente com base na largura da tela!
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Este card agora estica ocupando toda a largura, independente se é tablet ou celular
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(listOf(TerraBarro, TerraBarro.copy(alpha = 0.9f))),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp) // Aumentei o padding interno para telas grandes
                ) {
                    Text("Bom dia, comprador! ☀️", color = SolNordeste, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("O melhor do Sertão,\ndireto para si.", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black, lineHeight = 36.sp, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Ex: Ovelha, Queijo...", color = Color.Gray, fontSize = 16.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = TerraBarro, modifier = Modifier.size(28.dp)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp), // CAIXA DE PESQUISA MAIOR
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

            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(categorias) { categoria ->
                        FilterChip(
                            selected = categoriaSelecionada == categoria,
                            onClick = { categoriaSelecionada = categoria },
                            label = { Text(categoria, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (categoriaSelecionada == categoria) TextoPrincipal else Color.DarkGray) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SolNordeste, containerColor = Color.White),
                            border = null,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.height(56.dp) // CHIPS MAIORES PARA CLIQUE FÁCIL
                        )
                    }
                }
            }

            if (anunciosDisponiveis.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Nenhum produto encontrado.", color = Color.Gray, fontSize = 18.sp)
                    }
                }
            } else {
                items(anunciosDisponiveis) { produto ->
                    MarketplaceGridCard(
                        produto = produto,
                        onProductClick = { id -> navController.navigate("product_details/$id") }
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
        modifier = Modifier.fillMaxWidth().clickable { onProductClick(produto.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f) // Ajusta a foto para não ficar esmagada em celulares finos
                    .background(Brush.verticalGradient(colors = listOf(CinzaAreia, Color(0xFFE2DED4)))),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isAnimal) "🐑" else "🧀", fontSize = 72.sp)

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(56.dp).background(Color.White.copy(alpha = 0.8f), CircleShape) // BOTÃO DE FAVORITO MAIOR
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favoritar produto",
                        tint = if (isFavorite) VermelhoBarro else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) { // Mais espaço interno
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = produto.nome.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp, // LETRA MAIOR
                        color = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f) // Previne que o nome empurre as estrelas para fora da tela
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = SolNordeste, modifier = Modifier.size(18.dp))
                        Text("4.9", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 2.dp))
                    }
                }

                Text(text = raca, color = Color.Gray, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(precoVenda),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp, // PREÇO BEM MAIOR
                    color = VerdeCaatinga
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Iguatu, CE", fontSize = 14.sp, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onProductClick(produto.id) },
                    modifier = Modifier.fillMaxWidth().height(56.dp), // ACESSIBILIDADE: BOTÃO GIGANTE (passou de 44 para 56dp)
                    colors = ButtonDefaults.buttonColors(containerColor = TerraBarro, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("VER DETALHES", fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}