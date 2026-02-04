package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import java.text.NumberFormat
import java.util.*

// Modelo de dados para o Anúncio
data class Ad(
    val id: String,
    val animal: Animal,
    val price: Double,
    val description: String,
    val status: String = "Ativo"
)

// Simulando anúncios baseados na nossa lista de produtos
val dummyAdList = dummyProductList
    .filterIsInstance<Animal>()
    .take(2)
    .mapIndexed { index, animal ->
        Ad(
            id = "ad${index + 1}",
            animal = animal,
            price = animal.custo * 1.4, // Preço de venda com margem
            description = "Excelente exemplar para reprodução."
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Anúncios", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (dummyAdList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Você ainda não possui anúncios ativos.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(dummyAdList) { ad ->
                        AdListItem(ad = ad)
                    }
                }
            }
        }
    }
}

@Composable
fun AdListItem(ad: Ad) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // A CORREÇÃO: Atualizado para usar o novo componente visual EnhancedProductListItem
            EnhancedProductListItem(product = ad.animal, onClick = {})

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Preço no Mercado",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(ad.price),
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

                Surface(
                    color = Color(0xFFE3F2FD),
                    shape = androidx.compose.foundation.shape.CircleShape
                ) {
                    Text(
                        text = ad.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}