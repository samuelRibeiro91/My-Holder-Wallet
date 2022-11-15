package com.samuel.myholderwallet.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.samuel.myholderwallet.db.dao.TransactionDAO
import com.samuel.myholderwallet.db.Converters.Converters
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.db.dao.PaperDAO
import com.samuel.myholderwallet.db.dao.WalletDAO
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity


@Database(entities = [PaperEntity::class, BrokerEntity::class, WalletEntity::class, TransactionEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val paperDAO: PaperDAO
    abstract val brokerDAO: BrokerDAO
    abstract val walletDAO: WalletDAO
    abstract val transactionDAO: TransactionDAO


    companion object{
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            synchronized(this){
                var instance: AppDatabase? = INSTANCE

                val migrationAddFactor = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("alter table `transaction` add `factor` INTEGER NOT NULL default 0")
                    }
                }

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java, "walletdatabase"
                    )
                        .addMigrations(migrationAddFactor)
                        .build()

                }

                return instance
            }

        }

    }
}