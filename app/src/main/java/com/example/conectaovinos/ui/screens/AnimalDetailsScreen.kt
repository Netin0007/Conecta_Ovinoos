package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnimalDetailsViewModel
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailsScreen(navController: NavController, animalId: String?) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp

    val detailsViewModel: AnimalDetailsViewModel = viewModel(
        factory = AnimalDetailsViewModel.Factory(app.rebanhoRepository)
    )
    val anuncioViewModel: AnuncioViewModel = viewModel(
        factory = AnuncioViewModel.Factory(app.anuncioRepository)
    )

    val todosProdutos by detailsViewModel.produtos.collectAsState()
    val todosAnuncios by anuncioViewModel.todosAnuncios.collectAsState()

    val produto = todosProdutos.find { it.id == animalId }
    val anuncioAtivo = todosAnuncios.find { it.animalId == animalId && it.ativo }
    val jaAnunciado = anuncioAtivo != null

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("FICHA TÉCNICA", fontWeight = FontWeight.Black, fontSize = 18.sp) },
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

        if (produto == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                if (todosProdutos.isEmpty()) {
                    CircularProgressIndicator(color = TerraBarro)
                } else {
                    Text("Registro não encontrado no estoque.", color = Color.Gray)
                }
            }
            return@Scaffold
        }

        val emoji = when (produto) {
            is AnimalLote -> emojiParaEspecie(produto.especie)
            is ProdutoProcessado -> emojiParaDerivado(produto.tipoProduto)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().background(TerraBarro).padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        if (produto.imageUrls.isNotEmpty()) {
                            AsyncImage(
                                model = produto.imageUrls.first(),
                                contentDescription = produto.nomeAmigavel,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(emoji, fontSize = 56.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = produto.nomeAmigavel.uppercase(),
                        color = SolNordeste,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    if (jaAnunciado) {
                        Surface(
                            color = SolNordeste,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                "ANÚNCIO ATIVO NA FEIRA",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = TextoPrincipal
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.offset(y = (-20).dp).padding(horizontal = 16.dp)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Valor no Estoque", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(
                            formatarMoeda(produto.custoTotal),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = VerdeCaatinga
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("DETALHES DO REGISTRO", fontWeight = FontWeight.Black, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (produto is AnimalLote) {
                        DetailRow("Espécie", produto.especie)
                        DetailRow("Raça", produto.raca.ifBlank { "Não informada" })
                        DetailRow("Sexo", produto.sexo)
                        DetailRow("Brinco", produto.brinco.ifBlank { "Sem identificação" })
                        DetailRow("Peso", "${produto.peso} kg")
                        DetailRow("Vacinação", if (produto.vacinado) "Em dia ✅" else "Pendente ⚠️")
                    } else if (produto is ProdutoProcessado) {
                        DetailRow("Tipo", produto.tipoProduto)
                        DetailRow("Lote", produto.codigoLote.ifBlank { "—" })
                        DetailRow("Quantidade", "${produto.quantidade} ${produto.unidadeMedida}")
                    }
                    DetailRow("Cadastrado em", formatTimestamp(produto.dataRegistro))
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (jaAnunciado) {
                    Button(
                        onClick = { navController.navigate("product_details/${anuncioAtivo?.id}") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VER ANÚNCIO NA FEIRA", fontWeight = FontWeight.Black)
                    }
                } else {
                    Button(
                        onClick = { navController.navigate("create_ad/${produto.id}") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("VENDER NA FEIRA", fontWeight = FontWeight.Black)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { /* TODO: Implementar edição */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("EDITAR INFORMAÇÕES", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(value, fontWeight = FontWeight.Black, color = TextoPrincipal)
    }
}

private fun formatarMoeda(valor: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}

private fun formatTimestamp(timeInMillis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    return formatter.format(Date(timeInMillis))
}

private fun emojiParaEspecie(especie: String): String {
    return when (especie.lowercase()) {
        "bovino" -> "🐄"
        "caprino" -> "🐐"
        "suíno", "suino" -> "🐖"
        else -> "🐑"
    }
}

private fun emojiParaDerivado(tipo: String): String {
    return if (tipo.lowercase().contains("carne")) "🥩" else "🧀"
}
