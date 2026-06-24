package com.example.conectaovinos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.conectaovinos.data.local.dao.AnuncioDao
import com.example.conectaovinos.data.local.dao.RebanhoDao
import com.example.conectaovinos.data.local.dao.TransacaoDao
import com.example.conectaovinos.data.local.entity.AnimalEntity
import com.example.conectaovinos.data.local.entity.AnuncioEntity
import com.example.conectaovinos.data.local.entity.ProdutoDerivadoEntity
import com.example.conectaovinos.data.local.entity.TransacaoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Database(
    entities = [AnimalEntity::class, ProdutoDerivadoEntity::class, TransacaoEntity::class, AnuncioEntity::class],
    version = 6, // Incrementado para versão 6 devido à mudança para múltiplas fotos e localização
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rebanhoDao(): RebanhoDao
    abstract fun transacaoDao(): TransacaoDao
    abstract fun anunciodao(): AnuncioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "conecta_ovinos.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private class SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val agora = System.currentTimeMillis()

                    // --- INJETANDO OS NOVOS LOTES COM O SCHEMA ATUALIZADO ---
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("1", 2500.0, agora, "", "Ovino", 10, "Lote Mococa", "BR-100", "Santa Inês", "Fêmea", 450.0, true, null, null, "Tauá, CE")
                    )
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("2", 15000.0, agora, "", "Bovino", 5, "Bezerros Nelore", "BR-200", "Nelore", "Macho", 1200.0, true, null, null, "Tauá, CE")
                    )

                    // --- INJETANDO OS PRODUTOS PROCESSADOS ---
                    database.rebanhoDao().inserirDerivado(
                        ProdutoDerivadoEntity("p1", 75.0, agora, "", "Queijo de Cabra", "Unidade", 5.0, "LOTE-001", null, null, "Tauá, CE")
                    )

                    // --- TRANSAÇÕES FINANCEIRAS ---
                    database.transacaoDao().inserirTransacao(
                        TransacaoEntity("t1", "Venda de Lote Ovino", 7500.0, "Receita", Date().time, "Venda de Animal")
                    )
                    database.transacaoDao().inserirTransacao(
                        TransacaoEntity("t2", "Compra de Ração", 320.0, "Despesa", Date().time, "Insumos")
                    )
                    database.transacaoDao().inserirTransacao(
                        TransacaoEntity("t3", "Venda de 5 Queijos", 125.0, "Receita", Date().time, "Venda de Derivado")
                    )
                }
            }
        }
    }
}
