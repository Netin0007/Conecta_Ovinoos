package com.example.conectaovinos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext // Injeção do João
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Injeção do João
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp // Injeção do João
import com.example.conectaovinos.ui.components.CategorySelectionCard
import com.example.conectaovinos.ui.components.FormSectionTitle
import com.example.conectaovinos.ui.components.SertaoTextField
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel // Injeção do João

// O Enum mantém a nossa UX com Ícones Nativos e Elegantes
enum class CategoriaProduto(val titulo: String, val icone: ImageVector, val descricao: String) {
    ANIMAL_VIVO("Animal Vivo", Icons.Rounded.Pets, "Lote de ovelhas, cordeiros ou reprodutores"),
    ANIMAL_ABATIDO("Animal Abatido", Icons.Rounded.Scale, "Carcaça inteira limpa pronta para venda"),
    MANTA("Manta de Carneiro", Icons.Rounded.SetMeal, "Manta tradicional aberta (fresca ou salgada)"),
    CORTES("Peças de Corte", Icons.Rounded.Restaurant, "Pernil, paleta, costela, carré, etc."),
    DERIVADO("Derivados", Icons.Rounded.Inventory2, "Queijo, leite, couro, etc.")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController) {
    // --- O MOTOR DO JOÃO (BACKEND VIEWMODEL) ---
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )

    // --- A NOSSA UX PREMIUM (FRONTEND) ---
    var categoriaSelecionada by remember { mutableStateOf<CategoriaProduto?>(null) }
    var quantidade by remember { mutableStateOf("") }
    var custoTotal by remember { mutableStateOf("") }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("REGISTRAR ESTOQUE", fontWeight = FontWeight.Black) },
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
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormSectionTitle("1. O QUE VOCÊ VAI GUARDAR?")

            // Os cartões continuam renderizando a nossa interface fluida
            CategoriaProduto.entries.forEach { categoria ->
                CategorySelectionCard(
                    titulo = categoria.titulo,
                    icone = categoria.icone,
                    descricao = categoria.descricao,
                    isSelected = categoriaSelecionada == categoria,
                    onClick = { categoriaSelecionada = categoria }
                )
            }

            AnimatedVisibility(
                visible = categoriaSelecionada != null,
                enter = fadeIn() + expandVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    FormSectionTitle("2. DETALHES DO LOTE")

                    Spacer(modifier = Modifier.height(8.dp))

                    SertaoTextField(
                        value = quantidade,
                        onValueChange = { quantidade = it },
                        label = "Quantidade (Cabeças / Peças / Kg)",
                        keyboardType = KeyboardType.Number,
                        helperText = "Quantos itens iguais a este você tem?"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SertaoTextField(
                        value = custoTotal,
                        onValueChange = { custoTotal = it },
                        label = "Custo Total do Lote (R$)",
                        keyboardType = KeyboardType.Number,
                        helperText = "Quanto você gastou com tudo isso junto?"
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val custoDouble = custoTotal.toDoubleOrNull() ?: 0.0
                            val qtdInt = quantidade.toIntOrNull() ?: 1
                            val nomeAmigavel = "${categoriaSelecionada?.titulo} ($qtdInt un.)"

                            // --- A COSTURA DOS DOIS MUNDOS ---
                            // Enviamos o input elegante da UI direto para as funções sólidas do Back-end
                            if (categoriaSelecionada == CategoriaProduto.ANIMAL_VIVO) {
                                viewModel.addAnimal(
                                    nome = nomeAmigavel,
                                    raca = "Lote Misto",
                                    dataNascimento = "N/A",
                                    custo = custoDouble
                                )
                            } else {
                                viewModel.addProdutoDerivado(
                                    nome = nomeAmigavel,
                                    unidadeDeMedida = "Un/Kg",
                                    custo = custoDouble
                                )
                            }

                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SolNordeste,
                            contentColor = TextoPrincipal,
                            disabledContainerColor = Color.LightGray
                        ),
                        enabled = quantidade.isNotBlank() && custoTotal.isNotBlank()
                    ) {
                        Text(
                            "SALVAR NO INVENTÁRIO",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}