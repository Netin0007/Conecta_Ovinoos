package com.example.conectaovinos

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.conectaovinos.database.DatabaseProvider
import com.example.conectaovinos.viewmodel.MarketplaceViewModel

@Composable
fun MarketplaceScreenRoute(
    navController: NavController,
    onLogout: () -> Unit = {},
    showOnlyFavorites: Boolean = false
) {
    val context = LocalContext.current
    val db = DatabaseProvider.get(context)

    val vm: MarketplaceViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MarketplaceViewModel(
                    animalDao = db.animalDao(),
                    derivadoDao = db.produtosDao()
                ) as T
            }
        }
    )

    MarketplaceScreen(
        navController = navController,
        viewModel = vm,
        showOnlyFavorites = showOnlyFavorites,
        onLogout = onLogout
    )
}