package com.example.conectaovinos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.models.TipoManejo
import com.example.conectaovinos.viewmodel.AnimalViewModel
import com.example.conectaovinos.viewmodel.ManejoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManejoScreen(
    navController: NavController,
    animalId: Int?,
    manejoViewModel: ManejoViewModel,
    animalViewModel: AnimalViewModel
) {

    var selectedType by remember { mutableStateOf(TipoManejo.Vacinacao) }
    var descricao by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }

    var animal by remember { mutableStateOf<AnimalEntity?>(null) }

    LaunchedEffect(animalId) {
        if (animalId != null) {
            animal = animalViewModel.getAnimalById(animalId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Evento de Manejo") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                "Registrando para: ${animal?.nome ?: "Animal não encontrado"}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Tipo de Manejo:")

            Column(Modifier.selectableGroup()) {
                TipoManejo.values().forEach { tipo ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedType == tipo,
                                onClick = { selectedType = tipo },
                                role = Role.RadioButton
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selectedType == tipo, onClick = null)
                        Text(tipo.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = data,
                onValueChange = { data = it },
                label = { Text("Data") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    if (animalId != null) {
                        manejoViewModel.addManejo(
                            animalId,
                            selectedType.name,
                            descricao,
                            data
                        )
                    }
                    navController.navigateUp()
                }
            ) {
                Text("SALVAR EVENTO")
            }
        }
    }
}

