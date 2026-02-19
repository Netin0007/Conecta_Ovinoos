package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.conectaovinos.ui.theme.*
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(navController: NavController) {
    val activeAds = rebanhoGlobal.filterIsInstance<Animal>().take(2)

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text("OS MEUS ANÚNCIOS", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(BottomNavScreen.Inventory.route) },
                containerColor = SolNordeste,
                contentColor = TextoPrincipal,
                icon = { Icon(Icons.Default.Add, contentDescription = "Novo") },
                text = { Text("Vender Artigo", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(activeAds) { animal ->
                    AdItemCard(animal = animal)
                }
            }
        }
    }
}

@Composable
fun AdItemCard(animal: Animal) {
    val precoSimulado = animal.custo * 1.5

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEBE1))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(animal.nome.uppercase(), fontWeight = FontWeight.Black, color = TextoPrincipal)
                Surface(color = VerdeCaatinga, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        "ATIVO",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(50.dp).background(CinzaAreia, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🐑", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Preço no Mercado", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        NumberFormat.getCurrencyInstance(Locale("pt", "PT")).format(precoSimulado),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = SolNordeste
                    )
                }
                OutlinedButton(
                    onClick = { /* Lógica para editar a implementar no futuro */ },
                    border = androidx.compose.foundation.BorderStroke(1.dp, TerraBarro),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TerraBarro),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("EDITAR", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}