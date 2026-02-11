package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.viewmodel.AnimalViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    animalViewModel: AnimalViewModel
) {

    // 🔹 Pegando animais do banco (Flow)
    val animals by animalViewModel.animals.collectAsState(initial = emptyList())

    // 🔹 Calculando custos reais (exemplo: usando campo "valor")
    val totalCusto = animals.sumOf { it.preco ?: 0.0 }
    val totalVendasPotencial = totalCusto * 1.6
    val lucroEstimado = totalVendasPotencial - totalCusto

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("VISÃO DO INVESTIDOR", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                )
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

            // 🔹 CARD PRINCIPAL LUCRO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = VerdeCaatinga),
                shape = RoundedCornerShape(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("LUCRO ESTIMADO", color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        text = formatCurrency(lucroEstimado),
                        color = SolNordeste,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // 🔹 CARDS MENORES
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DashboardSmallCard("INVESTIDO", totalCusto, TerraBarro, Modifier.weight(1f))
                DashboardSmallCard("PROJEÇÃO", totalVendasPotencial, TextoPrincipal, Modifier.weight(1f))
            }

            // 🔹 DICA
            Surface(
                color = SolNordeste,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("☀️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "DICA: Manter os custos baixos é o segredo do lucro no final.",
                        color = TextoPrincipal,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardSmallCard(label: String, value: Double, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.Gray)
            Text(
                text = formatCurrency(value),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}
