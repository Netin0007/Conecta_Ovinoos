package com.example.conectaovinos.data

import android.content.Context
import com.example.conectaovinos.data.local.AppDatabase
import com.example.conectaovinos.data.local.dao.AnuncioDao
import com.example.conectaovinos.data.local.dao.RebanhoDao
import com.example.conectaovinos.data.local.dao.TransacaoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        // Substitui o antigo: AppDatabase.getInstance(this)
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideRebanhoDao(database: AppDatabase): RebanhoDao {
        return database.rebanhoDao()
    }

    @Provides
    @Singleton
    fun provideTransacaoDao(database: AppDatabase): TransacaoDao {
        return database.transacaoDao()
    }

    @Provides
    @Singleton
    fun provideAnuncioDao(database: AppDatabase): AnuncioDao {
        return database.anuncioDao()
    }
}