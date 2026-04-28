package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectaovinos.ui.components.CardAudioDica
import com.example.conectaovinos.ui.components.CardCotacao
import com.example.conectaovinos.ui.components.CardTextoDica
import com.example.conectaovinos.ui.theme.*
import kotlinx.coroutines.launch

/**
 * Tela de Comunidade e Networking Rural.
 * Permite ao produtor acessar dicas de especialistas (áudio e texto), acompanhar
 * o preço de mercado da sua região e conectar-se com vizinhos.
 *
 * Utiliza um Pager horizontal (`HorizontalPager`) para navegação fluida entre abas.
 *
 * @param navController Controlador de navegação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(navController: NavController) {
    val abas = listOf("Dicas Rápidas", "Mercado da @", "Vizinhos")
    val pagerState = rememberPagerState(pageCount = { abas.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("COMUNIDADE", fontWeight = FontWeight.Black) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TerraBarro,
                        titleContentColor = Color.White
                    )
                )

                // Barra de Navegação Horizontal (Tabs)
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = TerraBarro,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = SolNordeste,
                            height = 4.dp
                        )
                    }
                ) {
                    abas.forEachIndexed { index, titulo ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = {
                                Text(
                                    text = titulo,
                                    fontWeight = if (pagerState.currentPage == index) FontWeight.Black else FontWeight.Normal,
                                    color = if (pagerState.currentPage == index) SolNordeste else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Gerenciador de Deslizamento de Telas
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { paginaAtual ->
            when (paginaAtual) {
                0 -> AbaDicasRapidas()
                1 -> AbaMercado()
                2 -> AbaVizinhos()
            }
        }
    }
}

/**
 * Sub-tela: Exibe lista de dicas formatadas com foco em áudio e texto curto.
 */
@Composable
private fun AbaDicasRapidas() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            CardAudioDica(
                titulo = "Atenção aos vermes nas primeiras chuvas",
                autor = "Dr. João (Veterinário)",
                duracao = "1:45"
            )
        }
        item {
            CardTextoDica(
                titulo = "Corte de Casco",
                texto = "Com o solo molhado, o casco da ovelha amolece e pode pegar podridão. Faça o casqueamento preventivo nesta semana!",
                autor = "Zeca do Emater"
            )
        }
    }
}

/**
 * Sub-tela: Exibe a lista de cotações de mercado para que o produtor tenha
 * uma base de preço antes de negociar com atravessadores.
 */
@Composable
private fun AbaMercado() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            CardCotacao(animal = "Ovelha Gorda (Abate)", preco = "R$ 11,50 / kg vivo", tendencia = "Subiu 0,50")
            Spacer(modifier = Modifier.height(16.dp))
            CardCotacao(animal = "Cordeiro Desmamado", preco = "R$ 250,00 / cabeça", tendencia = "Estável")
            Spacer(modifier = Modifier.height(16.dp))

            // Dica de mercado em destaque
            Surface(
                color = SolNordeste,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "DICA: Os preços em Fortaleza estão 20% maiores. Junte com um vizinho para encher um caminhão!",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
            }
        }
    }
}

/**
 * Sub-tela: Mapa ou lista de contatos dos vizinhos (Em desenvolvimento).
 */
@Composable
private fun AbaVizinhos() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Em breve: Encontre criadores num raio de 50km.", color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}