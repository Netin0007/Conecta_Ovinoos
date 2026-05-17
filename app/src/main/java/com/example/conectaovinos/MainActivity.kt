package com.example.conectaovinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.conectaovinos.ui.screens.AddProductScreen
import com.example.conectaovinos.ui.screens.CommunityScreen
import com.example.conectaovinos.ui.screens.CreateAdScreen
import com.example.conectaovinos.ui.screens.DashboardScreen
import com.example.conectaovinos.ui.screens.InventoryScreen
import com.example.conectaovinos.ui.screens.MyAdsScreen
import com.example.conectaovinos.ui.theme.ConectaOvinosTheme
import com.example.conectaovinos.ui.theme.SolNordeste
import com.example.conectaovinos.ui.theme.TerraBarro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConectaOvinosTheme {
                ConectaOvinosApp()
            }
        }
    }
}

/**
 * Estrutura Base do Aplicativo (UX Premium com Bottom Navigation)
 */
@Composable
fun ConectaOvinosApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { innerPadding ->
        // O MAESTRO DA NAVEGAÇÃO
        NavHost(
            navController = navController,
            startDestination = "inventory", // A tela inicial é o Estoque
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inventory") { InventoryScreen(navController) }
            composable("add_product") { AddProductScreen(navController) }
            composable("dashboard") { DashboardScreen(navController) }
            composable("community") { CommunityScreen(navController) }

            // Conectamos a Feira Livre à tela de Anúncios que você já tem
            composable("marketplace") { MyAdsScreen(navController) }

            // Rota inteligente que recebe o ID do animal para a criação do anúncio
            composable("create_ad/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                CreateAdScreen(navController, animalId)
            }
        }
    }
}

// --- CONFIGURAÇÃO DA BARRA INFERIOR (UX MOBILE-FIRST) ---
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : BottomNavItem("inventory", "Estoque", Icons.Rounded.Inventory2)
    object Marketplace : BottomNavItem("marketplace", "Feira", Icons.Rounded.Storefront)
    object Dashboard : BottomNavItem("dashboard", "Finanças", Icons.Rounded.Analytics)
    object Community : BottomNavItem("community", "Rede", Icons.Rounded.Forum)
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Inventory,
        BottomNavItem.Marketplace,
        BottomNavItem.Dashboard,
        BottomNavItem.Community
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Não mostramos a barra inferior nas telas de formulário para focar a atenção do produtor
    val hideBottomBarRoutes = listOf("add_product")
    val isCreateAdRoute = currentDestination?.route?.startsWith("create_ad") == true

    if (currentDestination?.route !in hideBottomBarRoutes && !isCreateAdRoute) {
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
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) TerraBarro else Color.Gray
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = if (isSelected) TerraBarro else Color.Gray,
                            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = SolNordeste.copy(alpha = 0.2f)
                    ),
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}