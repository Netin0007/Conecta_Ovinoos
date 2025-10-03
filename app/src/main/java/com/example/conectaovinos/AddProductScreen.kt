package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

enum class ProductType { Animal, Derivado }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController) {

    var selectedType by remember { mutableStateOf(ProductType.Animal) }
    var nome by remember { mutableStateOf("") }
    var custo by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var unidade by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Produto") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Qual o tipo de produto?", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.selectableGroup()) {
                ProductType.values().forEach { productType ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (selectedType == productType),
                                onClick = { selectedType = productType },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedType == productType),
                            onClick = null
                        )
                        Text(text = productType.name, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome ou Nº de Identificação") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = custo,
                onValueChange = { custo = it },
                label = { Text("Custo de Produção (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedType) {
                ProductType.Animal -> {
                    OutlinedTextField(
                        value = raca,
                        onValueChange = { raca = it },
                        label = { Text("Raça") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = dataNascimento,
                        onValueChange = { dataNascimento = it },
                        label = { Text("Data de Nascimento") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                ProductType.Derivado -> {
                    OutlinedTextField(
                        value = unidade,
                        onValueChange = { unidade = it },
                        label = { Text("Unidade de Medida (Kg, Litro, Peça)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SALVAR PRODUTO")
            }
        }
    }
}