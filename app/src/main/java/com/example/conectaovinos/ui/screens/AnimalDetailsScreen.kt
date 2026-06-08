package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnimalDetailsViewModel
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tela de Detalhes do Produto/Lote (AnimalDetailsScreen).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailsScreen(
    navController: NavController,
    animalId: String?,
    detailsViewModel: AnimalDetailsViewModel = hiltViewModel(),
    anuncioViewModel: AnuncioViewModel = hiltViewModel()
) {
    // --- OBSERVAÇÃO DE ESTADO ---
    val todosProdutos by detailsViewModel.produtos.collectAsState()
    val todosAnuncios by anuncioViewModel.todosAnuncios.collectAsState()

    // Encontra o Produto (AnimalLote ou ProdutoProcessado) no Repositório Unificado
    val produto = todosProdutos.find { it.id == animalId }
    val jaAnunciado = todosAnuncios.any { it.animalId == animalId && it.ativo }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = {
                    Text("FICHA DE CONTROLE", fontWeight = FontWeight.Black, fontSize = 16.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->

        // --- ESTADOS DE CARREGAMENTO (LOADING / NOT FOUND) ---
        if (todosProdutos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TerraBarro)
            }
            return@Scaffold
        }

        if (produto == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Registro não encontrado no estoque.", color = Color.Gray)
            }
            return@Scaffold
        }

        // --- CONFIGURAÇÃO DINÂMICA DE UI (EMOJIS E BADGES) ---
        val emoji = when (produto) {
            is AnimalLote -> when (produto.especie.lowercase()) {
                "bovino" -> "🐄"
                "caprino" -> "🐐"
                "suíno", "suino" -> "🐖"
                else -> "🐑"
            }
            is ProdutoProcessado -> if (produto.tipoProduto.lowercase().contains("carne")) "🥩" else "🧀"
        }

        val badgeText = when (produto) {
            is AnimalLote -> produto.especie.uppercase()
            is ProdutoProcessado -> "DERIVADO"
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // --- HEADER: FOTO (EMOJI) E TÍTULO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TerraBarro)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 64.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = produto.nomeAmigavel.uppercase(),
                        color = SolNordeste,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Surface(color = VerdeCaatinga, shape = RoundedCornerShape(16.dp)) {
                            Text(
                                text = badgeText,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (jaAnunciado) {
                            Surface(color = SolNordeste, shape = RoundedCornerShape(16.dp)) {
                                Text(
                                    text = "EM VENDA",
                                    color = TextoPrincipal,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // --- CORPO: DADOS FINANCEIROS E TÉCNICOS ---
            Column(
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .padding(horizontal = 16.dp)
            ) {
                // Painel de Precificação (Custo vs Margem de Venda)
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Custo do Lote", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                formatCurrencyDetails(produto.custoTotal),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = VermelhoBarro
                            )
                        }
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Venda Estimada", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                formatCurrencyDetails(produto.custoTotal * 1.5), // Margem de 50%
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = VerdeCaatinga
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "DADOS TÉCNICOS",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Detalhe 1: Data do Registro no Inventário
                    DetailItem(
                        icon = Icons.Default.DateRange,
                        label = "Registrado em",
                        value = formatTimestamp(produto.dataRegistro),
                        modifier = Modifier.weight(1f)
                    )
                    // Detalhe 2: Status Comercial
                    DetailItem(
                        icon = Icons.Default.Info,
                        label = "Status",
                        value = if (jaAnunciado) "Em Venda" else "Em Estoque",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- BOTÕES DE AÇÃO (MARKETPLACE) ---
                if (jaAnunciado) {
                    OutlinedButton(
                        onClick = {
                            val anuncioId = todosAnuncios
                                .firstOrNull { it.animalId == animalId && it.ativo }?.id
                            if (anuncioId != null) {
                                navController.navigate("product_details/$anuncioId")
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, VerdeCaatinga),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VerdeCaatinga),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VER ANÚNCIO ATIVO", fontWeight = FontWeight.Black)
                    }
                } else {
                    Button(
                        onClick = { navController.navigate("create_ad_form/${produto.id}") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(6.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ANUNCIAR PARA VENDA", fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        }
    }
}

private fun formatCurrencyDetails(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}

private fun formatTimestamp(timeInMillis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    return formatter.format(Date(timeInMillis))
}