package com.example.conectaovinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.conectaovinos.database.DatabaseProvider
import com.example.conectaovinos.database.entities.AnimalEntity
import com.example.conectaovinos.database.entities.AnuncioEntity
import com.example.conectaovinos.ui.theme.*
import com.example.conectaovinos.viewmodel.AnimalDetailsViewModel
import com.example.conectaovinos.viewmodel.AnimalDetailsViewModelFactory
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAdScreen(navController: NavController, animalId: String?) {
    val context = LocalContext.current
    val db = DatabaseProvider.get(context)
    val id = animalId?.toIntOrNull() ?: 0
    val scope = rememberCoroutineScope()

    val viewModel: AnimalDetailsViewModel = viewModel(
        factory = AnimalDetailsViewModelFactory(db.animalDao(), id)
    )

    val animal by viewModel.animal.collectAsState()
    
    var precoVenda by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var anuncioExistente by remember { mutableStateOf<AnuncioEntity?>(null) }

    // Carregar anúncio se já existir
    LaunchedEffect(id) {
        val existing = db.anuncioDao().getByAnimalId(id)
        if (existing != null) {
            anuncioExistente = existing
            precoVenda = existing.price.toString()
            descricao = existing.description
            isEditing = true
        }
    }

    Scaffold(
        containerColor = CinzaAreia,
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "EDITAR ANÚNCIO" else "CRIAR ANÚNCIO", fontWeight = FontWeight.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TerraBarro,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (animal == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TerraBarro)
            }
            return@Scaffold
        }

        val animalData = animal!!

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp).background(CinzaAreia, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                        if (!animalData.fotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = animalData.fotoUri,
                                contentDescription = animalData.nome,
                                modifier = Modifier.fillMaxSize().background(CinzaAreia, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("🐑", fontSize = 32.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(animalData.nome.uppercase(), fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
                        Text("Raça: ${animalData.raca}", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "Custo: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(animalData.preco)}",
                            color = VermelhoBarro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("PREÇO DE VENDA", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precoVenda,
                onValueChange = { precoVenda = it },
                placeholder = { Text("Ex: 500.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                prefix = { Text("R$ ", color = TerraBarro, fontWeight = FontWeight.Bold) }
            )
            val precoDigitado = precoVenda.toDoubleOrNull()
            if (precoDigitado != null) {
                val lucro = precoDigitado - animalData.preco
                Text(
                    "Lucro estimado: ${NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(lucro)}",
                    color = if (lucro > 0) VerdeCaatinga else VermelhoBarro,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("DESCRIÇÃO PARA O COMPRADOR", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TerraBarro,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val valor = precoVenda.toDoubleOrNull() ?: 0.0
                    scope.launch {
                        if (isEditing && anuncioExistente != null) {
                            db.anuncioDao().update(
                                anuncioExistente!!.copy(
                                    price = valor,
                                    description = descricao
                                )
                            )
                        } else {
                            db.anuncioDao().insert(
                                AnuncioEntity(
                                    animalId = id,
                                    price = valor,
                                    description = descricao
                                )
                            )
                        }
                        // CORREÇÃO DA ROTA: Volta para a tela de Anúncios sem quebrar a navegação do Rebanho
                        navController.popBackStack(BottomNavScreen.Inventory.route, false)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SolNordeste, contentColor = TextoPrincipal),
            ) {
                Text(if (isEditing) "ATUALIZAR ANÚNCIO" else "PUBLICAR ANÚNCIO", fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
            
            if (isEditing) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            anuncioExistente?.let { db.anuncioDao().deleteById(it.id) }
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, VermelhoBarro),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VermelhoBarro)
                ) {
                    Text("EXCLUIR ANÚNCIO", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}