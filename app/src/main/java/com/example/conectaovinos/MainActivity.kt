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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.conectaovinos.database.AppDatabase
import com.example.conectaovinos.database.repository.AnimalRepository
import com.example.conectaovinos.database.repository.ManejoRepository
import com.example.conectaovinos.database.repository.ProdutoRepository
import com.example.conectaovinos.ui.theme.ConectaOvinosTheme
import com.example.conectaovinos.viewmodel.AnimalViewModel
import com.example.conectaovinos.viewmodel.ManejoViewModel
import com.example.conectaovinos.viewmodel.ProdutoViewModel

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

class SimpleViewModelFactory<T : ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        @Suppress("UNCHECKED_CAST")
        return creator() as VM
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

    // DB -> Repository -> ViewModel
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }

    val animalRepository = remember { AnimalRepository(db.AnimalDao()) }
    val produtoRepository = remember { ProdutoRepository(db.ProdutoDao()) }
    val manejoRepository = remember { ManejoRepository(db.ManejoDao()) } // ✅ ADICIONADO

    val animalViewModel: AnimalViewModel = viewModel(
        factory = SimpleViewModelFactory { AnimalViewModel(animalRepository) }
    )

    val produtoViewModel: ProdutoViewModel = viewModel(
        factory = SimpleViewModelFactory { ProdutoViewModel(produtoRepository) }
    )

    val manejoViewModel: ManejoViewModel = viewModel( // ✅ ADICIONADO
        factory = SimpleViewModelFactory { ManejoViewModel(manejoRepository) }
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Inventory.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(BottomNavScreen.Inventory.route) {
                InventoryScreen(navController = navController, animalViewModel = animalViewModel)
            }

            composable(BottomNavScreen.Ads.route) {
                MyAdsScreen(navController = navController, animalViewModel = animalViewModel)
            }

            composable(BottomNavScreen.Dashboard.route) {
                DashboardScreen(navController = navController, animalViewModel = animalViewModel)
            }

            composable("add_product_form") {
                AddProductScreen(
                    navController = navController,
                    animalViewModel = animalViewModel,
                    produtoViewModel = produtoViewModel
                )
            }

            composable("animal_details/{animalId}") { backStackEntry ->
                val animalIdStr = backStackEntry.arguments?.getString("animalId")
                val animalIdInt: Int? = animalIdStr?.toIntOrNull()

                AnimalDetailsScreen(
                    navController = navController,
                    animalId = animalIdInt,
                    animalViewModel = animalViewModel,
                    manejoViewModel = manejoViewModel // ✅ PASSANDO AQUI
                )
            }

            composable("create_ad_form/{animalId}") { backStackEntry ->
                val animalIdStr = backStackEntry.arguments?.getString("animalId")
                val animalIdInt: Int? = animalIdStr?.toIntOrNull()

                CreateAdScreen(
                    navController = navController,
                    animalId = animalIdInt,
                    animalViewModel = animalViewModel
                )
            }
        }
    }
}

// Login
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

// Barra inferior
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

sealed class AppScreen(val route: String) {
    object Authentication : AppScreen("authentication")
    object Main : AppScreen("main")
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    object Inventory : BottomNavScreen("inventory", "Inventário", R.drawable.ic_herd)
    object Ads : BottomNavScreen("ads", "Anúncios", R.drawable.ic_ads)
    object Dashboard : BottomNavScreen("dashboard", "Financeiro", R.drawable.ic_finance)
}
