package com.example.conectaovinos.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ui.viewmodels.CadastroEvent
import com.example.conectaovinos.ui.viewmodels.CadastroUiState
import com.example.conectaovinos.ui.viewmodels.CadastroViewModel
import com.example.conectaovinos.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    produtoId: String? = null,
    viewModel: CadastroViewModel = viewModel() // Usando o nosso novo ViewModel!
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Launcher da Galeria de Fotos
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onEvent(CadastroEvent.SetPhoto(uri))
    }

    // Ouvintes de Sucesso e Erro (Disparam a Snackbar ou fecham a tela)
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CadastroEvent.ClearError)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar("Registro salvo com sucesso!")
            navController.navigateUp()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Produto", fontWeight = FontWeight.Black) },
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
        },
        bottomBar = {
            BottomSubmitButton(isLoading = uiState.isLoading) {
                viewModel.onEvent(CadastroEvent.Submit)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. SELETOR ANIMAL OU DERIVADO
            item {
                ModoSelector(
                    isAnimalMode = uiState.isAnimalMode,
                    onModeSelected = { viewModel.onEvent(CadastroEvent.SetModo(it)) }
                )
            }

            // 2. FOTO OBRIGATÓRIA
            item {
                PhotoPickerCard(
                    photoUri = uiState.photoUri,
                    onClick = { photoPickerLauncher.launch("image/*") }
                )
            }

            // 3. FORMULÁRIO DINÂMICO
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (uiState.isAnimalMode) {
                            AnimalForm(uiState)
                        } else {
                            DerivadoForm(uiState, viewModel)
                        }
                    }
                }
            }

            // Espaço extra para não ficar colado no botão inferior
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// --- COMPONENTES VISUAIS ---

@Composable
fun ModoSelector(isAnimalMode: Boolean, onModeSelected: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onModeSelected(true) },
            modifier = Modifier.weight(1f).height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAnimalMode) SolNordeste else Color.White,
                contentColor = if (isAnimalMode) TextoPrincipal else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(if (isAnimalMode) 4.dp else 0.dp)
        ) {
            Text("🐑 ANIMAL", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = { onModeSelected(false) },
            modifier = Modifier.weight(1f).height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isAnimalMode) SolNordeste else Color.White,
                contentColor = if (!isAnimalMode) TextoPrincipal else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(if (!isAnimalMode) 4.dp else 0.dp)
        ) {
            Text("🧀 DERIVADO", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PhotoPickerCard(photoUri: Uri?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2DED4))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (photoUri != null) {
                // Aqui no futuro você pode colocar a biblioteca Coil: AsyncImage(model = photoUri)
                Icon(Icons.Filled.AddAPhoto, contentDescription = null, modifier = Modifier.size(64.dp), tint = VerdeCaatinga)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Foto Selecionada!", color = VerdeCaatinga, fontWeight = FontWeight.Bold)
            } else {
                Icon(Icons.Filled.AddAPhoto, contentDescription = null, modifier = Modifier.size(48.dp), tint = TerraBarro)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Toque para adicionar foto *", color = TerraBarro, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AnimalForm(uiState: CadastroUiState) {
    // Variáveis locais para digitar os textos (serão passadas pro VM no botão salvar no futuro)
    var nome by remember { mutableStateOf("") }
    var brinco by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var isVacinado by remember { mutableStateOf(false) }

    Text("Informações Básicas", fontWeight = FontWeight.Bold, color = TerraBarro, fontSize = 18.sp)

    CustomDropdownMenu(label = "Espécie", options = uiState.especies, selectedValue = "", onValueChange = {})

    OutlinedTextField(value = brinco, onValueChange = { brinco = it }, label = { Text("Número do brinco") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
    OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome (Opcional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomDropdownMenu(label = "Sexo", options = listOf("Macho", "Fêmea"), selectedValue = "", onValueChange = {}, modifier = Modifier.weight(1f))
        OutlinedTextField(value = raca, onValueChange = { raca = it }, label = { Text("Raça") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
    }

    Text("Dados Físicos e Saúde", fontWeight = FontWeight.Bold, color = TerraBarro, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))

    OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso (kg)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isVacinado, onCheckedChange = { isVacinado = it }, colors = CheckboxDefaults.colors(checkedColor = VerdeCaatinga))
        Text("Animal Vacinado")
    }
}

@Composable
fun DerivadoForm(uiState: CadastroUiState, viewModel: CadastroViewModel) {
    var codigoLote by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    Text("Identificação do Lote", fontWeight = FontWeight.Bold, color = TerraBarro, fontSize = 18.sp)

    OutlinedTextField(value = codigoLote, onValueChange = { codigoLote = it }, label = { Text("Código do Lote") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))

    // Dropdown que dispara a regra de negócio da Unidade de Medida
    CustomDropdownMenu(
        label = "Tipo de Derivado",
        options = uiState.tiposDerivado,
        selectedValue = "",
        onValueChange = { viewModel.onEvent(CadastroEvent.OnProductTypeChanged(it)) }
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = quantidade, onValueChange = { quantidade = it }, label = { Text("Quantidade") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))

        // Exibe a unidade sugerida automaticamente
        OutlinedTextField(value = uiState.suggestedUnit, onValueChange = {}, label = { Text("Unidade") }, readOnly = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
    }

    Text("Dados Comerciais", fontWeight = FontWeight.Bold, color = TerraBarro, fontSize = 18.sp, modifier = Modifier.padding(top = 8.dp))
    OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Valor de Venda (R$)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(label: String, options: List<String>, selectedValue: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(selectedValue) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        text = selectionOption
                        onValueChange(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun BottomSubmitButton(isLoading: Boolean, onClick: () -> Unit) {
    Surface(shadowElevation = 16.dp, color = CinzaAreia) {
        Button(
            onClick = onClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeCaatinga)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("SALVAR REGISTRO", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}