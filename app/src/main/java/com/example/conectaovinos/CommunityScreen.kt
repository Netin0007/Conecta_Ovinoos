package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(navController: NavController) {
    // Definimos as abas que ele pode deslizar
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
                // A barra de navegação no topo (Tabs)
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
                            onClick = {
                                // Anima o deslizar quando clica na aba
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = {
                                Text(
                                    titulo,
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
        // O Pager é o que permite o movimento de "deslizar o dedo" para o lado
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { paginaAtual ->
            // Mostra o conteúdo dependendo da aba que ele está
            when (paginaAtual) {
                0 -> AbaDicasRapidas()
                1 -> AbaMercado()
                2 -> AbaVizinhos()
            }
        }
    }
}

// --- CONTEÚDO DA ABA 1: DICAS RÁPIDAS (FOCO EM ÁUDIO E TEXTO) ---
@Composable
fun AbaDicasRapidas() {
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

// --- CONTEÚDO DA ABA 2: MERCADO (PREÇOS PARA EVITAR ATRAVESSADOR) ---
@Composable
fun AbaMercado() {
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
            Surface(
                color = SolNordeste,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "DICA: Os preços em Fortaleza estão 20% maiores. Junte com um vizinho para encher um caminhão!",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
            }
        }
    }
}

// --- CONTEÚDO DA ABA 3: VIZINHOS (NETWORKING RURAL) ---
@Composable
fun AbaVizinhos() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Em breve: Encontre criadores num raio de 50km.", color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}

// --- COMPONENTES VISUAIS REUTILIZÁVEIS ---

@Composable
fun CardAudioDica(titulo: String, autor: String, duracao: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botão de Play Gigante
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(VerdeCaatinga)
                    .clickable { /* Toca o áudio */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Ouvir", tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
                Text(autor, color = TerraBarro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Áudio • $duracao", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CardTextoDica(titulo: String, texto: String, autor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
            Spacer(modifier = Modifier.height(8.dp))
            Text(texto, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Por $autor", color = TerraBarro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { /* Compartilha no WhatsApp */ }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartilhar", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun CardCotacao(animal: String, preco: String, tendencia: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(animal.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color.Gray)
                Text(preco, fontWeight = FontWeight.Black, fontSize = 20.sp, color = TextoPrincipal)
            }
            Surface(
                color = if (tendencia.contains("Subiu")) VerdeCaatinga else CinzaAreia,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    tendencia,
                    color = if (tendencia.contains("Subiu")) Color.White else Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}