package com.example.conectaovinos

import android.app.Application
import com.example.conectaovinos.data.RebanhoRepository
import com.example.conectaovinos.data.TransacaoRepository
import com.example.conectaovinos.data.local.AppDatabase
import com.example.conectaovinos.data.AnuncioRepository

class ConectaOvinosApp : Application() {

    // Banco e repositories criados uma única vez, vivem enquanto o app viver
    val database by lazy { AppDatabase.getInstance(this) }
    val rebanhoRepository by lazy { RebanhoRepository(database.rebanhoDao()) }
    val transacaoRepository by lazy { TransacaoRepository(database.transacaoDao()) }

    val anuncioRepository by lazy { AnuncioRepository(database.anunciodao()) }
}