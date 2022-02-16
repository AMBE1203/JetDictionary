package com.example.jetdictionary.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetdictionary.domain.model.LoginResponse

@Database(entities = [LoginResponse::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val DB_NAME = "JetDictionaryDatabase.db"
    }
}