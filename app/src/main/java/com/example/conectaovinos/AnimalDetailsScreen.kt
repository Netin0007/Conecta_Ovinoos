package com.example.conectaovinos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.EventoManejo
import com.example.conectaovinos.models.TipoManejo
import java.text.SimpleDateFormat
import java.util.*

val dummyEventList = listOf(
    EventoManejo(id = "e1", tipo = TipoManejo.Vacinacao, data = Date(), descricao = "Vacina Aftosa (Dose 1)", animalId = "1"),
    EventoManejo(id = "e2", tipo = TipoManejo.Pesagem, data = Date(System.currentTimeMillis() - 86400000L * 30), descricao = "Peso: 45 Kg", animalId = "1"),
    EventoManejo(id = "e3", tipo = TipoManejo.Reproducao, data = Date(System.currentTimeMillis() - 86400000L * 60), descricao = "Cobrição com Macho 05", animalId = "1")
)
private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
fun Date.format(): String = sdf.format(this)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailsScreen(navController: NavController, animalId: String?) {
    val animal = dummyProductList.find { it.id == animalId } as? Animal
    val animalEvents = dummyEventList.filter { it.animalId == animalId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = animal?.nome ?: "Detalhes do Animal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (animal == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Animal não encontrado.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            AnimalInfoPanel(animal = animal)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        navController.navigate("add_manejo_form/${animal.id}")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registar Evento")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { navController.navigate("create_ad_form/${animal.id}") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Anunciar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            Text(
                "Histórico de Manejo (RF06)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
                items(animalEvents) { evento ->
                    EventListItem(evento = evento)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
fun AnimalInfoPanel(animal: Animal) { /* ... */ }

@Composable
fun EventListItem(evento: EventoManejo) { /* ... */ }