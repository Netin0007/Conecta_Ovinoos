package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.rebanhoGlobal
import com.example.conectaovinos.ui.components.FormSectionTitle
import com.example.conectaovinos.ui.components.SertaoTextField
import com.example.conectaovinos.ui.theme.*

/**
 * Tela de Criação de Anúncio para a Feira Livre (Marketplace).
 * Aplica os conceitos de formulário limpo e Call-to-Action (botão) de alta conversão.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(navController: NavController) {
    // Estados do formulário
    var precoVenda by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("NOVO ANÚNCIO", fontWeight = FontWeight.Black) },
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

            // --- PROTEÇÃO CONTRA ERROS (UX PREVENTIVA) ---
            // Se ele tentar anunciar sem ter nada no estoque, avisamos com carinho.
            if (rebanhoGlobal.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📦", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Seu estoque está vazio.", color = TerraBarro, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Vá no Estoque e adicione um lote primeiro para poder anunciar.", fontSize = 14.sp, color = Color.Gray, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            } else {

                // --- FORMULÁRIO DE ANÚNCIO ---
                FormSectionTitle("1. DETALHES DA VENDA")

                SertaoTextField(
                    value = precoVenda,
                    onValueChange = { precoVenda = it },
                    label = "Preço de Venda (R$)",
                    keyboardType = KeyboardType.Number,
                    helperText = "Qual o valor total que você quer receber pelo lote?"
                )

                Spacer(modifier = Modifier.height(8.dp))

                SertaoTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = "Descrição para os compradores (Opcional)",
                    placeholder = "Ex: Animais saudáveis, vacinados, excelente genética...",
                    helperText = "Uma boa descrição vende muito mais rápido!"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- O BOTÃO QUE RESOLVE O ERRO DE NAVEGAÇÃO ---
                Button(
                    onClick = {
                        // A MÁGICA ACONTECE AQUI:
                        // Usamos as novas strings de rota e voltamos a pilha para não pesar o celular.
                        navController.navigate("marketplace") {
                            popUpTo("inventory") { inclusive = false }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeCaatinga, // Verde significa conversão/venda!
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray
                    ),
                    enabled = precoVenda.isNotBlank() // Só deixa clicar se tiver preço
                ) {
                    Icon(Icons.Rounded.Storefront, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PUBLICAR NA FEIRA", fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}