package com.github.saferoommigration.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.github.saferoommigration.origin.DatabaseHelper.MAIN_DATABASE_DB
import com.github.saferoommigration.origin.DatabaseHelper.PASSW

@Database(entities = [NoteEntity::class], version = 3)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context): NotesDatabase =
                Room.databaseBuilder(context.applicationContext, NotesDatabase::class.java, MAIN_DATABASE_DB)
                        .addMigrations(MIGRATION_2_3)
                        .allowMainThreadQueries()
                        .openHelperFactory(SafeHelperFactory(PASSW.toCharArray()))
                        .build()

        private val MIGRATION_2_3 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                try {
                    database.execSQL("ALTER TABLE Notes RENAME TO Notes_old;")
                    database.execSQL("CREATE TABLE IF NOT EXISTS `Notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL);")
                    database.execSQL("INSERT INTO Notes (id, text, created_at, updated_at) SELECT id, text, timestamp, timestamp FROM Notes_old;")
                    database.execSQL("DROP TABLE IF EXISTS Notes_old;")
                    database.setTransactionSuccessful()
                } finally {
                    database.endTransaction()
                }
            }
        }
    }
}