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


import com.example.conectaovinos.InventoryScreen
import com.example.conectaovinos.AddProductScreen
import com.example.conectaovinos.AnimalDetailsScreen
import com.example.conectaovinos.CreateAdScreen
import com.example.conectaovinos.MyAdsScreen
import com.example.conectaovinos.FinancialScreen
import com.example.conectaovinos.AddTransactionScreen
import com.example.conectaovinos.AddManejoScreen

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
            // Rotas das Abas
            composable(BottomNavScreen.Inventory.route) { InventoryScreen(navController) }
            composable(BottomNavScreen.Ads.route) { MyAdsScreen(navController) }
            composable(BottomNavScreen.Finance.route) { FinancialScreen(navController) }

            // Rotas de Formulários e Detalhes
            composable("add_product_form") { AddProductScreen(navController) }
            composable("add_transaction_form") { AddTransactionScreen(navController) }

            composable("animal_details/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AnimalDetailsScreen(navController, animalId)
            }
            composable("create_ad_form/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                CreateAdScreen(navController, animalId)
            }

            // A NOVA ROTA PARA O FORMULÁRIO DE MANEJO
            composable("add_manejo_form/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AddManejoScreen(navController, animalId)
            }
        }
    }
}

@Composable
fun AuthenticationScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            navController.navigate(AppScreen.Main.route) {
                popUpTo(AppScreen.Authentication.route) { inclusive = true }
            }
        }) {
            Text("Login / Cadastro (Simulado)")
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavScreen.Inventory,
        BottomNavScreen.Ads,
        BottomNavScreen.Finance
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = {
                    Icon(
                        painterResource(id = screen.icon),
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


sealed class AppScreen(val route: String) {
    object Authentication : AppScreen("authentication")
    object Main : AppScreen("main")
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    object Inventory : BottomNavScreen("inventory", "Inventário", R.drawable.ic_herd)
    object Ads : BottomNavScreen("ads", "Anúncios", R.drawable.ic_ads)
    object Finance : BottomNavScreen("finance", "Finanças", R.drawable.ic_finance)
}