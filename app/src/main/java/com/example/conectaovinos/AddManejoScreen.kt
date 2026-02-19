package com.example.conectaovinos

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
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManejoScreen(navController: NavController) {
    val animais = rebanhoGlobal.filterIsInstance<Animal>()

    var animalSelecionado by remember { mutableStateOf("") }
    var tipoManejo by remember { mutableStateOf("") }
    var dataManejo by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    var expandedAnimal by remember { mutableStateOf(false) }

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
                expanded = expandedAnimal,
                onExpandedChange = { expandedAnimal = !expandedAnimal }
            ) {
                OutlinedTextField(
                    value = animalSelecionado.ifEmpty { "Selecione o animal..." },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Qual animal?") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAnimal) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TerraBarro,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedAnimal,
                    onDismissRequest = { expandedAnimal = false }
                ) {
                    if (animais.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Nenhum animal no rebanho") },
                            onClick = { expandedAnimal = false }
                        )
                    } else {
                        animais.forEach { animal ->
                            DropdownMenuItem(
                                text = { Text("${animal.nome} (${animal.raca})") },
                                onClick = {
                                    animalSelecionado = animal.nome
                                    expandedAnimal = false
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

            // 4. Observações
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
                    /* adicionar na lista de manejos do animal */
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