package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.ui.components.FormSectionTitle
import com.example.conectaovinos.ui.components.SelectableChip
import com.example.conectaovinos.ui.components.SertaoTextField
import com.example.conectaovinos.ui.components.TypeSelectionCard
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel

val racasComuns = listOf("Santa Inês", "Dorper", "Morada Nova", "Somalis", "SRD", "Boer")
val unidadesComuns = listOf("Kg", "Litro", "Garrafa", "Peça", "Duzia")

enum class ProductType { Animal, Derivado }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )

    var selectedType by remember { mutableStateOf(ProductType.Animal) }
    var nome by remember { mutableStateOf("") }
    var custo by remember { mutableStateOf("") }
    var racaSelecionada by remember { mutableStateOf("") }
    var unidadeSelecionada by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("NOVO REGISTRO", fontWeight = FontWeight.Black) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TypeSelectionCard(
                    title = "ANIMAL",
                    icon = "🐑",
                    isSelected = selectedType == ProductType.Animal,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedType = ProductType.Animal }
                )
                TypeSelectionCard(
                    title = "PRODUTO",
                    icon = "🧀",
                    isSelected = selectedType == ProductType.Derivado,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedType = ProductType.Derivado }
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(2.dp, if (nome.isEmpty()) Color.LightGray else VerdeCaatinga, RoundedCornerShape(12.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Foto", tint = TerraBarro, modifier = Modifier.size(48.dp))
                        Text("Toque para adicionar foto", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                FormSectionTitle("INFORMAÇÕES BÁSICAS")
                SertaoTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = if (selectedType == ProductType.Animal) "Nome ou Nº do Brinco" else "Nome do Produto"
                )

                Spacer(modifier = Modifier.height(16.dp))

                SertaoTextField(
                    value = custo,
                    onValueChange = { custo = it },
                    label = "Custo de Produção (R$)",
                    keyboardType = KeyboardType.Number,
                    helperText = "Quanto gastou para produzir/criar este item?"
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedType == ProductType.Animal) {
                    FormSectionTitle("RAÇA (Selecione)")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(count = racasComuns.size) { index ->
                            val raca = racasComuns[index]
                            SelectableChip(raca, raca == racaSelecionada) { racaSelecionada = raca }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SertaoTextField(
                        value = dataNascimento,
                        onValueChange = { dataNascimento = it },
                        label = "Nascimento / Aquisição",
                        keyboardType = KeyboardType.Number,
                        placeholder = "DD/MM/AAAA"
                    )
                } else {
                    FormSectionTitle("UNIDADE DE VENDA")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(count = unidadesComuns.size) { index ->
                            val unidade = unidadesComuns[index]
                            SelectableChip(unidade, unidade == unidadeSelecionada) { unidadeSelecionada = unidade }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val custoDouble = custo.toDoubleOrNull() ?: 0.0
                    if (selectedType == ProductType.Animal) {
                        viewModel.addAnimal(nome, racaSelecionada, dataNascimento, custoDouble)
                    } else {
                        viewModel.addProdutoDerivado(nome, unidadeSelecionada, custoDouble)
                    }
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                enabled = nome.isNotBlank() && custo.isNotBlank()
            ) {
                Text("SALVAR NO INVENTÁRIO", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}