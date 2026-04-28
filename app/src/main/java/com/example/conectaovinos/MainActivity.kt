package com.example.conectaovinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.conectaovinos.ui.screens.AddProductScreen
import com.example.conectaovinos.ui.screens.AnimalDetailsScreen
import com.example.conectaovinos.ui.screens.CommunityScreen
import com.example.conectaovinos.ui.screens.CreateAdScreen
import com.example.conectaovinos.ui.screens.DashboardScreen
import com.example.conectaovinos.ui.screens.InventoryScreen
import com.example.conectaovinos.ui.screens.MarketplaceScreen
import com.example.conectaovinos.ui.screens.MyAdsScreen
import com.example.conectaovinos.ui.screens.ProductDetailsScreen
import com.example.conectaovinos.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConectaOvinosTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = CinzaAreia) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreen.Authentication.route) {
        composable(AppScreen.Authentication.route) {
            AuthenticationScreen(navController = navController)
        }
        composable(AppScreen.ProducerMain.route) {
            ProducerMainScreen(
                onLogout = {
                    navController.navigate(AppScreen.Authentication.route) { popUpTo(0) }
                },
                onSwitchToConsumer = {
                    navController.navigate(AppScreen.ConsumerMain.route) {
                        popUpTo(AppScreen.ProducerMain.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreen.ConsumerMain.route) {
            ConsumerMainScreen(
                onLogout = {
                    navController.navigate(AppScreen.Authentication.route) { popUpTo(0) }
                },
                onSwitchToProducer = {
                    navController.navigate(AppScreen.ProducerMain.route) {
                        popUpTo(AppScreen.ConsumerMain.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun AuthenticationScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize().background(TerraBarro),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp).fillMaxWidth() // Adaptável a todas as telas
        ) {
            Text("CONECTA:OVINOS", color = SolNordeste, fontSize = 34.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            Text("Escolha como deseja entrar:", color = Color.White, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(AppScreen.ProducerMain.route) { popUpTo(AppScreen.Authentication.route) { inclusive = true } }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
                modifier = Modifier.fillMaxWidth().height(72.dp), // BOTOES AINDA MAIORES PARA MÃO DO PRODUTOR
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("SOU PRODUTOR RURAL", fontWeight = FontWeight.Black, fontSize = 18.sp)
            }

            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreen.ConsumerMain.route) { popUpTo(AppScreen.Authentication.route) { inclusive = true } }
                },
                border = androidx.compose.foundation.BorderStroke(3.dp, SolNordeste),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SolNordeste),
                modifier = Modifier.fillMaxWidth().height(72.dp), // BOTOES AINDA MAIORES
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("QUERO COMPRAR (FEIRA)", fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
        }
    }
}

// --- FLUXO DO PRODUTOR ---
@Composable
fun ProducerMainScreen(onLogout: () -> Unit, onSwitchToConsumer: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home_tabs") {
        composable("home_tabs") { ProducerHomeTabs(navController, onLogout, onSwitchToConsumer) }
        composable("add_product_form") { AddProductScreen(navController) }
        composable("animal_details/{animalId}") { backStackEntry -> AnimalDetailsScreen(navController, backStackEntry.arguments?.getString("animalId")) }
        composable("create_ad_form/{animalId}") { backStackEntry -> CreateAdScreen(navController, backStackEntry.arguments?.getString("animalId")) }
    }
}

@Composable
fun ProducerHomeTabs(navController: NavController, onLogout: () -> Unit, onSwitchToConsumer: () -> Unit) {
    val items = listOf(BottomNavScreen.Inventory, BottomNavScreen.Dashboard, BottomNavScreen.Ads, BottomNavScreen.Community)
    val pagerState = rememberPagerState(pageCount = { items.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = TerraBarro,
                contentColor = Color.White,
                modifier = Modifier.height(80.dp) // BARRA INFERIOR MAIS ROBUSTA
            ) {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        label = { Text(screen.title, color = if(pagerState.currentPage == index) SolNordeste else Color.White, fontSize = 12.sp) }, // Fonte maiorzinha
                        icon = { Icon(painterResource(id = screen.icon), contentDescription = null, modifier = Modifier.size(28.dp)) },
                        selected = pagerState.currentPage == index,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TerraBarro, selectedTextColor = SolNordeste,
                            indicatorColor = SolNordeste, unselectedIconColor = Color.White.copy(alpha = 0.6f)
                        ),
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) { page ->
            when (page) {
                0 -> InventoryScreen(navController, onLogout, onSwitchToConsumer)
                1 -> DashboardScreen(navController)
                2 -> MyAdsScreen(navController)
                3 -> CommunityScreen(navController)
            }
        }
    }
}

// --- FLUXO DO CONSUMIDOR ---
@Composable
fun ConsumerMainScreen(onLogout: () -> Unit, onSwitchToProducer: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "consumer_home_tabs") {
        composable("consumer_home_tabs") { ConsumerHomeTabs(navController, onLogout, onSwitchToProducer) }
        composable("product_details/{productId}") { backStackEntry ->
            ProductDetailsScreen(navController, backStackEntry.arguments?.getString("productId"))
        }
    }
}

@Composable
fun ConsumerHomeTabs(navController: NavController, onLogout: () -> Unit, onSwitchToProducer: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = TerraBarro,
                contentColor = Color.White,
                modifier = Modifier.height(80.dp) // BARRA INFERIOR MAIS ROBUSTA
            ) {
                NavigationBarItem(
                    label = { Text("Feira", color = if(pagerState.currentPage == 0) SolNordeste else Color.White, fontSize = 14.sp) },
                    icon = { Text("🛒", fontSize = 28.sp) },
                    selected = pagerState.currentPage == 0,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = TerraBarro, indicatorColor = SolNordeste),
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }
                )
                NavigationBarItem(
                    label = { Text("Favoritos", color = if(pagerState.currentPage == 1) SolNordeste else Color.White, fontSize = 14.sp) },
                    icon = { Text("❤️", fontSize = 28.sp) },
                    selected = pagerState.currentPage == 1,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = TerraBarro, indicatorColor = SolNordeste),
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) { page ->
            when (page) {
                0 -> MarketplaceScreen(navController, onLogout, onSwitchToProducer)
                1 -> FavoritosPlaceholderScreen()
            }
        }
    }
}

@Composable
fun FavoritosPlaceholderScreen() {
    Box(modifier = Modifier.fillMaxSize().background(CinzaAreia), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text("❤️", fontSize = 80.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Os seus produtos favoritos", fontWeight = FontWeight.Black, fontSize = 20.sp, color = TextoPrincipal, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Text("aparecerão aqui em breve.", color = Color.Gray, fontSize = 16.sp)
        }
    }
}

// --- ROTAS E MENUS ---
sealed class AppScreen(val route: String) {
    object Authentication : AppScreen("authentication")
    object ProducerMain : AppScreen("producer_main")
    object ConsumerMain : AppScreen("consumer_main")
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    object Inventory : BottomNavScreen("inventory", "Rebanho", R.drawable.ic_herd)
    object Dashboard : BottomNavScreen("dashboard", "Lucros", R.drawable.ic_finance)
    object Ads : BottomNavScreen("ads", "Vendas", R.drawable.ic_ads)
    object Community : BottomNavScreen("community", "Prosa", R.drawable.ic_ads)
}