package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.ui.components.FormSectionTitle
import com.example.conectaovinos.ui.components.SertaoTextField
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(
    navController: NavController,
    produtoId: String?,
    inventoryViewModel: InventoryViewModel = hiltViewModel(),
    anuncioViewModel: AnuncioViewModel = hiltViewModel()
) {
    // --- OBSERVAÇÃO DE ESTADO ---
    val todosProdutos by inventoryViewModel.produtos.collectAsState()

    // Busca o produto genérico (pode ser Lote Vivo ou Processado)
    val produto = todosProdutos.find { it.id == produtoId }

    // --- ESTADOS DO FORMULÁRIO ---
    var precoVenda by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("NOVO ANÚNCIO", fontWeight = FontWeight.Black) },
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

        // --- TRATAMENTO DE ESTADOS (LOADING / NOT FOUND) ---
        if (todosProdutos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TerraBarro)
            }
            return@Scaffold
        }

        if (produto == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Item não encontrado no estoque.", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
            return@Scaffold
        }

        // --- LÓGICA DE APRESENTAÇÃO DINÂMICA ---
        val emoji = when (produto) {
            is AnimalLote -> when (produto.especie.lowercase()) {
                "bovino" -> "🐄"
                "caprino" -> "🐐"
                "suíno", "suino" -> "🐖"
                else -> "🐑"
            }
            is ProdutoProcessado -> if (produto.tipoProduto.lowercase().contains("carne")) "🥩" else "🧀"
        }

        val detalheProduto = when (produto) {
            is AnimalLote -> "Espécie: ${produto.especie}"
            is ProdutoProcessado -> "Categoria: ${produto.tipoProduto}"
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CARD DE RESUMO DO PRODUTO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(60.dp).background(CinzaAreia, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 32.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(produto.nomeAmigavel.uppercase(), fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
                        Text(detalheProduto, color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "Custo: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(produto.custoTotal)}",
                            color = TerraBarro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            FormSectionTitle("1. DETALHES DA VENDA")

            // --- LÓGICA DE NEGÓCIO: CÁLCULO DE LUCRO EM TEMPO REAL ---
            val precoDigitado = precoVenda.toDoubleOrNull()
            val helperLucro = if (precoDigitado != null && precoDigitado > produto.custoTotal) {
                val lucro = precoDigitado - produto.custoTotal
                "Lucro estimado: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(lucro)}"
            } else {
                "Qual o valor total que você quer receber por este item?"
            }

            SertaoTextField(
                value = precoVenda,
                onValueChange = { precoVenda = it },
                label = "Preço de Venda (R$)",
                keyboardType = KeyboardType.Number,
                helperText = helperLucro
            )

            Spacer(modifier = Modifier.height(8.dp))

            SertaoTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = "Descrição para os compradores (Opcional)",
                placeholder = "Ex: Lote saudável, vacinado, queijo curado...",
                helperText = "Uma boa descrição vende muito mais rápido!"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- CALL TO ACTION (CTA) E INTEGRAÇÃO COM BACKEND ---
            Button(
                onClick = {
                    val preco = precoVenda.toDoubleOrNull() ?: 0.0
                    if (preco > 0) {
                        anuncioViewModel.publicarAnuncio(produto, preco, descricao)

                        navController.navigate("marketplace") {
                            popUpTo("inventory") { inclusive = false }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeCaatinga,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray
                ),
                enabled = precoVenda.isNotBlank() && (precoVenda.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Icon(Icons.Rounded.Storefront, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("PUBLICAR NA FEIRA", fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            }
        }
    }
}