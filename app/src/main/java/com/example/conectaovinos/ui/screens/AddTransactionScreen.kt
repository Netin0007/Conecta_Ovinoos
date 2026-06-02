package com.example.conectaovinos.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(app.transacaoRepository, app.anuncioRepository)
    )

    var selectedType by remember { mutableStateOf(TipoTransacao.Despesa) }
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Transação") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Tipo de Transação:", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.selectableGroup()) {
                TipoTransacao.entries.forEach { tipo ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (selectedType == tipo),
                                onClick = { selectedType = tipo },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (selectedType == tipo), onClick = null)
                        Text(
                            text = if (tipo == TipoTransacao.Receita) "Receita" else "Despesa",
                            modifier = Modifier.padding(start = 8.dp),
                            color = if (tipo == TipoTransacao.Receita) Color(0xFF008000) else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição (ex: Compra de ração, Venda de queijo)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria (ex: Insumos, Saúde, Venda de Animal)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val valorDouble = valor.toDoubleOrNull() ?: 0.0
                    if (descricao.isNotBlank() && valorDouble > 0.0) {
                        viewModel.addTransacao(descricao, valorDouble, selectedType, categoria)
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                // O botão só ativa quando os campos obrigatórios estão preenchidos
                enabled = descricao.isNotBlank() && valor.isNotBlank()
            ) {
                Text("SALVAR TRANSAÇÃO")
            }
        }
    }
}