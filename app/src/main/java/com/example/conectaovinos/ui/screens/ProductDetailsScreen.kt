package com.example.conectaovinos.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.ConectaOvinosApp
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.AnuncioViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(navController: NavController, anuncioId: String?) {
    val app = LocalContext.current.applicationContext as ConectaOvinosApp
    val viewModel: AnuncioViewModel = viewModel(
        factory = AnuncioViewModel.Factory(app.anuncioRepository)
    )
    val context = LocalContext.current

    val anuncios by viewModel.anunciosAtivos.collectAsState()
    val anuncio = anuncios.find { it.id == anuncioId }

    if (anuncio == null) {
        Scaffold(containerColor = CinzaAreia) { padding ->
            Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Eita! Não conseguimos encontrar este anúncio.",
                    color = Color.DarkGray,
                    fontSize = 18.sp
                )
            }
        }
        return
    }

    val precoVenda = anuncio.precoVenda
    val nomeProduto = anuncio.nomeAnimal
    val racaDetalhe = anuncio.racaAnimal

    val emoji = when {
        racaDetalhe.contains("Bovino", ignoreCase = true) -> "🐄"
        racaDetalhe.contains("Caprino", ignoreCase = true) -> "🐐"
        racaDetalhe.contains("Suíno", ignoreCase = true) -> "🐖"
        racaDetalhe.contains("Queijo", ignoreCase = true) -> "🧀"
        racaDetalhe.contains("Carne", ignoreCase = true) -> "🥩"
        else -> "🐑"
    }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("DETALHES DO ANÚNCIO", fontWeight = FontWeight.Black, fontSize = 18.sp) },
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
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 24.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { abrirWhatsApp(context, nomeProduto, precoVenda) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        Icons.Filled.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "CONVERSAR COM O PRODUTOR",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            val pagerState = rememberPagerState(pageCount = {
                anuncio.imageUrls.size.coerceAtLeast(1)
            })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        Brush.verticalGradient(colors = listOf(Color(0xFFE2DED4), CinzaAreia))
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (anuncio.imageUrls.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = anuncio.imageUrls[page],
                            contentDescription = nomeProduto,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    if (anuncio.imageUrls.size > 1) {
                        Row(
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(anuncio.imageUrls.size) { iteration ->
                                val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(8.dp)
                                )
                            }
                        }
                    }
                } else {
                    Text(text = emoji, fontSize = 120.sp)
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BadgeStatus("🟢 Disponível", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                    BadgeStatus("✅ Verificado", Color(0xFFE3F2FD), Color(0xFF1565C0))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = nomeProduto.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    color = TextoPrincipal
                )

                Text(
                    text = formatarMoeda(precoVenda),
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp,
                    color = VerdeCaatinga
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Descrição", fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text(
                    text = anuncio.descricao.ifBlank { "Sem descrição adicional fornecida pelo vendedor." },
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    color = TerraBarro.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TerraBarro)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("📍 Localização", fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(
                                anuncio.endereco.ifBlank { "Tauá, Ceará (Região dos Inhamuns)" }, 
                                color = Color.Gray, 
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun BadgeStatus(texto: String, corFundo: Color, corTexto: Color) {
    Surface(color = corFundo, shape = RoundedCornerShape(8.dp)) {
        Text(texto, color = corTexto, fontWeight = FontWeight.Black, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

private fun abrirWhatsApp(context: Context, nomeProduto: String, preco: Double) {
    val precoFormatado = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(preco)
    val mensagem = "Olá! Vi o anúncio de '$nomeProduto' por $precoFormatado no aplicativo ConectaOvinos e tenho interesse."
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://api.whatsapp.com/send?phone=5588999999999&text=${Uri.encode(mensagem)}")
    try { context.startActivity(intent) } catch (e: Exception) {}
}

private fun formatarMoeda(valor: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}
