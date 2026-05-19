package com.example.conectaovinos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
    version = 3, // Truque Ninja: Versão 3 força a recriação limpa do banco de dados!
    exportSchema = false
)
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
                    .fallbackToDestructiveMigration() // Se a versão mudar, ele apaga o velho e recria
                    .addCallback(SeedCallback())  // popula o banco na primeira abertura
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    // Popula o banco com dados de exemplo alinhados com o novo chassi da Fazenda
    private class SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Roda em background para não travar a UI
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val agora = System.currentTimeMillis()

                    // --- INJETANDO OS NOVOS LOTES ---
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("1", 2500.0, agora, "Ovino", 10)
                    )
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("2", 15000.0, agora, "Bovino", 5)
                    )
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("3", 3300.0, agora, "Caprino", 15)
                    )

                    // --- INJETANDO OS PRODUTOS PROCESSADOS ---
                    database.rebanhoDao().inserirDerivado(
                        ProdutoDerivadoEntity("p1", 75.0, agora, "Queijo de Cabra", "Unidade", 5.0)
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
                    database.transacaoDao().inserirTransacao(
                        TransacaoEntity("t4", "Medicamentos (Vermífugo)", 80.0, "Despesa", Date().time, "Saúde")
                    )
                }
            }
        }
    }
}