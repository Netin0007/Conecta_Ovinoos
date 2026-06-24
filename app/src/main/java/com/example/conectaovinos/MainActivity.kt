package com.example.conectaovinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.conectaovinos.ui.screens.*
import com.example.conectaovinos.ui.screens.AddTransactionScreen
import com.example.conectaovinos.ui.screens.FinancialScreen
import com.example.conectaovinos.ui.screens.MarketplaceScreen
import com.example.conectaovinos.ui.screens.ProductDetailsScreen
import com.example.conectaovinos.ui.screens.AnimalDetailsScreen
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.ui.viewmodels.CadastroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConectaOvinosTheme {
                ConectaOvinosMainUI()
            }
        }
    }
}

@Composable
fun ConectaOvinosMainUI() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            // ── PORTAL DE ENTRADA ──────────────────────────────────────────
            composable("welcome") {
                WelcomeScreen(navController = navController)
            }

            // ── ÁREA DO PRODUTOR ───────────────────────────────────────────
            composable("inventory") {
                InventoryScreen(navController)
            }

            composable("add_product") {
                val app = LocalContext.current.applicationContext as ConectaOvinosApp
                val viewModel: CadastroViewModel = viewModel(
                    factory = CadastroViewModel.Factory(app.rebanhoRepository)
                )
                AddProductScreen(navController, viewModel = viewModel)
            }

            composable(
                route = "add_product/{produtoId}",
                arguments = listOf(navArgument("produtoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val produtoId = backStackEntry.arguments?.getString("produtoId")
                val app = LocalContext.current.applicationContext as ConectaOvinosApp
                val viewModel: CadastroViewModel = viewModel(
                    factory = CadastroViewModel.Factory(app.rebanhoRepository)
                )
                AddProductScreen(navController, produtoId, viewModel = viewModel)
            }

            composable(
                route = "create_ad/{produtoId}",
                arguments = listOf(navArgument("produtoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val produtoId = backStackEntry.arguments?.getString("produtoId")
                CreateAdScreen(navController, produtoId)
            }

            // ── FINANCEIRO ─────────────────────────────────────────────────
            composable("dashboard") {
                DashboardScreen(navController)
            }

            composable("financial") {
                FinancialScreen(navController)
            }

            composable("add_transaction_form") {
                AddTransactionScreen(navController)  // tela já existe no projeto
            }

            // ── FEIRA (MARKETPLACE PÚBLICO) ────────────────────────────────
            // ATENÇÃO: "feira" = tela pública para compradores (MarketplaceScreen)
            composable("feira") {
                MarketplaceScreen(
                    navController = navController,
                    onSwitchToProducer = {
                        navController.navigate("inventory") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    },
                    onLogout = {
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Detalhes de um anúncio na feira (comprador vê o produto)
            composable(
                route = "product_details/{anuncioId}",
                arguments = listOf(navArgument("anuncioId") { type = NavType.StringType })
            ) { backStackEntry ->
                val anuncioId = backStackEntry.arguments?.getString("anuncioId")
                ProductDetailsScreen(navController, anuncioId)
            }

            // ── MEUS ANÚNCIOS (PRODUTOR) ───────────────────────────────────
            // ATENÇÃO: "marketplace" = painel do produtor com os anúncios DELE (MyAdsScreen)
            composable("marketplace") {
                MyAdsScreen(navController)
            }

            // ── DETALHES DO ANIMAL (aberto pelo produtor a partir do inventário) ──
            composable(
                route = "animal_details/{animalId}",
                arguments = listOf(navArgument("animalId") { type = NavType.StringType })
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AnimalDetailsScreen(navController, animalId)
            }
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CinzaAreia)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CONECTA OVINOS",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = TerraBarro,
            letterSpacing = 2.sp
        )
        Text(
            text = "Escolha como deseja acessar a plataforma hoje",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 48.dp)
        )

        // Card 1: Comprador -> Vai direto para a Feira
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("feira") }, // ← CORRIGIDO PARA "feira"
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Storefront, contentDescription = null, tint = TerraBarro, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("FEIRA LIVRE", fontWeight = FontWeight.Black, fontSize = 20.sp, color = TextoPrincipal)
                    Text("Explorar anúncios de animais e derivados do Sertão.", fontSize = 13.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card 2: Produtor -> Vai para o Estoque
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("inventory") },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = TerraBarro),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.Inventory2, contentDescription = null, tint = SolNordeste, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("ÁREA DO PRODUTOR", fontWeight = FontWeight.Black, fontSize = 20.sp, color = SolNordeste)
                    Text("Gerenciar rebanho, controlar finanças e cadastrar estoque.", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

// Configuração Condicional da Barra Inferior
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : BottomNavItem("inventory", "Estoque", Icons.Rounded.Inventory2)
    object Feira      : BottomNavItem("feira", "Feira", Icons.Rounded.Storefront) // ← CORRIGIDO
    object Dashboard : BottomNavItem("dashboard", "Finanças", Icons.Rounded.Analytics)
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Inventory,
        BottomNavItem.Feira,
        BottomNavItem.Dashboard
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Rotas onde a barra deve ficar OCULTA
    val rotasOcultas = setOf("welcome")
    val rotaAtual = currentDestination?.route ?: ""
    val deveOcultar = rotasOcultas.any { rotaAtual == it }

    if (!deveOcultar) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = TerraBarro,
            tonalElevation = 8.dp
        ) {
            items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) TerraBarro else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            item.title,
                            color = if (isSelected) TerraBarro else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = SolNordeste.copy(alpha = 0.2f)
                    ),
                    selected = isSelected,
                    onClick = {
                        // Navega para a aba selecionada limpando o que estiver acima da raiz
                        navController.navigate(item.route) {
                            // Se a aba já estiver aberta, volta para o início dela (PopUpTo)
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Evita criar múltiplas instâncias da mesma aba
                            launchSingleTop = true
                            // Restaura o estado anterior (scroll, etc) se existir
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}