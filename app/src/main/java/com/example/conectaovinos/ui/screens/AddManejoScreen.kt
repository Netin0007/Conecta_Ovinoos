package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel

// IMPORTANTE: Adicione este import do Hilt
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManejoScreen(
    navController: NavController,
    // --- O HILT INJETA O VIEWMODEL AUTOMATICAMENTE AQUI ---
    viewModel: InventoryViewModel = hiltViewModel()
) {
    // As linhas antigas do LocalContext e da Factory foram removidas!

    // Observa o estoque e filtra apenas os itens que são Lotes Vivos
    val produtos by viewModel.produtos.collectAsState()
    val lotes = produtos.filterIsInstance<AnimalLote>()

    var loteSelecionado by remember { mutableStateOf("") }
    var tipoManejo by remember { mutableStateOf("") }
    var dataManejo by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    var expandedLote by remember { mutableStateOf(false) }

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
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "DADOS DO MANEJO",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expandedLote,
                onExpandedChange = { expandedLote = !expandedLote }
            ) {
                OutlinedTextField(
                    value = loteSelecionado.ifEmpty { "Selecione o lote..." },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Qual Lote?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLote) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TerraBarro,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedLote,
                    onDismissRequest = { expandedLote = false }
                ) {
                    if (lotes.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nenhum lote registrado no estoque") },
                            onClick = { expandedLote = false }
                        )
                    } else {
                        lotes.forEach { lote ->
                            DropdownMenuItem(
                                text = { Text(lote.nomeAmigavel) },
                                onClick = {
                                    loteSelecionado = lote.nomeAmigavel
                                    expandedLote = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tipoManejo,
                onValueChange = { tipoManejo = it },
                label = { Text("Tipo (Ex: Vacina, Vermífugo, Pesagem)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dataManejo,
                onValueChange = { dataManejo = it },
                label = { Text("Data do Manejo") },
                placeholder = { Text("DD/MM/AAAA") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = observacoes,
                onValueChange = { observacoes = it },
                label = { Text("Observações (Opcional)") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    /* Futuramente: Salvar no banco de dados de Manejos */
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SolNordeste,
                    contentColor = TextoPrincipal
                ),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("REGISTRAR NO HISTÓRICO", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}