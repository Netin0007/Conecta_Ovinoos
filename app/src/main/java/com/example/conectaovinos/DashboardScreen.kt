package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.database.DatabaseProvider
import com.example.conectaovinos.database.entities.TransacaoEntity
import com.example.conectaovinos.database.enums.tipoTransacao
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.viewmodel.DashboardViewModel
import com.example.conectaovinos.viewmodel.FinancialSummary
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val context = LocalContext.current
    val db = DatabaseProvider.get(context)

    val viewModel: DashboardViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(db.animalDao(), db.transacaoDao()) as T
            }
        }
    )

    val summary = viewModel.financialSummary.collectAsState().value
    val transacoes = viewModel.transacoes.collectAsState().value

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("CENTRAL FINANCEIRA", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_transaction") },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova Transação")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // CARD PRINCIPAL: SALDO ATUAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = VerdeCaatinga),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(VerdeCaatinga, VerdeCaatinga.copy(alpha = 0.8f))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = SolNordeste, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "SALDO GERAL EM CONTA",
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = formatCurrency(summary.saldoGeral),
                            color = SolNordeste,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("ENTRADAS", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(formatCurrency(summary.receitas), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("SAÍDAS TOTAL", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(formatCurrency(summary.despesas), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            // CARDS SECUNDÁRIOS
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardSmallCard(
                    label = "VALOR REBANHO",
                    value = summary.custoRebanho,
                    color = TerraBarro,
                    icon = "🐑",
                    modifier = Modifier.weight(1f)
                )
                DashboardSmallCard(
                    label = "LUCRO BRUTO",
                    value = summary.receitas - summary.despesas,
                    color = if (summary.receitas - summary.despesas >= 0) VerdeCaatinga else VermelhoBarro,
                    icon = "📈",
                    modifier = Modifier.weight(1f)
                )
            }

            // LISTA DE ÚLTIMAS TRANSAÇÕES
            Text(
                "MOVIMENTAÇÕES RECENTES",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = TextoPrincipal,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (transacoes.isEmpty()) {
                Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Text("Nenhuma transação registrada.", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                transacoes.take(5).forEach { transacao ->
                    TransactionItem(transacao)
                }
            }

            // DICA DO DIA
            Surface(
                color = SolNordeste.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SolNordeste),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💡", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "O valor do seu rebanho representa seu patrimônio acumulado.",
                        color = TextoPrincipal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardSmallCard(label: String, value: Double, color: Color, icon: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(label, fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrency(value),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

@Composable
fun TransactionItem(t: TransacaoEntity) {
    val isReceita = t.tipo == tipoTransacao.Receita
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(if (isReceita) VerdeCaatinga.copy(0.1f) else VermelhoBarro.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isReceita) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (isReceita) VerdeCaatinga else VermelhoBarro,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(t.descricao, fontWeight = FontWeight.Bold, color = TextoPrincipal, fontSize = 14.sp)
                Text(t.data, color = Color.Gray, fontSize = 12.sp)
            }
            Text(
                text = (if (isReceita) "+ " else "- ") + formatCurrency(t.valor),
                fontWeight = FontWeight.Black,
                color = if (isReceita) VerdeCaatinga else VermelhoBarro,
                fontSize = 14.sp
            )
        }
    }
}
