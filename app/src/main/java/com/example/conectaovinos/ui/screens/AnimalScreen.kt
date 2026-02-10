package com.example.conectaovinos.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.conectaovinos.viewmodel.AnimalViewModel


@Composable
fun AnimalScreen(viewModel: AnimalViewModel = viewModel()) {

    val animals by viewModel.animals.collectAsState(initial = emptyList())

    LazyColumn {
        items(animals){
            animal ->
            Text("${animal.nome} - ${animal.raca} - ${animal.idade} - ${animal.preco}")
        }
    }
}