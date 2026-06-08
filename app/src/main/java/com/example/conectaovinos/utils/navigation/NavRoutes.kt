package com.example.conectaovinos.utils.navigation

sealed class NavRoutes(val route: String) {
    // Telas sem argumentos
    object Selection : NavRoutes("selection")
    object Inventory : NavRoutes("inventory")
    object Dashboard : NavRoutes("dashboard")
    object Financial : NavRoutes("financial")
    object AddTransaction : NavRoutes("add_transaction")
    object Community : NavRoutes("community")
    object Marketplace : NavRoutes("marketplace")
    object MarketplaceBuyer : NavRoutes("marketplace_buyer")

    // Rota de Adicionar Produto (Limpo)
    object AddProduct : NavRoutes("add_product")

    // Rota de Editar Produto (Com argumento)
    object EditProduct : NavRoutes("add_product/{animalId}") {
        fun createRoute(animalId: String): String {
            return "add_product/$animalId"
        }
    }

    // Rota de Criar Anúncio (Com argumento)
    object CreateAd : NavRoutes("create_ad/{animalId}") {
        fun createRoute(animalId: String): String {
            return "create_ad/$animalId"
        }
    }
}