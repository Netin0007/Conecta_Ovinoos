package com.example.conectaovinos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conectaovinos.ui.theme.*

/**
 * Cartão para exibir dicas em formato de áudio (estilo mensagem de voz).
 * Possui um botão de play com área de toque expandida.
 *
 * @param titulo Assunto principal do áudio.
 * @param autor Nome de quem gravou (ex: Veterinário, Zootecnista).
 * @param duracao Tempo de duração do áudio (ex: "1:45").
 */
@Composable
fun CardAudioDica(titulo: String, autor: String, duracao: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(VerdeCaatinga)
                    .clickable { /* Toca o áudio */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Ouvir", tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
                Text(autor, color = TerraBarro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Áudio • $duracao", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

/**
 * Cartão para exibir dicas rápidas em formato de texto.
 * Permite leitura fácil e compartilhamento rápido para o WhatsApp.
 *
 * @param titulo Assunto principal da dica.
 * @param texto Conteúdo explicativo da dica.
 * @param autor Nome de quem escreveu a dica.
 */
@Composable
fun CardTextoDica(titulo: String, texto: String, autor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextoPrincipal)
            Spacer(modifier = Modifier.height(8.dp))
            Text(texto, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Por $autor", color = TerraBarro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { /* Compartilha no WhatsApp */ }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Filled.Share, contentDescription = "Compartilhar", tint = Color.Gray)
                }
            }
        }
    }
}

/**
 * Cartão para exibir a cotação de mercado atualizada da região.
 * Destaca visualmente se o preço subiu, desceu ou se manteve.
 *
 * @param animal Nome do animal ou categoria (ex: "Ovelha Gorda").
 * @param preco Preço atual da arroba ou kg.
 * @param tendencia Status do mercado (ex: "Subiu", "Estável").
 */
@Composable
fun CardCotacao(animal: String, preco: String, tendencia: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(animal.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = Color.Gray)
                Text(preco, fontWeight = FontWeight.Black, fontSize = 20.sp, color = TextoPrincipal)
            }
            Surface(
                color = if (tendencia.contains("Subiu")) VerdeCaatinga else CinzaAreia,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    tendencia,
                    color = if (tendencia.contains("Subiu")) Color.White else Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}