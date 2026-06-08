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

// Imports das suas telas
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

// Imports do seu tema
import com.example.conectaovinos.ui.theme.ConectaOvinosTheme
import com.example.conectaovinos.ui.theme.SolNordeste
import com.example.conectaovinos.ui.theme.TerraBarro

// Import da nova classe de Rotas (Type-Safe)
import com.example.conectaovinos.utils.navigation.NavRoutes

// IMPORTANTE: Adicione o import do Hilt
import dagger.hilt.android.AndroidEntryPoint

// IMPORTANTE: Adicione a anotação antes da classe
@AndroidEntryPoint
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
        navController.navigate(NavRoutes.MarketplaceBuyer.route) {
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
            startDestination = NavRoutes.Selection.route, // Inicia na tela de escolha de perfil
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.Selection.route) { RoleSelectionScreen(navController) }

            composable(NavRoutes.Inventory.route) { InventoryScreen(navController, onSwitchToBuyer) }

            // --- ROTAS DO FORMULÁRIO INTELIGENTE ---
            // Abre limpo para Criar
            composable(NavRoutes.AddProduct.route) { AddProductScreen(navController) }

            // Abre preenchido para Editar
            composable(NavRoutes.EditProduct.route) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                AddProductScreen(navController, animalId)
            }

            composable(NavRoutes.Dashboard.route) { DashboardScreen(navController, onSwitchToBuyer) }
            composable(NavRoutes.Financial.route) { FinancialScreen(navController, onSwitchToBuyer) }
            composable(NavRoutes.AddTransaction.route) { AddTransactionScreen(navController) }
            composable(NavRoutes.Community.route) { CommunityScreen(navController, onSwitchToBuyer) }

            // Tela de Gestão de Anúncios (PRODUTOR)
            composable(NavRoutes.Marketplace.route) { MyAdsScreen(navController, onSwitchToBuyer) }

            // Tela de Compra (COMPRADOR)
            composable(NavRoutes.MarketplaceBuyer.route) {
                MarketplaceScreen(
                    navController = navController,
                    onLogout = {
                        navController.navigate(NavRoutes.Selection.route) {
                            popUpTo(NavRoutes.Selection.route) { inclusive = true }
                        }
                    },
                    onSwitchToProducer = {
                        navController.navigate(NavRoutes.Inventory.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }

            // Rota inteligente que recebe o ID do animal para a criação do anúncio
            composable(NavRoutes.CreateAd.route) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getString("animalId")
                CreateAdScreen(navController, animalId)
            }
        }
    }
}

// --- CONFIGURAÇÃO DA BARRA INFERIOR (UX MOBILE-FIRST) ---
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inventory : BottomNavItem(NavRoutes.Inventory.route, "Estoque", Icons.Rounded.Inventory2)
    object Marketplace : BottomNavItem(NavRoutes.Marketplace.route, "Feira", Icons.Rounded.Storefront)
    object Financial : BottomNavItem(NavRoutes.Financial.route, "Finanças", Icons.Rounded.Analytics)
    object Community : BottomNavItem(NavRoutes.Community.route, "Rede", Icons.Rounded.Forum)
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
    val isAddProductRoute = currentDestination?.route?.startsWith(NavRoutes.AddProduct.route) == true
    val isCreateAdRoute = currentDestination?.route?.startsWith("create_ad") == true
    val isSelectionRoute = currentDestination?.route == NavRoutes.Selection.route
    val isMarketplaceBuyerRoute = currentDestination?.route == NavRoutes.MarketplaceBuyer.route

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