package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.models.Anuncio
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.*

/**
 * Tela de Gestão de Anúncios do Produtor (MyAdsScreen).
 * @author Equipe ConectaFazenda
 * @description Permite que o produtor pause, reative ou exclua itens que estão no Marketplace.
 * Atualizado para suportar a arquitetura genérica de Produtos (Lotes e Derivados).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(
    navController: NavController,
    onSwitchToBuyer: () -> Unit = {}
) {
    // --- INJEÇÃO DE DEPENDÊNCIAS ---
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val anuncioViewModel: AnuncioViewModel = viewModel(
        factory = AnuncioViewModel.Factory(app.anuncioRepository)
    )
    val inventoryViewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )

    // --- OBSERVAÇÃO REATIVA DE ESTADOS ---
    val todosAnuncios by anuncioViewModel.todosAnuncios.collectAsState()
    val todosProdutos by inventoryViewModel.produtos.collectAsState()

    // Controla o bottom sheet de seleção do que vender
    var mostrarPicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("MEUS ANÚNCIOS", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                ),
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
            ExtendedFloatingActionButton(
                onClick = { mostrarPicker = true },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Anunciar Lote/Produto", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    ) { innerPadding ->
        if (todosAnuncios.isEmpty()) {
            // --- EMPTY STATE (NENHUM ANÚNCIO) ---
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(Icons.Rounded.Storefront, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Nenhum anúncio ainda",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = TextoPrincipal
                    )
                    Text(
                        "Toque em \"Anunciar\" para colocar um lote ou derivado à venda na feira livre.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            // --- LISTA DE ANÚNCIOS ATIVOS/PAUSADOS ---
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(todosAnuncios, key = { it.id }) { anuncio ->
                    AnuncioCard(
                        anuncio = anuncio,
                        onPausar = { anuncioViewModel.pausarAnuncio(anuncio.id) },
                        onReativar = { anuncioViewModel.reativarAnuncio(anuncio.id) },
                        onDeletar = { anuncioViewModel.deletarAnuncio(anuncio.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    // --- BOTTOM SHEET INTELIGENTE DE SELEÇÃO ---
    if (mostrarPicker) {
        ProdutoPickerSheet(
            produtos = todosProdutos,
            anunciosAtivos = todosAnuncios.filter { it.ativo },
            onProdutoSelecionado = { produto ->
                mostrarPicker = false
                navController.navigate("create_ad/${produto.id}")
            },
            onDismiss = { mostrarPicker = false }
        )
    }
}

/**
 * Componente de painel inferior (Bottom Sheet) para escolher qual item do estoque vai para a feira.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutoPickerSheet(
    produtos: List<Produto>,
    anunciosAtivos: List<Anuncio>,
    onProdutoSelecionado: (Produto) -> Unit,
    onDismiss: () -> Unit
) {
    val idsJaAnunciados = anunciosAtivos.map { it.animalId }.toSet()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                "O que você quer vender?",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = TextoPrincipal,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            if (produtos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Seu inventário está vazio. Registre algo no Estoque primeiro.",
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn {
                    items(produtos, key = { it.id }) { produto ->
                        val jaAnunciado = produto.id in idsJaAnunciados

                        // Emoji Dinâmico
                        val emoji = when (produto) {
                            is AnimalLote -> when (produto.especie.lowercase()) {
                                "bovino" -> "🐄"
                                "caprino" -> "🐐"
                                "suíno", "suino" -> "🐖"
                                else -> "🐑"
                            }
                            is ProdutoProcessado -> if (produto.tipoProduto.lowercase().contains("carne")) "🥩" else "🧀"
                        }

                        val subtitulo = when (produto) {
                            is AnimalLote -> produto.especie
                            is ProdutoProcessado -> produto.tipoProduto
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !jaAnunciado) {
                                    onProdutoSelecionado(produto)
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(CinzaAreia, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emoji, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    produto.nomeAmigavel.uppercase(),
                                    fontWeight = FontWeight.Black,
                                    color = if (jaAnunciado) Color.Gray else TextoPrincipal,
                                    fontSize = 16.sp
                                )
                                Text(subtitulo, color = Color.Gray, fontSize = 13.sp)
                            }
                            if (jaAnunciado) {
                                Surface(
                                    color = VerdeCaatinga.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "JÁ ANUNCIADO",
                                        color = VerdeCaatinga,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        HorizontalDivider(color = CinzaAreia)
                    }
                }
            }
        }
    }
}

/**
 * Card exibido na lista de gestão do produtor.
 */
@Composable
fun AnuncioCard(
    anuncio: Anuncio,
    onPausar: () -> Unit,
    onReativar: () -> Unit,
    onDeletar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Cabeçalho com nome e badge de status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (anuncio.ativo) Color(0xFFEFEBE1) else Color(0xFFF5F5F5))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    anuncio.nomeAnimal.uppercase(),
                    fontWeight = FontWeight.Black,
                    color = if (anuncio.ativo) TextoPrincipal else Color.Gray
                )
                Surface(
                    color = if (anuncio.ativo) VerdeCaatinga else Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (anuncio.ativo) "ATIVO" else "PAUSADO",
                        color = if (anuncio.ativo) Color.White else Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Corpo com preço e dados do banco
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(CinzaAreia, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Aqui mantemos um ícone genérico de loja
                    Icon(imageVector = Icons.Rounded.Storefront, contentDescription = "Item", tint = TerraBarro)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(anuncio.racaAnimal, fontSize = 12.sp, color = Color.Gray)
                    Text(
                        NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                            .format(anuncio.precoVenda),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = SolNordeste
                    )
                    Text(
                        "Custo: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(anuncio.custoAnimal)}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            // Botões de ação dinâmicos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (anuncio.ativo) {
                    OutlinedButton(
                        onClick = onPausar,
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TerraBarro),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TerraBarro),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("PAUSAR", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                } else {
                    Button(
                        onClick = onReativar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeCaatinga, contentColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("REATIVAR", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
                OutlinedButton(
                    onClick = onDeletar,
                    modifier = Modifier.weight(1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("EXCLUIR", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}