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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.conectaovinos.ui.screens.*
import com.example.conectaovinos.ui.theme.*

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
            startDestination = "welcome", // FORÇADO: Inicia sempre no portal de escolha
            modifier = Modifier.padding(innerPadding)
        ) {
            // Rota do Portal de Entrada
            composable("welcome") {
                WelcomeScreen(navController = navController)
            }

            // Rotas do Produtor
            composable("inventory") { InventoryScreen(navController) }
            composable("dashboard") { DashboardScreen(navController) }
            composable("community") { CommunityScreen(navController) }
            composable("add_product") { AddProductScreen(navController) }
            composable("add_product/{produtoId}") { backStackEntry ->
                val produtoId = backStackEntry.arguments?.getString("produtoId")
                AddProductScreen(navController, produtoId)
            }

            // Rotas do Marketplace / Anúncios
            composable("marketplace") { MyAdsScreen(navController) }
            composable("create_ad/{produtoId}") { backStackEntry ->
                val produtoId = backStackEntry.arguments?.getString("produtoId")
                CreateAdScreen(navController, produtoId)
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
                .clickable { navController.navigate("marketplace") },
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
    object Marketplace : BottomNavItem("marketplace", "Feira", Icons.Rounded.Storefront)
    object Dashboard : BottomNavItem("dashboard", "Finanças", Icons.Rounded.Analytics)
    object Community : BottomNavItem("community", "Rede", Icons.Rounded.Forum)
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(BottomNavItem.Inventory, BottomNavItem.Marketplace, BottomNavItem.Dashboard, BottomNavItem.Community)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isWelcomeRoute = currentDestination?.route == "welcome"
    val isAddProductRoute = currentDestination?.route?.startsWith("add_product") == true
    val isCreateAdRoute = currentDestination?.route?.startsWith("create_ad") == true

    if (!isWelcomeRoute && !isAddProductRoute && !isCreateAdRoute) {
        NavigationBar(containerColor = Color.White, contentColor = TerraBarro, tonalElevation = 8.dp) {
            items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title, tint = if (isSelected) TerraBarro else Color.Gray) },
                    label = { Text(item.title, color = if (isSelected) TerraBarro else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = SolNordeste.copy(alpha = 0.2f)),
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}