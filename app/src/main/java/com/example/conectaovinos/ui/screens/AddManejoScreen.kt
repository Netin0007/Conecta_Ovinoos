package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.AnimalModel
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManejoScreen(
    navController: NavController,
    viewModel: InventoryViewModel = viewModel() // Instanciação moderna, sem Factory!
) {
    // Pegamos o estado novo que criamos no passo anterior
    val uiState by viewModel.uiState.collectAsState()
    val animais = uiState.animais

    // Estados do Formulário
    var animalSelecionado by remember { mutableStateOf<AnimalModel?>(null) }
    var tipoManejo by remember { mutableStateOf("") }
    var dataManejo by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    // Controle dos Dropdowns
    var expandirAnimais by remember { mutableStateOf(false) }
    var expandirTipos by remember { mutableStateOf(false) }

    val tiposDeManejo = listOf("Vacinação", "Vermifugação", "Pesagem", "Tratamento Médico", "Outro")

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("REGISTRAR MANEJO", fontWeight = FontWeight.Black) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // CARD DO FORMULÁRIO
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Detalhes do Manejo", fontWeight = FontWeight.Bold, color = TerraBarro, fontSize = 18.sp)

                    // 1. SELECIONAR ANIMAL (Substitui o antigo 'lotes.forEach')
                    ExposedDropdownMenuBox(
                        expanded = expandirAnimais,
                        onExpandedChange = { expandirAnimais = !expandirAnimais }
                    ) {
                        OutlinedTextField(
                            value = animalSelecionado?.let { "${it.animalType} - ${it.name.ifEmpty { it.earTag }}" } ?: "Selecione o Animal",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Animal Alvo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirAnimais) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandirAnimais,
                            onDismissRequest = { expandirAnimais = false }
                        ) {
                            if (animais.isEmpty()) {
                                DropdownMenuItem(text = { Text("Nenhum animal no estoque") }, onClick = { expandirAnimais = false })
                            } else {
                                animais.forEach { animal ->
                                    DropdownMenuItem(
                                        text = { Text("${animal.animalType} - ${animal.name.ifEmpty { "Brinco: " + animal.earTag }}") },
                                        onClick = {
                                            animalSelecionado = animal
                                            expandirAnimais = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 2. TIPO DE MANEJO
                    ExposedDropdownMenuBox(
                        expanded = expandirTipos,
                        onExpandedChange = { expandirTipos = !expandirTipos }
                    ) {
                        OutlinedTextField(
                            value = tipoManejo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Manejo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandirTipos) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandirTipos,
                            onDismissRequest = { expandirTipos = false }
                        ) {
                            tiposDeManejo.forEach { tipo ->
                                DropdownMenuItem(
                                    text = { Text(tipo) },
                                    onClick = {
                                        tipoManejo = tipo
                                        expandirTipos = false
                                    }
                                )
                            }
                        }
                    }

                    // 3. DATA DO MANEJO
                    OutlinedTextField(
                        value = dataManejo,
                        onValueChange = { dataManejo = it },
                        label = { Text("Data (DD/MM/AAAA)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // 4. OBSERVAÇÕES
                    OutlinedTextField(
                        value = observacoes,
                        onValueChange = { observacoes = it },
                        label = { Text("Observações (Ex: Nome da vacina, dosagem)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÃO SALVAR
            Button(
                onClick = {
                    // Aqui você chamará o ViewModel para salvar o manejo no futuro
                    // viewModel.salvarManejo(animalSelecionado, tipoManejo, dataManejo, observacoes)
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeCaatinga),
                shape = RoundedCornerShape(16.dp),
                enabled = animalSelecionado != null && tipoManejo.isNotBlank() && dataManejo.isNotBlank()
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SALVAR MANEJO", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}