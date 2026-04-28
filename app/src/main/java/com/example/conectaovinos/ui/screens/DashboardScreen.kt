package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.conectaovinos.rebanhoGlobal
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

/**
 * Tela de Dashboard Financeiro (Resumo do Bolso).
 * Focada em altíssima acessibilidade para produtores com baixa escolaridade.
 * Utiliza cores fortes (Verde/Vermelho) e símbolos universais (Setas/Emojis)
 * para que o entendimento do lucro e do custo seja imediato, mesmo sem leitura.
 *
 * @param navController Controlador de navegação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    // Lógica Financeira Básica
    val totalCusto = rebanhoGlobal.sumOf { it.custo }
    val totalVendasPotencial = totalCusto * 1.6
    val lucroEstimado = totalVendasPotencial - totalCusto

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                // Linguagem simples e direta
                title = { Text("MEU DINHEIRO", fontWeight = FontWeight.Black) },
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
            // CARTÃO PRINCIPAL: LUCRO (O que sobra no bolso)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = VerdeCaatinga),
                shape = RoundedCornerShape(16.dp), // Bordas mais arredondadas e amigáveis
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícone gigante para associação imediata
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

            // LINHA DE CARTÕES MENORES (Gastos e Vendas)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Cartão de Custo: Usa ícone de queda e cor de alerta (TerraBarro/Avermelhado)
                DashboardSmallCard(
                    label = "GASTOS (CUSTO)",
                    value = totalCusto,
                    icon = "📉",
                    accentColor = TerraBarro,
                    modifier = Modifier.weight(1f)
                )

                // Cartão de Venda: Usa ícone de subida e cor positiva
                DashboardSmallCard(
                    label = "VENDAS (ESPERADO)",
                    value = totalVendasPotencial,
                    icon = "📈",
                    accentColor = VerdeCaatinga,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // DICA VISUAL
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

/**
 * Componente interno: Cartão pequeno para exibir métricas secundárias.
 * Baseia-se em ícones grandes e cores fortes para ajudar na leitura.
 *
 * @param label Texto descritivo (ex: "GASTOS").
 * @param value Valor financeiro a ser formatado.
 * @param icon Emoji ou símbolo que representa a métrica.
 * @param accentColor Cor de destaque para o valor e para o fundo do ícone.
 * @param modifier Modificador de layout.
 */
@Composable
private fun DashboardSmallCard(label: String, value: Double, icon: String, accentColor: Color, modifier: Modifier) {
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

/**
 * Função utilitária para formatar valores.
 */
private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}