package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.DashboardViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.Factory(app.transacaoRepository)
    )

    val transacoes by viewModel.transacoes.collectAsState()
    val totalReceitas = viewModel.getTotalReceitas(transacoes)
    val totalDespesas = viewModel.getTotalDespesas(transacoes)
    val lucroEstimado = viewModel.getLucroLiquido(transacoes)

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("MEU DINHEIRO", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("financial") }) {
                        Icon(Icons.Default.List, contentDescription = "Ver Extrato")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = VerdeCaatinga),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💰", fontSize = 48.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "LUCRO (DINHEIRO LIVRE)",
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = formatCurrency(lucroEstimado),
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DashboardSmallCard(
                    label = "GASTOS (CUSTO)",
                    value = totalDespesas,
                    icon = "📉",
                    accentColor = TerraBarro,
                    modifier = Modifier.weight(1f)
                )
                DashboardSmallCard(
                    label = "VENDAS (ESPERADO)",
                    value = totalReceitas,
                    icon = "📈",
                    accentColor = VerdeCaatinga,
                    modifier = Modifier.weight(1f)
                )
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = SolNordeste),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💡", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "DICA: Manter os custos baixos é o segredo para sobrar dinheiro no final do mês.",
                        color = TextoPrincipal,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardSmallCard(
    label: String,
    value: Double,
    icon: String,
    accentColor: Color,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formatCurrency(value),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}