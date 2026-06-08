package com.example.conectaovinos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
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
    version = 3,
    exportSchema = true // ✅ 1. Mudado para true para salvar o histórico do banco
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rebanhoDao(): RebanhoDao
    abstract fun transacaoDao(): TransacaoDao
    abstract fun anuncioDao(): AnuncioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // ✅ 2. Criando a regra de migração da versão 2 para a 3
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // ATENÇÃO: Se as suas classes AnimalEntity ou ProdutoDerivadoEntity tiverem
                // um @Entity(tableName = "nome_customizado"), mude os nomes abaixo para bater com o nome customizado.
                // Caso contrário, o Room usa o próprio nome da classe como nome da tabela.

                // Adicionando espécie e quantidade na tabela de animais
                database.execSQL("ALTER TABLE AnimalEntity ADD COLUMN especie TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE AnimalEntity ADD COLUMN quantidade INTEGER NOT NULL DEFAULT 0")

                // Adicionando unidade de medida na tabela de produtos
                database.execSQL("ALTER TABLE ProdutoDerivadoEntity ADD COLUMN unidadeMedida TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "conecta_ovinos.db"
                )
                    // ✅ 3. Adicionamos a migração e removemos o fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_2_3)
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