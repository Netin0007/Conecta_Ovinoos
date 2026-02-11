package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.viewmodel.AnimalViewModel
import com.example.conectaovinos.viewmodel.ManejoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailsScreen(
    navController: NavController,
    animalId: Int?,
    animalViewModel: AnimalViewModel,
    manejoViewModel: ManejoViewModel
){
    // ✅ se vier nulo, não tenta consultar Room
    val id = animalId
    if (id == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalhes do Animal") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("ID do animal inválido.")
            }
        }
        return
    }

    // 🔹 Animal do banco
    var animal by remember { mutableStateOf<AnimalEntity?>(null) }

    // 🔹 Lista de manejos do banco (agora com Int certo)
    val manejos by manejoViewModel
        .getManejosByAnimal(id)
        .collectAsState(initial = emptyList())

    // Buscar animal no banco
    LaunchedEffect(id) {
        animal = animalViewModel.getAnimalById(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(animal?.nome ?: "Detalhes do Animal") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        if (animal == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Animal não encontrado no banco")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Botões
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("add_manejo_form/$id") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Registrar Manejo")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { navController.navigate("create_ad_form/$id") },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Anunciar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            Text("Histórico de Manejo", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(manejos) { manejo ->
                    // EventListItem(manejo)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
