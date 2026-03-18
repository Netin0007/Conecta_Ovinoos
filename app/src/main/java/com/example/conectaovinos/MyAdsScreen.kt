package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.database.DatabaseProvider
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.AnuncioEntity
import com.example.conectaovinos.ui.theme.*
import kotlinx.coroutines.flow.combine
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdsScreen(navController: NavController) {
    val context = LocalContext.current
    val db = DatabaseProvider.get(context)
    
    // Lista combinada de anúncios e dados dos animais
    val adsWithAnimals by produceState<List<Pair<AnuncioEntity, AnimalEntity?>>>(initialValue = emptyList()) {
        combine(db.anuncioDao().getAll(), db.animalDao().getAll()) { anuncios, animais ->
            anuncios.map { anuncio ->
                anuncio to animais.find { it.id == anuncio.animalId }
            }
        }.collect { value = it }
    }

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
        if (adsWithAnimals.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Você não possui anúncios ativos.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(adsWithAnimals) { (anuncio, animal) ->
                    AdItemCard(
                        anuncio = anuncio,
                        animal = animal,
                        onEdit = {
                            navController.navigate("create_ad_form/${anuncio.animalId}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdItemCard(anuncio: AnuncioEntity, animal: AnimalEntity?, onEdit: () -> Unit) {
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
                Text(animal?.nome?.uppercase() ?: "ANIMAL REMOVIDO", fontWeight = FontWeight.Black, color = TextoPrincipal)
                Surface(color = VerdeCaatinga, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        anuncio.status.uppercase(),
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
                    if (animal?.fotoUri != null) {
                        AsyncImage(
                            model = animal.fotoUri,
                            contentDescription = animal.nome,
                            modifier = Modifier.fillMaxSize().background(CinzaAreia, RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("🐑", fontSize = 24.sp)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Preço Anunciado", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(anuncio.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = SolNordeste
                    )
                }
                OutlinedButton(
                    onClick = onEdit,
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