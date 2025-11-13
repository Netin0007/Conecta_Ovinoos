package com.example.conectaovinos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.conectaovinos.models.Animal
import com.example.conectaovinos.models.Produto
import com.example.conectaovinos.models.ProdutoDerivado
import java.text.NumberFormat
import java.util.*

val dummyProductList = listOf<Produto>(
    Animal(id = "1", nome = "Mococa 01", raca = "Santa Inês", dataNascimento = "10/05/2023", custo = 250.0),
    ProdutoDerivado(id = "p1", nome = "Queijo de Cabra Artesanal", unidadeDeMedida = "Peça de 500g", custo = 15.0),
    Animal(id = "2", nome = "Brinco 142", raca = "Dorper", dataNascimento = "02/01/2024", custo = 300.0),
    ProdutoDerivado(id = "p2", nome = "Leite de Ovelha Integral", unidadeDeMedida = "Garrafa de 1L", custo = 5.0),
    Animal(id = "3", nome = "Fumacinha", raca = "SRD", dataNascimento = "25/08/2022", custo = 220.0),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Inventário") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_product_form")
                },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(dummyProductList) { product ->
                ProductListItem(product = product, onClick = {
                    if (product is Animal) {
                        navController.navigate("animal_details/${product.id}")
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListItem(product: Produto, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_herd),
                contentDescription = "Foto de ${product.nome}",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                when (product) {
                    is Animal -> {
                        Text(text = "Raça: ${product.raca}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Nasc: ${product.dataNascimento}", style = MaterialTheme.typography.bodySmall)
                    }
                    is ProdutoDerivado -> {
                        Text(text = "Unidade: ${product.unidadeDeMedida}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("Custo", style = MaterialTheme.typography.labelSmall)
                Text(
                    text = formatCurrency(product.custo),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}