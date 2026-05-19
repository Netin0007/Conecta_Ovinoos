package com.example.conectaovinos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Scale
import androidx.compose.material.icons.rounded.SetMeal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.ui.components.CategorySelectionCard
import com.example.conectaovinos.ui.components.SertaoTextField
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel

// ATUALIZADO: Nomes mais comerciais focados na fazenda inteira
enum class CategoriaProduto(val titulo: String, val icone: ImageVector, val descricao: String) {
    ANIMAL_VIVO("Lote de Animais", Icons.Rounded.Pets, "Gado, Ovinos, Caprinos, etc."),
    CARNE_KG("Peso da Carcaça / KG", Icons.Rounded.Scale, "Carne limpa vendida no peso (Kg)"),
    MANTA("Manta Tradicional", Icons.Rounded.SetMeal, "Manta aberta (fresca ou salgada)"),
    CORTES("Peças de Corte", Icons.Rounded.Restaurant, "Pernil, paleta, costela, etc."),
    DERIVADO("Derivados", Icons.Rounded.Inventory2, "Queijo, leite, couro, etc.")
}

val especiesDisponiveis = listOf("Bovino", "Ovino", "Caprino", "Suíno", "Outro")
val unidadesDisponiveis = listOf("Kg", "Unidade", "Litro")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, produtoId: String? = null) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: InventoryViewModel = viewModel(factory = InventoryViewModel.Factory(app.rebanhoRepository))

    val produtos by viewModel.produtos.collectAsState()
    val isEditMode = produtoId != null
    val produtoEmEdicao = if (isEditMode) produtos.find { it.id == produtoId } else null

    var categoriaSelecionada by remember { mutableStateOf<CategoriaProduto?>(null) }

    // Estados do Formulário Genérico
    var especie by remember { mutableStateOf("Ovino") }
    var tipoProcessado by remember { mutableStateOf("") }
    var unidadeMedida by remember { mutableStateOf("Kg") }
    var quantidade by remember { mutableStateOf("") }
    var custoTotal by remember { mutableStateOf("") }

    // Efeito para preencher no modo edição
    LaunchedEffect(produtoEmEdicao) {
        when (produtoEmEdicao) {
            is AnimalLote -> {
                categoriaSelecionada = CategoriaProduto.ANIMAL_VIVO
                especie = produtoEmEdicao.especie
                quantidade = produtoEmEdicao.quantidade.toString()
                custoTotal = produtoEmEdicao.custoTotal.toString()
            }
            is ProdutoProcessado -> {
                // Apenas um atalho: se for processo, marca como derivado para abrir o form certo
                categoriaSelecionada = CategoriaProduto.CARNE_KG
                tipoProcessado = produtoEmEdicao.tipoProduto
                unidadeMedida = produtoEmEdicao.unidadeMedida
                quantidade = produtoEmEdicao.quantidade.toString()
                custoTotal = produtoEmEdicao.custoTotal.toString()
            }
            null -> {}
        }
    }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "EDITAR REGISTRO" else "REGISTRAR ESTOQUE", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TerraBarro, titleContentColor = Color.White, navigationIconContentColor = Color.White),
                navigationIcon = { IconButton(onClick = { navController.navigateUp() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (!isEditMode) {
                Text("1. O QUE VOCÊ VAI GUARDAR?", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 14.sp)
                CategoriaProduto.entries.forEach { categoria ->
                    CategorySelectionCard(
                        titulo = categoria.titulo,
                        icone = categoria.icone,
                        descricao = categoria.descricao,
                        isSelected = categoriaSelecionada == categoria,
                        onClick = {
                            categoriaSelecionada = categoria
                            // Auto-preenche o nome se for carne, manta ou corte
                            if (categoria != CategoriaProduto.ANIMAL_VIVO && categoria != CategoriaProduto.DERIVADO) {
                                tipoProcessado = categoria.titulo
                            } else {
                                tipoProcessado = ""
                            }
                        }
                    )
                }
            }

            AnimatedVisibility(visible = categoriaSelecionada != null, enter = fadeIn() + expandVertically()) {
                // Coloquei o formulário dentro de um Card branco para resolver o visual "apagado"
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(if (isEditMode) "DADOS DO REGISTRO" else "2. DETALHES DO LOTE", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 14.sp)

                        if (categoriaSelecionada == CategoriaProduto.ANIMAL_VIVO) {
                            // --- FORMULÁRIO: ANIMAL VIVO ---
                            Text("Espécie do Animal", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(especiesDisponiveis) { e ->
                                    FilterChip(
                                        selected = especie == e,
                                        onClick = { especie = e },
                                        label = { Text(e) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SolNordeste)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    SertaoTextField(
                                        value = quantidade,
                                        onValueChange = { quantidade = it },
                                        label = "Qtd. (Cabeças)",
                                        keyboardType = KeyboardType.Number
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    SertaoTextField(
                                        value = custoTotal,
                                        onValueChange = { custoTotal = it },
                                        label = "Custo Total (R$)",
                                        keyboardType = KeyboardType.Number
                                    )
                                }
                            }
                        } else {
                            // --- FORMULÁRIO: CARNE / DERIVADOS ---
                            SertaoTextField(
                                value = tipoProcessado,
                                onValueChange = { tipoProcessado = it },
                                label = "Nome / Detalhe do Produto",
                                helperText = "Ex: Carcaça de Ovino, Queijo Curado"
                            )

                            Text("Unidade de Medida", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(unidadesDisponiveis) { u ->
                                    FilterChip(
                                        selected = unidadeMedida == u,
                                        onClick = { unidadeMedida = u },
                                        label = { Text(u) },
                                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SolNordeste)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    SertaoTextField(
                                        value = quantidade,
                                        onValueChange = { quantidade = it },
                                        label = "Quantidade",
                                        keyboardType = KeyboardType.Number
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    SertaoTextField(
                                        value = custoTotal,
                                        onValueChange = { custoTotal = it },
                                        label = "Custo Total (R$)",
                                        keyboardType = KeyboardType.Number
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                val custoDouble = custoTotal.toDoubleOrNull() ?: 0.0
                                val qtdDouble = quantidade.toDoubleOrNull() ?: 0.0
                                val qtdInt = quantidade.toIntOrNull() ?: 0

                                if (isEditMode && produtoEmEdicao is AnimalLote) {
                                    viewModel.updateAnimalLote(produtoEmEdicao.copy(especie = especie, quantidade = qtdInt, custoTotal = custoDouble))
                                } else if (!isEditMode) {
                                    if (categoriaSelecionada == CategoriaProduto.ANIMAL_VIVO) {
                                        viewModel.addAnimalLote(especie, qtdInt, custoDouble)
                                    } else {
                                        viewModel.addProdutoProcessado(tipoProcessado, unidadeMedida, qtdDouble, custoDouble)
                                    }
                                }
                                navController.navigateUp()
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (isEditMode) VerdeCaatinga else SolNordeste, contentColor = if (isEditMode) Color.White else TextoPrincipal),
                            enabled = custoTotal.isNotBlank() && quantidade.isNotBlank()
                        ) {
                            Text(if (isEditMode) "ATUALIZAR DADOS" else "SALVAR NO INVENTÁRIO", fontWeight = FontWeight.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}