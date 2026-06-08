package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.TipoTransacao
import com.example.conectaovinos.models.Transacao
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.DashboardViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialScreen(
    navController: NavController,
    onSwitchToBuyer: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val transacoes by viewModel.transacoes.collectAsState()
    val anuncios by viewModel.todosAnuncios.collectAsState()

    val totalReceitas = viewModel.getTotalReceitas(transacoes)
    val totalDespesas = viewModel.getTotalDespesas(transacoes)
    val lucroLiquido = viewModel.getLucroLiquido(transacoes)
    val lucroEsperadoAnuncios = viewModel.getLucroEsperadoAnuncios(anuncios)

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("CONTROLE FINANCEIRO", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onSwitchToBuyer) {
                        Icon(
                            Icons.Rounded.Storefront,
                            contentDescription = "Ir para Feira",
                            tint = SolNordeste
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_transaction") },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                shape = RoundedCornerShape(16.dp)
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
            FinancialDashboard(
                receitas = totalReceitas,
                despesas = totalDespesas,
                lucro = lucroLiquido,
                lucroEsperado = lucroEsperadoAnuncios
            )

            Text(
                "HISTÓRICO DE FLUXO",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (transacoes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma transação registrada.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(transacoes) { transacao ->
                        TransactionListItem(transacao = transacao)
                    }
                }
            }
        }
    }
}

@Composable
fun FinancialDashboard(receitas: Double, despesas: Double, lucro: Double, lucroEsperado: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Analytics, contentDescription = null, tint = TerraBarro)
            Spacer(modifier = Modifier.width(8.dp))
            Text("RESUMO DA FAZENDA", fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("RECEITAS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(formatCurrency(receitas), color = VerdeCaatinga, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("DESPESAS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(formatCurrency(despesas), color = Color.Red, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = CinzaAreia)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("LUCRO ATUAL", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextoPrincipal)
                Text(
                    formatCurrency(lucro),
                    color = if (lucro >= 0) VerdeCaatinga else Color.Red,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                )
            }

            Surface(
                color = SolNordeste.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text("ESPERADO (FEIRA)", fontSize = 9.sp, fontWeight = FontWeight.Black, color = TerraBarro)
                    Text(formatCurrency(lucroEsperado), color = TerraBarro, fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(transacao: Transacao) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (transacao.tipo == TipoTransacao.Receita) VerdeCaatinga.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (transacao.tipo == TipoTransacao.Receita) "⬆️" else "⬇️", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(transacao.descricao.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = TextoPrincipal)
                Text(transacao.categoria, fontSize = 12.sp, color = Color.Gray)
            }

            Text(
                text = formatCurrency(transacao.valor),
                color = if (transacao.tipo == TipoTransacao.Receita) VerdeCaatinga else Color.Red,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}