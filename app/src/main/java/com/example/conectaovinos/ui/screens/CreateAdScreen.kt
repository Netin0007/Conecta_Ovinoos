package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(
    navController: NavController,
    produtoId: String?,
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    // Injeção do AnuncioViewModel com a Factory correta usando o ApplicationContext
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val anuncioViewModel: AnuncioViewModel = viewModel(
        factory = AnuncioViewModel.Factory(app.anuncioRepository)
    )

    val uiState by inventoryViewModel.uiState.collectAsState()

    // O sistema agora procura inteligentemente nas duas listas!
    val animal = uiState.animais.find { it.id == produtoId }
    val derivado = uiState.derivados.find { it.id == produtoId }

    val itemExiste = animal != null || derivado != null

    // Monta o nome dependendo do que ele achou
    val nomeItem = animal?.let { "${it.animalType} ${it.name.ifEmpty { it.earTag }}" }
        ?: derivado?.productType
        ?: ""

    val custoAtual = animal?.salePrice ?: derivado?.salePrice ?: 0.0

    var precoVenda by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    val quickTags = listOf("Saudável", "Vacinado", "Pronta Entrega", "Negociável", "Excelente Genética")

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("CRIAR ANÚNCIO", fontWeight = FontWeight.Black) },
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

        // Tratamento caso o ID tenha sido apagado ou não exista
        if (!itemExiste) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Item não encontrado no estoque.", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
            return@Scaffold
        }

        val precoDigitado = precoVenda.toDoubleOrNull() ?: 0.0
        val lucro = precoDigitado - custoAtual
        val isLucroPositivo = lucro > 0

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // RESUMO DO ITEM
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Item Selecionado:", fontSize = 12.sp, color = Color.Gray)
                    Text(nomeItem.uppercase(), fontWeight = FontWeight.Black, fontSize = 18.sp, color = TextoPrincipal)
                    Text("Custo/Valor Atual: ${formatarMoeda(custoAtual)}", color = VermelhoBarro, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            // DADOS DE VENDA
            OutlinedTextField(
                value = precoVenda,
                onValueChange = { precoVenda = it },
                label = { Text("Por quanto deseja vender?") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("R$ ") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeCaatinga,
                    focusedLabelColor = VerdeCaatinga
                )
            )

            // PAINEL DE LUCRO INTERATIVO
            Surface(
                color = if (isLucroPositivo) VerdeCaatinga.copy(alpha = 0.1f) else CinzaAreia,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lucro Estimado:", fontWeight = FontWeight.Bold, color = if (isLucroPositivo) VerdeCaatinga else Color.Gray)
                    Text(
                        text = if (precoDigitado > 0) formatarMoeda(lucro) else "R$ 0,00",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = if (isLucroPositivo) VerdeCaatinga else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // DESCRIÇÃO E TAGS RÁPIDAS
            Text("Descrição Comercial", fontWeight = FontWeight.Bold, color = TerraBarro)

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quickTags) { tag ->
                    AssistChip(
                        onClick = {
                            descricao = if (descricao.isEmpty()) tag else "$descricao, $tag"
                        },
                        label = { Text(tag) },
                        leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = Color.White)
                    )
                }
            }

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Detalhes para o comprador") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Use as tags acima ou digite mais detalhes...") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÃO DE PUBLICAR
            Button(
                onClick = {
                    if (precoDigitado > 0) {
                        // TODO no futuro: anuncioViewModel.publicarAnuncio(...)
                        navController.navigate("marketplace") {
                            popUpTo("inventory") { inclusive = false }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SolNordeste,
                    contentColor = TextoPrincipal,
                    disabledContainerColor = Color.LightGray
                ),
                enabled = isLucroPositivo
            ) {
                Icon(Icons.Rounded.Storefront, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ENVIAR PARA A FEIRA", fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
            }
        }
    }
}

private fun formatarMoeda(valor: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}