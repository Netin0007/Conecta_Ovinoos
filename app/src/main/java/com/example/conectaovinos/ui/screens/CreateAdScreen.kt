package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.ui.components.FormSectionTitle
import com.example.conectaovinos.ui.components.SertaoTextField
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(navController: NavController, animalId: String?) {
    // --- O CÉREBRO DO JOÃO (BACKEND VIEWMODELS) ---
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val inventoryViewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )
    val anuncioViewModel: AnuncioViewModel = viewModel(
        factory = AnuncioViewModel.Factory(app.anuncioRepository)
    )

    val todosProdutos by inventoryViewModel.produtos.collectAsState()
    val animal = todosProdutos.find { it.id == animalId } as? Animal

    // --- ESTADOS DA NOSSA UI ---
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

        // Tela de Carregamento enquanto o banco responde
        if (todosProdutos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TerraBarro)
            }
            return@Scaffold
        }

        // Proteção contra animal inexistente
        if (animal == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Animal não encontrado no estoque.", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CARD DE RESUMO DO ANIMAL ---
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
                        Icon(Icons.Rounded.Pets, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(animal.nome.uppercase(), fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
                        Text("Raça: ${animal.raca}", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "Custo: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(animal.custo)}",
                            color = TerraBarro, // Ajustado para a paleta de cores existente
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            FormSectionTitle("1. DETALHES DA VENDA")

            // Lógica inteligente de cálculo de lucro injetada no nosso TextField
            val precoDigitado = precoVenda.toDoubleOrNull()
            val helperLucro = if (precoDigitado != null && precoDigitado > animal.custo) {
                val lucro = precoDigitado - animal.custo
                "Lucro estimado: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(lucro)}"
            } else {
                "Qual o valor total que você quer receber pelo lote?"
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
                placeholder = "Ex: Animais saudáveis, vacinados, excelente genética...",
                helperText = "Uma boa descrição vende muito mais rápido!"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- O BOTÃO FINAL COM NAVEGAÇÃO CORRIGIDA ---
            Button(
                onClick = {
                    val preco = precoVenda.toDoubleOrNull() ?: 0.0
                    if (preco > 0) {
                        anuncioViewModel.publicarAnimal(animal, preco, descricao)
                        // A nossa nova arquitetura de rotas
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