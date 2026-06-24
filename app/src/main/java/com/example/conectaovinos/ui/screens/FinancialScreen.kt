package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.models.Transacao
import com.example.conectaovinos.ui.theme.TerraBarro
import com.example.conectaovinos.ui.theme.VerdeCaatinga
import com.example.conectaovinos.ui.theme.CinzaAreia
import com.example.conectaovinos.ui.viewmodels.DashboardViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(app.transacaoRepository)
    )

    val transacoes by viewModel.transacoes.collectAsState()
    val totalReceitas = viewModel.getTotalReceitas(transacoes)
    val totalDespesas = viewModel.getTotalDespesas(transacoes)
    val lucroLiquido = viewModel.getLucroLiquido(transacoes)

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("EXTRATO FINANCEIRO", fontWeight = FontWeight.Black) },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_transaction_form") },
                containerColor = VerdeCaatinga,
                contentColor = Color.White
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

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(transacoes) { transacao ->
                    TransactionListItem(
                        transacao = transacao,
                        onDelete = { viewModel.deletarTransacao(transacao.id) }
                    )
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
fun TransactionListItem(transacao: Transacao, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    transacao.descricao.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    transacao.categoria,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (transacao.tipo == TipoTransacao.Receita) "+ ${formatCurrency(transacao.valor)}" else "- ${formatCurrency(transacao.valor)}",
                    color = if (transacao.tipo == TipoTransacao.Receita) VerdeCaatinga else TerraBarro,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Deletar",
                        tint = Color.Red.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}
