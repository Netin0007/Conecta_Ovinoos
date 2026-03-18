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
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.database.DatabaseProvider
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.viewmodel.ProductDetailsViewModel
import com.example.conectaovinos.viewmodel.ProductDetailsViewModelFactory
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(navController: NavController, productId: String?) {
    val context = LocalContext.current
    val db = DatabaseProvider.get(context)
    val id = productId?.toIntOrNull() ?: 0

    val viewModel: ProductDetailsViewModel = viewModel(
        factory = ProductDetailsViewModelFactory(db.produtosDao(), id)
    )

    val produto by viewModel.produto.collectAsState()

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("DETALHES DO PRODUTO", fontWeight = FontWeight.Black, fontSize = 16.sp) },
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
                CircularProgressIndicator(color = TerraBarro)
            }
            return@Scaffold
        }

        val prodData = produto!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TerraBarro)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!prodData.fotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = prodData.fotoUri,
                                contentDescription = prodData.nome,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("🧀", fontSize = 64.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(prodData.nome.uppercase(), color = SolNordeste, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Surface(color = VerdeCaatinga, shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(top = 8.dp)) {
                        Text(prodData.tipo.name.uppercase(), color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(
                modifier = Modifier.offset(y = (-20).dp).padding(horizontal = 16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Preço", fontSize = 12.sp, color = Color.Gray)
                            Text(formatCurrencyInternal(prodData.preco), fontSize = 24.sp, fontWeight = FontWeight.Black, color = VerdeCaatinga)
                        }
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Quantidade", fontSize = 12.sp, color = Color.Gray)
                            Text(prodData.quantidade, fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextoPrincipal)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("INFORMAÇÕES ADICIONAIS", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailItemProd(icon = Icons.Default.List, label = "Estoque", value = prodData.quantidade, modifier = Modifier.weight(1f))
                    DetailItemProd(icon = Icons.Default.Info, label = "ID", value = "#${prodData.id}", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* Lógica de compra ou contato */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TerraBarro, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ENTRAR EM CONTATO", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun DetailItemProd(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
        }
    }
}

private fun formatCurrencyInternal(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}