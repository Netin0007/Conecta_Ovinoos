package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.viewmodel.AnimalViewModel
import java.text.NumberFormat
import java.util.Locale

// Modelo de dados para o Anúncio (agora usando AnimalEntity do Room)
data class Ad(
    val id: String,
    val animal: AnimalEntity,
    val price: Double,
    val description: String,
    val status: String = "Ativo"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(
    navController: NavController,
    animalViewModel: AnimalViewModel
) {
    // 🔹 Pega animais do banco (Room) via Flow
    val animals: List<AnimalEntity> by animalViewModel.animals.collectAsState(initial = emptyList())

    // 🔹 Cria anúncios a partir dos animais cadastrados (exemplo: pega os 2 primeiros)
    val ads: List<Ad> = remember(animals) {
        animals.take(2).mapIndexed { index, animal ->
            Ad(
                id = "ad${index + 1}",
                animal = animal,
                price = animal.preco * 1.4, // margem (ajuste se quiser)
                description = "Excelente exemplar para reprodução."
            )
        }
    }

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
            if (ads.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Você ainda não possui anúncios ativos.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ads) { ad ->
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
        Column(modifier = Modifier.padding(16.dp)) {

            // 🔹 “Header” do anúncio (substitui EnhancedProductListItem)
            Text(
                text = ad.animal.nome,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Raça: ${ad.animal.raca} • Idade: ${ad.animal.idade}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        text = formatCurrency(ad.price),
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

            if (ad.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = ad.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF444444)
                )
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}
