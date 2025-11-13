package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.models.Transacao
import java.text.NumberFormat
import java.util.*

val dummyTransactionList = listOf(
    Transacao(id = "1", descricao = "Venda da Mococa 01", valor = 750.0, tipo = TipoTransacao.Receita, data = Date(), categoria = "Venda de Animal"),
    Transacao(id = "2", descricao = "Compra de Ração", valor = 320.0, tipo = TipoTransacao.Despesa, data = Date(), categoria = "Insumos"),
    Transacao(id = "3", descricao = "Venda de 5 Queijos", valor = 125.0, tipo = TipoTransacao.Receita, data = Date(), categoria = "Venda de Derivado"),
    Transacao(id = "4", descricao = "Medicamentos (Vermífugo)", valor = 80.0, tipo = TipoTransacao.Despesa, data = Date(), categoria = "Saúde")
)

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(navController: NavController) {
    val totalReceitas = dummyTransactionList.filter { it.tipo == TipoTransacao.Receita }.sumOf { it.valor }
    val totalDespesas = dummyTransactionList.filter { it.tipo == TipoTransacao.Despesa }.sumOf { it.valor }
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
                onClick = {navController.navigate("add_transaction_form")},
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
            FinancialDashboard(totalReceitas, totalDespesas, lucroLiquido)

            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(dummyTransactionList) { transacao ->
                    TransactionListItem(transacao = transacao)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun FinancialDashboard(receitas: Double, despesas: Double, lucro: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Resumo Financeiro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Receitas", style = MaterialTheme.typography.bodyMedium)
                Text(
                    formatCurrency(receitas),
                    color = Color(0xFF008000),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Despesas", style = MaterialTheme.typography.bodyMedium)
                Text(
                    formatCurrency(despesas),
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Lucro Líquido:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                formatCurrency(lucro),
                color = if (lucro >= 0) Color(0xFF008000) else Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun TransactionListItem(transacao: Transacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(transacao.descricao, fontWeight = FontWeight.Bold)
                Text(transacao.categoria, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = if (transacao.tipo == TipoTransacao.Receita) "+ ${formatCurrency(transacao.valor)}" else "- ${formatCurrency(transacao.valor)}",
                color = if (transacao.tipo == TipoTransacao.Receita) Color(0xFF008000) else Color.Red,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}