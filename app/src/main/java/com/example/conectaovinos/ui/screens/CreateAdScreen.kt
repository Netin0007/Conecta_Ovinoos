package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import com.example.conectaovinos.ui.viewmodels.InventoryViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(navController: NavController, animalId: String?) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val inventoryViewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(app.rebanhoRepository)
    )
    val anuncioViewModel: AnuncioViewModel = viewModel(
        factory = AnuncioViewModel.Factory(app.anuncioRepository)
    )

    // ✅ CORREÇÃO: observa o Flow inteiro e busca dentro dele
    // Assim a tela recompõe quando o banco termina de carregar
    val todosProdutos by inventoryViewModel.produtos.collectAsState()
    val animal = todosProdutos.find { it.id == animalId } as? Animal

    var precoVenda by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("CRIAR ANÚNCIO", fontWeight = FontWeight.Black) },
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

        // Enquanto o banco carrega, mostra um loading no lugar do "não encontrado"
        if (todosProdutos.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TerraBarro)
            }
            return@Scaffold
        }

        // Só chega aqui depois que o banco carregou — se ainda não achar, aí é erro real
        if (animal == null) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Animal não encontrado.", color = Color.Gray)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Card resumo do animal
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
                            .size(60.dp)
                            .background(CinzaAreia, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐑", fontSize = 32.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            animal.nome.uppercase(),
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = TextoPrincipal
                        )
                        Text("Raça: ${animal.raca}", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "Custo: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(animal.custo)}",
                            color = VermelhoBarro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "PREÇO DE VENDA",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precoVenda,
                onValueChange = { precoVenda = it },
                placeholder = { Text("Ex: 500.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("R$ ", color = TerraBarro, fontWeight = FontWeight.Bold) }
            )

            val precoDigitado = precoVenda.toDoubleOrNull()
            if (precoDigitado != null && precoDigitado > animal.custo) {
                val lucro = precoDigitado - animal.custo
                Text(
                    "Lucro estimado: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(lucro)}",
                    color = VerdeCaatinga,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "DESCRIÇÃO PARA O COMPRADOR",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val preco = precoVenda.toDoubleOrNull() ?: 0.0
                    if (preco > 0) {
                        anuncioViewModel.publicarAnimal(animal, preco, descricao)
                        navController.popBackStack("home_tabs", inclusive = false)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SolNordeste,
                    contentColor = TextoPrincipal
                ),
                enabled = precoVenda.isNotBlank() && (precoVenda.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text("PUBLICAR ANÚNCIO", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        }
    }
}