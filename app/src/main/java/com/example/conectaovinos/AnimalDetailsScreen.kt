package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailsScreen(navController: NavController, animalId: String?) {
    // Busca o animal na lista global
    val animal = remember { dummyProductList.find { it.id == animalId } as? Animal }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("FICHA DO ANIMAL", fontWeight = FontWeight.Black, fontSize = 16.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Editar */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        if (animal == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Animal não encontrado.", color = Color.Gray)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. ÁREA DE DESTAQUE (FOTO E NOME)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TerraBarro)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Foto do Animal (Placeholder)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐑", fontSize = 64.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = animal.nome.uppercase(),
                        color = SolNordeste,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Surface(
                        color = VerdeCaatinga,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = animal.raca.uppercase(),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. CONTEÚDO PRINCIPAL (CARDS SOBREPOSTOS)
            Column(
                modifier = Modifier
                    .offset(y = (-20).dp) // Efeito visual de sobreposição
                    .padding(horizontal = 16.dp)
            ) {

                // Card Financeiro (Lado Investidor)
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Custo de Produção", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = formatCurrency(animal.custo),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = VermelhoBarro // Custo é saída
                            )
                        }

                        VerticalDivider(modifier = Modifier.height(40.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Valor de Venda Est.", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = formatCurrency(animal.custo * 1.5), // Simulação de +50%
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = VerdeCaatinga // Venda é entrada
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detalhes Técnicos
                Text(
                    "DADOS TÉCNICOS",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailItem(
                        // Substituído CalendarToday por DateRange que é mais comum no core
                        icon = Icons.Default.DateRange,
                        label = "Nascimento",
                        value = animal.dataNascimento,
                        modifier = Modifier.weight(1f)
                    )
                    DetailItem(
                        // Substituído AttachMoney por Info para evitar erro de referência
                        icon = Icons.Default.Info,
                        label = "Status",
                        value = "Em Criação",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // BOTÕES DE AÇÃO
                Button(
                    onClick = { navController.navigate("create_ad_form/${animal.id}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SolNordeste,
                        contentColor = TextoPrincipal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ANUNCIAR PARA VENDA", fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { /* Histórico médico futuro */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TerraBarro),
                    border = androidx.compose.foundation.BorderStroke(2.dp, TerraBarro),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("VER HISTÓRICO DE VACINAS", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}