package com.example.conectaovinos.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.conectaovinos.models.AnimalLote
import com.example.conectaovinos.models.ProdutoProcessado
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.MarketplaceViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    produtoId: String?,
    viewModel: MarketplaceViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // --- OBSERVAÇÃO REATIVA DE ESTADOS ---
    val todosProdutos by viewModel.produtos.collectAsState()
    val produto = todosProdutos.find { it.id == produtoId }

    // --- TRATAMENTO DE EXCEÇÃO (PRODUTO NÃO ENCONTRADO) ---
    if (produto == null) {
        Scaffold(containerColor = CinzaAreia) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
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

    // --- MÉTODOS DE PRECIFICAÇÃO E ADAPTAÇÃO VISUAL DA FAZENDA ---
    val precoVenda = produto.custoTotal * 1.5 // Margem de lucro padrão comercializada de 50%
    val isAnimal = produto is AnimalLote

    val detalheTipo = when (produto) {
        is AnimalLote -> "Espécie: ${produto.especie}"
        is ProdutoProcessado -> "Categoria: ${produto.tipoProduto}"
    }

    // Identificação do Emoji com base na taxonomia do produto
    val emoji = when (produto) {
        is AnimalLote -> when (produto.especie.lowercase()) {
            "bovino" -> "🐄"
            "caprino" -> "🐐"
            "suíno", "suino" -> "🐖"
            else -> "🐑"
        }
        is ProdutoProcessado -> if (produto.tipoProduto.lowercase().contains("carne")) "🥩" else "🧀"
    }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "DETALHES DO ANÚNCIO",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            // --- BOTÃO DE INTENT COMPARTILHADA (WHATSAPP DE CONTATO) ---
            Surface(
                color = CinzaAreia,
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { abrirWhatsApp(context, produto.nomeAmigavel, precoVenda) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(72.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SolNordeste,
                        contentColor = TextoPrincipal
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Filled.Phone, contentDescription = null, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "FALAR COM VENDEDOR NO WHATSAPP",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- BOX DE EXIBIÇÃO DA MOCK IMAGEM (EMOJI AVATAR) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        Brush.verticalGradient(colors = listOf(Color(0xFFE2DED4), CinzaAreia))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 120.sp)
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "📷 1 / 1",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // --- BADGES DINÂMICAS DE STATUS ---
                Row(
                    modifier = Modifier.padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BadgeStatus("🟢 Disponível", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                    if (isAnimal) {
                        BadgeStatus("💉 Lote Monitorado", Color(0xFFE3F2FD), Color(0xFF1565C0))
                    } else {
                        BadgeStatus("🧀 Inspeção Local", Color(0xFFFFF3E0), Color(0xFFE65100))
                    }
                }

                Text(
                    text = produto.nomeAmigavel.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    color = TextoPrincipal,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(precoVenda),
                    fontWeight = FontWeight.Black,
                    fontSize = 36.sp,
                    color = VerdeCaatinga
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = detalheTipo,
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Renderiza metadados adicionais apenas se for um lote de animais vivos
                if (produto is AnimalLote) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoCard("Quantidade", "${produto.quantidade} un.")
                        InfoCard("Manejo", "Em Dia")
                    }
                } else if (produto is ProdutoProcessado) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoCard("Quantidade", "${produto.quantidade} ${produto.unidadeMedida}")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- COMPONENTE DE LOCALIZAÇÃO GEOGRÁFICA REGIONAL ---
                Surface(
                    color = TerraBarro.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Localização",
                            tint = TerraBarro,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "📍 Tauá, Ceará",
                                fontWeight = FontWeight.Black,
                                color = TextoPrincipal,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Região dos Inhamuns",
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Vendedor",
                    fontWeight = FontWeight.Black,
                    color = TextoPrincipal,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // --- CARD DE INFORMAÇÕES CORPORATIVAS DO PRODUTOR ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(TerraBarro),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Produtor do IFCE",
                                fontWeight = FontWeight.Black,
                                color = TextoPrincipal,
                                fontSize = 18.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = SolNordeste,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = " 5.0 (Destaque Local)",
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Sobre o Produto",
                    fontWeight = FontWeight.Black,
                    color = TextoPrincipal,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // --- BULLET POINTS DE DESCRIÇÃO DA PRODUÇÃO RURAL ---
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BulletPointText("Origem controlada", " e de excelente procedência local.")
                    BulletPointText("Produção sustentável", " baseada na agricultura familiar do Sertão.")
                    BulletPointText("Manejo e sanidade", " totalmente verificados e em conformidade.")
                    BulletPointText("Negociação facilitada", " direto com quem produz, sem intermediários.")
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun BadgeStatus(texto: String, corFundo: Color, corTexto: Color) {
    Surface(color = corFundo, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = texto,
            color = corTexto,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun InfoCard(label: String, valor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, color = Color.Gray, fontSize = 14.sp)
            Text(valor, color = TextoPrincipal, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
    }
}

@Composable
fun BulletPointText(boldPart: String, normalPart: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = VerdeCaatinga,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Black, color = TextoPrincipal)) {
                    append(boldPart)
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.DarkGray)) {
                    append(normalPart)
                }
            },
            fontSize = 18.sp,
            lineHeight = 24.sp
        )
    }
}

private fun abrirWhatsApp(context: Context, nomeProduto: String, preco: Double) {
    val precoFormatado = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(preco)
    val mensagem = "Olá! Vi o anúncio de '$nomeProduto' por $precoFormatado no aplicativo ConectaFazenda e tenho interesse. Podemos conversar?"
    val telefone = "5588999999999" // Fallback de número para simulação de contato comercial
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$telefone&text=${Uri.encode(mensagem)}")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Tratamento de erro passivo
    }
}