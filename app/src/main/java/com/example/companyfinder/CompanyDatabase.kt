package com.example.companyfinder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Company::class, LocationCompany::class), version = 1)
abstract class CompanyDatabase: RoomDatabase(){
    abstract fun dao():CompanyDao;

    companion object {
        private var INSTANCE: CompanyDatabase? = null
        @JvmStatic fun getDatabase(context:Context): CompanyDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(context, CompanyDatabase::class.java, "company.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}
