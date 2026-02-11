package com.example.conectaovinos

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.ProdutoEntity
import com.example.conectaovinos.utils.copyImageToInternalStorage
import com.example.conectaovinos.viewmodel.AnimalViewModel
import com.example.conectaovinos.viewmodel.ProdutoViewModel

enum class ProductType { Animal, Derivado }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    animalViewModel: AnimalViewModel,
    produtoViewModel: ProdutoViewModel
) {
    val context = LocalContext.current

    var selectedType by remember { mutableStateOf(ProductType.Animal) }

    var nome by remember { mutableStateOf("") }
    var custo by remember { mutableStateOf("") }

    // Animal
    var raca by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }

    // ✅ agora vamos guardar a URI FINAL (file:///...) do armazenamento interno
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    // Derivado
    var unidade by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }

    // Picker (Android 13+)
    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val savedPath = copyImageToInternalStorage(context, uri) // ✅ copia
            fotoUri = savedPath?.let { Uri.parse(it) }               // ✅ vira file:///...
        }
    }

    // Fallback (qualquer Android)
    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val savedPath = copyImageToInternalStorage(context, uri) // ✅ copia
            fotoUri = savedPath?.let { Uri.parse(it) }               // ✅ vira file:///...
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Produto") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Qual o tipo de produto?", style = MaterialTheme.typography.titleMedium)

            Row(Modifier.selectableGroup()) {
                ProductType.values().forEach { productType ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (selectedType == productType),
                                onClick = { selectedType = productType },
                                role = Role.RadioButton
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selectedType == productType, onClick = null)
                        Text(productType.name)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome ou Nº de Identificação") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = custo,
                onValueChange = { custo = it },
                label = { Text("Custo / Preço (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (selectedType == ProductType.Animal) {
                OutlinedTextField(
                    value = raca,
                    onValueChange = { raca = it },
                    label = { Text("Raça") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = idade,
                    onValueChange = { idade = it },
                    label = { Text("Idade (anos)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))
                Text("Foto do animal", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                if (fotoUri != null) {
                    AsyncImage(
                        model = fotoUri,
                        contentDescription = "Foto do animal",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                } else {
                    Text("Nenhuma foto selecionada.")
                    Spacer(Modifier.height(8.dp))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // tenta Photo Picker (Android 13+)
                            try {
                                pickPhotoLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } catch (_: Exception) {
                                // fallback
                                getContentLauncher.launch("image/*")
                            }
                        }
                    ) {
                        Text("ESCOLHER FOTO")
                    }

                    if (fotoUri != null) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { fotoUri = null }
                        ) {
                            Text("REMOVER")
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = unidade,
                    onValueChange = { unidade = it },
                    label = { Text("Unidade de Medida (ex: kg, L, un)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = quantidade,
                    onValueChange = { quantidade = it },
                    label = { Text("Quantidade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val preco = custo.toDoubleOrNull() ?: 0.0

                    if (selectedType == ProductType.Animal) {
                        val idadeInt = idade.toIntOrNull() ?: 0

                        val animal = AnimalEntity(
                            nome = nome,
                            raca = raca,
                            idade = idadeInt,
                            preco = preco,
                            fotoUri = fotoUri?.toString() // ✅ agora é file:///... persistente
                        )
                        animalViewModel.addAnimal(animal)
                    } else {
                        val qtdInt = quantidade.toIntOrNull() ?: 0

                        val produto = ProdutoEntity(
                            quantidade = qtdInt,
                            nome = nome,
                            preco = preco,
                            unidade = unidade
                        )
                        produtoViewModel.addProduto(produto)
                    }

                    navController.navigateUp()
                }
            ) {
                Text("SALVAR PRODUTO")
            }
        }
    }
}
