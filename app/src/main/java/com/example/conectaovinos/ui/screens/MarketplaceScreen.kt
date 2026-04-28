package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
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
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.rebanhoGlobal
import com.example.conectaovinos.ui.components.ProductCard
import com.example.conectaovinos.ui.theme.*

/**
 * Tela de Marketplace (Feira Livre).
 * Atua como vitrine para compradores visualizarem os animais e produtos disponíveis.
 *
 * Implementa um design responsivo (`GridCells.Adaptive`) para suportar tanto
 * smartphones comuns quanto tablets usados no campo.
 *
 * @param navController Controlador de navegação do Jetpack Compose, usado para abrir os detalhes do anúncio.
 * @param onLogout Função de callback disparada ao clicar no botão de saída (deslogar).
 * @param onSwitchToProducer Função de callback que permite ao produtor retornar à tela de gestão (Sítio).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    navController: NavController,
    onLogout: () -> Unit = {},
    onSwitchToProducer: () -> Unit = {}
) {
    // --- GERENCIAMENTO DE ESTADO (State) ---
    var searchQuery by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Animais", "Derivados", "Equipamentos")

    // --- LÓGICA DE FILTRAGEM ---
    val anunciosDisponiveis = rebanhoGlobal.filter {
        (categoriaSelecionada == "Todos" ||
                (categoriaSelecionada == "Animais" && it is Animal) ||
                (categoriaSelecionada == "Derivados" && it !is Animal)) &&
                it.nome.contains(searchQuery, ignoreCase = true)
    }

    // --- ESTRUTURA VISUAL (UI) ---
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
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CABEÇALHO DA FEIRA (Banner + Pesquisa)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(listOf(TerraBarro, TerraBarro.copy(alpha = 0.9f))),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
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
                            .height(64.dp),
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

            // CHIPS DE CATEGORIA
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // SOLUÇÃO À PROVA DE ERROS: Usando 'count' e 'index'
                    items(count = categorias.size) { index ->
                        val categoria = categorias[index]

                        FilterChip(
                            selected = categoriaSelecionada == categoria,
                            onClick = { categoriaSelecionada = categoria },
                            label = { Text(categoria, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (categoriaSelecionada == categoria) TextoPrincipal else Color.DarkGray) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SolNordeste, containerColor = Color.White),
                            border = null,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.height(56.dp)
                        )
                    }
                }
            }

            // LISTA DE PRODUTOS OU ESTADO VAZIO
            if (anunciosDisponiveis.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Nenhum produto encontrado.", color = Color.Gray, fontSize = 18.sp)
                    }
                }
            } else {
                // SOLUÇÃO À PROVA DE ERROS: Usando 'count' e 'index'
                items(count = anunciosDisponiveis.size) { index ->
                    val produto = anunciosDisponiveis[index]

                    // Chamada limpa do componente encapsulado
                    ProductCard(
                        produto = produto,
                        onProductClick = { id -> navController.navigate("product_details/$id") }
                    )
                }
            }
        }
    }
}