package com.example.conectaovinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.conectaovinos.ui.theme.ConectaOvinosTheme
import com.example.conectaovinos.ui.theme.*

// Importação de todas as telas do projeto
import com.example.conectaovinos.InventoryScreen
import com.example.conectaovinos.AddProductScreen
import com.example.conectaovinos.AnimalDetailsScreen
import com.example.conectaovinos.CreateAdScreen
import com.example.conectaovinos.MyAdsScreen
import com.example.conectaovinos.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConectaOvinosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
        composable(AppScreen.Main.route) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavScreen.Inventory.route,
            Modifier.padding(innerPadding)
        ) {
            // Aba 1: Inventário de Animais e Produtos
            composable(BottomNavScreen.Inventory.route) { InventoryScreen(navController) }

            // Aba 2: Gestão de Anúncios no Marketplace
            composable(BottomNavScreen.Ads.route) { MyAdsScreen(navController) }

            // Aba 3: Painel do Investidor (Financeiro)
            composable(BottomNavScreen.Dashboard.route) { DashboardScreen(navController) }

            // Rotas de formulários e detalhes (fora da barra principal)
            composable("add_product_form") { AddProductScreen(navController) }

            composable("animal_details/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AnimalDetailsScreen(navController, animalId)
            }

            composable("create_ad_form/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                CreateAdScreen(navController, animalId)
            }
        }
    }
}

// Tela de Login (Simulada para o MVP)
@Composable
fun AuthenticationScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            navController.navigate(AppScreen.Main.route) {
                popUpTo(AppScreen.Authentication.route) { inclusive = true }
            }
        }) {
            Text("Entrar no Conecta:Ovinos")
        }
    }
}

// Componente da Barra de Navegação Inferior
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavScreen.Inventory,
        BottomNavScreen.Ads,
        BottomNavScreen.Dashboard
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = screen.title
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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

// Definição das Rotas Principais
sealed class AppScreen(val route: String) {
    object Authentication : AppScreen("authentication")
    object Main : AppScreen("main")
}

// Definição das Abas da Barra Inferior
sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    object Inventory : BottomNavScreen("inventory", "Inventário", R.drawable.ic_herd)
    object Ads : BottomNavScreen("ads", "Anúncios", R.drawable.ic_ads)
    object Dashboard : BottomNavScreen("dashboard", "Financeiro", R.drawable.ic_finance)
}