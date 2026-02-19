package com.example.conectaovinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.conectaovinos.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConectaOvinosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CinzaAreia
                ) {
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
            ProducerMainScreen(onLogout = {
                navController.navigate(AppScreen.Authentication.route) {
                    popUpTo(0) // Limpa todo o histórico, volta ao início limpo
                }
            })
        }
        composable(AppScreen.ConsumerMain.route) {
            ConsumerMainScreen(onLogout = {
                navController.navigate(AppScreen.Authentication.route) {
                    popUpTo(0) // Limpa todo o histórico, volta ao início limpo
                }
            })
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
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                "CONECTA:OVINOS",
                color = SolNordeste,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Text(
                "Escolha como deseja entrar:",
                color = Color.White,
                fontSize = 16.sp
            )

            Button(
                onClick = {
                    navController.navigate(AppScreen.ProducerMain.route) {
                        popUpTo(AppScreen.Authentication.route) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SOU PRODUTOR RURAL", fontWeight = FontWeight.Black)
            }

            OutlinedButton(
                onClick = {
                    navController.navigate(AppScreen.ConsumerMain.route) {
                        popUpTo(AppScreen.Authentication.route) { inclusive = true }
                    }
                },
                border = androidx.compose.foundation.BorderStroke(2.dp, SolNordeste),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SolNordeste),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("QUERO COMPRAR (FEIRA)", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun ProducerMainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        containerColor = CinzaAreia,
        bottomBar = { ProducerBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = BottomNavScreen.Inventory.route, Modifier.padding(innerPadding)) {
            composable(BottomNavScreen.Inventory.route) { InventoryScreen(navController, onLogout) }
            composable(BottomNavScreen.Dashboard.route) { DashboardScreen(navController) }
            composable(BottomNavScreen.Ads.route) { MyAdsScreen(navController) }
            composable("add_product_form") { AddProductScreen(navController) }
            composable("animal_details/{animalId}") { backStackEntry -> AnimalDetailsScreen(navController, backStackEntry.arguments?.getString("animalId")) }
            composable("create_ad_form/{animalId}") { backStackEntry -> CreateAdScreen(navController, backStackEntry.arguments?.getString("animalId")) }
        }
    }
}

@Composable
fun ConsumerMainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        containerColor = CinzaAreia,
        bottomBar = { ConsumerBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = "marketplace", Modifier.padding(innerPadding)) {
            composable("marketplace") { MarketplaceScreen(navController, onLogout) }
        }
    }
}

@Composable
fun ProducerBottomNavigationBar(navController: NavController) {
    val items = listOf(BottomNavScreen.Inventory, BottomNavScreen.Dashboard, BottomNavScreen.Ads)
    NavigationBar(containerColor = TerraBarro, contentColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                label = { Text(screen.title, color = if(selected) SolNordeste else Color.White) },
                icon = { Icon(painterResource(id = screen.icon), contentDescription = null) },
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TerraBarro, selectedTextColor = SolNordeste,
                    indicatorColor = SolNordeste, unselectedIconColor = Color.White.copy(alpha = 0.6f)
                ),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun ConsumerBottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = TerraBarro, contentColor = Color.White) {
        NavigationBarItem(
            label = { Text("Feira", color = SolNordeste) },
            icon = { Text("🛒", fontSize = 20.sp) },
            selected = true,
            colors = NavigationBarItemDefaults.colors(selectedIconColor = TerraBarro, indicatorColor = SolNordeste),
            onClick = { }
        )
    }
}

sealed class AppScreen(val route: String) {
    object Authentication : AppScreen("authentication")
    object ProducerMain : AppScreen("producer_main")
    object ConsumerMain : AppScreen("consumer_main")
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    object Inventory : BottomNavScreen("inventory", "Rebanho", R.drawable.ic_herd)
    object Dashboard : BottomNavScreen("dashboard", "Lucros", R.drawable.ic_finance)
    object Ads : BottomNavScreen("ads", "Vendas", R.drawable.ic_ads)
}