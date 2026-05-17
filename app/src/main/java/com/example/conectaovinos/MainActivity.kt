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
import com.example.conectaovinos.ui.screens.DashboardScreen
import com.example.conectaovinos.ui.screens.InventoryScreen
// Se você já tiver a tela de Marketplace, descomente a importação abaixo:
// import com.example.conectaovinos.ui.screens.MarketplaceScreen
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
            // AQUI O ERRO FOI CORRIGIDO! As telas agora só pedem o navController
            composable("inventory") { InventoryScreen(navController) }
            composable("add_product") { AddProductScreen(navController) }
            composable("dashboard") { DashboardScreen(navController) }
            composable("community") { CommunityScreen(navController) }

            // Quando for plugar a feira livre, basta descomentar esta linha:
            // composable("marketplace") { MarketplaceScreen(navController) }
        }
    }
}

// --- CONFIGURAÇÃO DA BARRA INFERIOR (UX MOBILE-FIRST) ---
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : BottomNavItem("inventory", "Estoque", Icons.Rounded.Inventory2)
    object Dashboard : BottomNavItem("dashboard", "Finanças", Icons.Rounded.Analytics)
    object Marketplace : BottomNavItem("marketplace", "Feira", Icons.Rounded.Storefront)
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

    NavigationBar(
        containerColor = Color.White,
        contentColor = TerraBarro,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Não mostramos a barra inferior quando o usuário está cadastrando um produto (reduz distração)
        if (currentDestination?.route != "add_product") {
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