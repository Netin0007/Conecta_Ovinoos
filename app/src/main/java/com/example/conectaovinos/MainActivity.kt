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
import com.example.conectaovinos.ui.screens.FinancialScreen
import com.example.conectaovinos.ui.screens.InventoryScreen
import com.example.conectaovinos.ui.screens.MarketplaceScreen
import com.example.conectaovinos.ui.screens.MyAdsScreen
import com.example.conectaovinos.ui.screens.RoleSelectionScreen
import com.example.conectaovinos.ui.screens.AddTransactionScreen
import com.example.conectaovinos.ui.theme.ConectaOvinosTheme
import com.example.conectaovinos.ui.theme.SolNordeste
import com.example.conectaovinos.ui.theme.TerraBarro

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

/**
 * Estrutura Base do Aplicativo (UX Premium com Bottom Navigation)
 */
@Composable
fun ConectaOvinosMainUI() {
    val navController = rememberNavController()
    
    val onSwitchToBuyer = {
        navController.navigate("marketplace_buyer") {
            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
        }
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { innerPadding ->
        // O MAESTRO DA NAVEGAÇÃO
        NavHost(
            navController = navController,
            startDestination = "selection", // Inicia na tela de escolha de perfil
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("selection") { RoleSelectionScreen(navController) }

            composable("inventory") { InventoryScreen(navController, onSwitchToBuyer) }

            // --- ROTAS DO FORMULÁRIO INTELIGENTE ---
            // Abre limpo para Criar
            composable("add_product") { AddProductScreen(navController) }

            // Abre preenchido para Editar
            composable("add_product/{animalId}") { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AddProductScreen(navController, animalId)
            }

            composable("dashboard") { DashboardScreen(navController, onSwitchToBuyer) }
            composable("financial") { FinancialScreen(navController, onSwitchToBuyer) }
            composable("add_transaction") { AddTransactionScreen(navController) }
            composable("community") { CommunityScreen(navController, onSwitchToBuyer) }

            // Tela de Gestão de Anúncios (PRODUTOR)
            composable("marketplace") { MyAdsScreen(navController, onSwitchToBuyer) }

            // Tela de Compra (COMPRADOR)
            composable("marketplace_buyer") {
                MarketplaceScreen(
                    navController = navController,
                    onLogout = {
                        navController.navigate("selection") {
                            popUpTo("selection") { inclusive = true }
                        }
                    },
                    onSwitchToProducer = {
                        navController.navigate("inventory") {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }

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
    object Financial : BottomNavItem("financial", "Finanças", Icons.Rounded.Analytics)
    object Community : BottomNavItem("community", "Rede", Icons.Rounded.Forum)
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Inventory,
        BottomNavItem.Marketplace,
        BottomNavItem.Financial,
        BottomNavItem.Community
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Não mostramos a barra inferior em certas telas para focar a atenção ou mudar o contexto
    val isAddProductRoute = currentDestination?.route?.startsWith("add_product") == true
    val isCreateAdRoute = currentDestination?.route?.startsWith("create_ad") == true
    val isSelectionRoute = currentDestination?.route == "selection"
    val isMarketplaceBuyerRoute = currentDestination?.route == "marketplace_buyer"

    if (!isAddProductRoute && !isCreateAdRoute && !isSelectionRoute && !isMarketplaceBuyerRoute) {
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