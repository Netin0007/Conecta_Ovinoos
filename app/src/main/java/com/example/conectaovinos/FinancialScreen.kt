package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.database.entities.TransacaoEntity
import com.example.conectaovinos.viewmodel.TransacaoViewModel
import java.text.NumberFormat
import java.util.*

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(
    navController: NavController,
    transacaoViewModel: TransacaoViewModel
) {

    // 🔹 Pegando transações do banco
    val transacoes by transacaoViewModel.transacoes.collectAsState(initial = emptyList())

    // 🔹 Cálculos reais
    val totalReceitas = transacoes.filter { it.tipo == TipoTransacao.Receita.name }.sumOf { it.valor }
    val totalDespesas = transacoes.filter { it.tipo == TipoTransacao.Despesa.name }.sumOf { it.valor }
    val lucroLiquido = totalReceitas - totalDespesas

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Controle Financeiro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_transaction_form") },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Transação")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

//            FinancialDashboard(totalReceitas, totalDespesas, lucroLiquido)

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(transacoes) { transacao ->
//                    TransactionListItem(transacao)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
