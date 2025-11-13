package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.conectaovinos.models.Animal

data class Ad(
    val id: String,
    val animal: Animal,
    val price: String,
    val description: String,
    val status: String = "Ativo"
)

val dummyAdList = dummyProductList
    .filterIsInstance<Animal>()
    .take(2)
    .mapIndexed { index, animal ->
        Ad(
            id = "ad${index + 1}",
            animal = animal,
            price = if (animal.raca == "Dorper") "R$ 750,00" else "R$ 680,00",
            description = "Ótimo exemplar da raça ${animal.raca}."
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Anúncios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dummyAdList) { ad ->
                AdListItem(ad = ad)
            }
        }
    }
}

@Composable
fun AdListItem(ad: Ad) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            ProductListItem(product = ad.animal, onClick = {})

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ad.price,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Status: ${ad.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}