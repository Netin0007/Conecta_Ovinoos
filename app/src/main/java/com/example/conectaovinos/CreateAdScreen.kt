package com.example.conectaovinos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.viewmodel.AnimalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(
    navController: NavController,
    animalId: Int?,
    animalViewModel: AnimalViewModel
) {

    var animal by remember { mutableStateOf<AnimalEntity?>(null) }

    // 🔹 Busca animal no banco
    LaunchedEffect(animalId) {
        if (animalId != null) {
            animal = animalViewModel.getAnimalById(animalId)
        }
    }

    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anunciar no Mercado", fontWeight = FontWeight.Bold) },
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

        // 🔴 Caso animal não exista
        if (animal == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding)) {
                Text("Animal não encontrado.", modifier = Modifier.padding(16.dp))
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Você está anunciando:",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 🔹 Painel com dados reais do banco
//            AnimalInfoPanel(animal!!)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Detalhes da Venda",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Preço de Venda (R$)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("R$ ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição para o comprador") },
                placeholder = { Text("Ex: Animal saudável, vacinado, bem tratado...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // 🔹 Aqui depois você salva anúncio no banco
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("PUBLICAR ANÚNCIO", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
