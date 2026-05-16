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
    version = 2,
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
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback())  // popula o banco na primeira abertura
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    // Popula o banco com dados de exemplo apenas na primeira vez que o app abre
    private class SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Roda em background para não travar a UI
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("1", "Mococa 01", "Santa Inês", "10/05/2023", 250.0)
                    )
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("2", "Brinco 142", "Dorper", "02/01/2024", 300.0)
                    )
                    database.rebanhoDao().inserirAnimal(
                        AnimalEntity("3", "Fumacinha", "SRD", "25/08/2022", 220.0)
                    )
                    database.rebanhoDao().inserirDerivado(
                        ProdutoDerivadoEntity("p1", "Queijo de Cabra", "Peça de 500g", 15.0)
                    )
                    database.transacaoDao().inserirTransacao(
                        TransacaoEntity("t1", "Venda da Mococa 01", 750.0, "Receita", Date().time, "Venda de Animal")
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