package com.example.conectaovinos.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.ui.viewmodels.CadastroEvent
import com.example.conectaovinos.ui.viewmodels.CadastroUiState
import com.example.conectaovinos.ui.viewmodels.CadastroViewModel
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.components.PhotoCarouselPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    produtoId: String? = null,
    viewModel: CadastroViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    // Launcher da Galeria de Fotos
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            viewModel.onEvent(CadastroEvent.AddPhoto(uri))
        }
    }

    // Ouvintes de Sucesso e Erro (Disparam a Snackbar ou fecham a tela)
    LaunchedEffect(produtoId) {
        if (produtoId != null && !uiState.isEditMode) {
            viewModel.onEvent(CadastroEvent.LoadProduto(produtoId))
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CadastroEvent.ClearError)
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigateUp()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("CADASTRO DE PRODUTO", fontWeight = FontWeight.Black) },
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
            if (!uiState.isEditMode) {
                item {
                    ModoSelector(
                        isAnimalMode = uiState.isAnimalMode,
                        onModeSelected = { viewModel.onEvent(CadastroEvent.SetModo(it)) }
                    )
                }
            }

            // 2. FOTOS
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Fotos do Animal", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 16.sp)
                    PhotoCarouselPicker(
                        photoUris = uiState.photoUris,
                        onAddClick = { photoPickerLauncher.launch("image/*") },
                        onRemoveClick = { viewModel.onEvent(CadastroEvent.RemovePhoto(it)) }
                    )
                }
            }

            // 2. LOCALIZAÇÃO
            item {
                LocationPickerCard(
                    endereco = uiState.endereco,
                    onAddressChange = { viewModel.onEvent(CadastroEvent.OnAddressChanged(it)) }
                )
            }

            // 3. FORMULÁRIO
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (uiState.isAnimalMode) {
                            AnimalForm(uiState, viewModel)
                        } else {
                            DerivadoForm(uiState, viewModel)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun LocationPickerCard(endereco: String, onAddressChange: (String) -> Unit) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TerraBarro)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Localização da Propriedade", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 16.sp)
            }
            
            OutlinedTextField(
                value = endereco,
                onValueChange = onAddressChange,
                label = { Text("Endereço Completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Ex: Tauá, CE - Fazenda Boa Vista") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
    }
}

@Composable
fun AnimalForm(uiState: CadastroUiState, viewModel: CadastroViewModel) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    Text("Informações do Animal", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 16.sp)

    CustomDropdownMenu(
        label = "Espécie",
        options = uiState.especies,
        selectedValue = uiState.especie,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnEspecieChanged(it)) }
    )

    OutlinedTextField(
        value = uiState.brinco,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnBrincoChanged(it)) },
        label = { Text("Número do brinco") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = androidx.compose.ui.text.input.ImeAction.Next),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )

    OutlinedTextField(
        value = uiState.nome,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnNomeChanged(it)) },
        label = { Text("Nome / Apelido") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CustomDropdownMenu(
            label = "Sexo",
            options = listOf("Macho", "Fêmea"),
            selectedValue = uiState.sexo,
            onValueChange = { viewModel.onEvent(CadastroEvent.OnSexoChanged(it)) },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = uiState.raca,
            onValueChange = { viewModel.onEvent(CadastroEvent.OnRacaChanged(it)) },
            label = { Text("Raça") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
        )
    }

    OutlinedTextField(
        value = uiState.peso,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnPesoChanged(it)) },
        label = { Text("Peso (kg)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = androidx.compose.ui.text.input.ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = uiState.isVacinado,
            onCheckedChange = { viewModel.onEvent(CadastroEvent.OnVacinadoChanged(it)) },
            colors = CheckboxDefaults.colors(checkedColor = VerdeCaatinga)
        )
        Text("Animal Vacinado", fontWeight = FontWeight.Bold)
    }
}

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
fun DerivadoForm(uiState: CadastroUiState, viewModel: CadastroViewModel) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    Text("Identificação do Lote", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 16.sp)

    OutlinedTextField(
        value = uiState.codigoLote,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnCodigoLoteChanged(it)) },
        label = { Text("Código do Lote") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next)
    )

    CustomDropdownMenu(
        label = "Tipo de Derivado",
        options = uiState.tiposDerivado,
        selectedValue = uiState.productType,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnProductTypeChanged(it)) }
    )

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = uiState.quantidade,
            onValueChange = { viewModel.onEvent(CadastroEvent.OnQuantidadeChanged(it)) },
            label = { Text("Quantidade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = androidx.compose.ui.text.input.ImeAction.Next),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = uiState.suggestedUnit,
            onValueChange = {},
            label = { Text("Unidade") },
            readOnly = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        )
    }

    Text("Dados Comerciais", fontWeight = FontWeight.Black, color = TerraBarro, fontSize = 16.sp)
    OutlinedTextField(
        value = uiState.preco,
        onValueChange = { viewModel.onEvent(CadastroEvent.OnPrecoChanged(it)) },
        label = { Text("Valor Estimado (R$)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = androidx.compose.ui.text.input.ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        prefix = { Text("R$ ") },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownMenu(label: String, options: List<String>, selectedValue: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier) {
        OutlinedTextField(
            value = selectedValue,
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
    Surface(shadowElevation = 16.dp, color = Color.White) {
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
                Text("SALVAR NO ESTOQUE", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}
