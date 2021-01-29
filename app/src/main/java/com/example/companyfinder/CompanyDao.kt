package com.example.companyfinder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CompanyDao {
    @Query("SELECT * FROM Company ORDER BY Name_enterprise")
    fun getAllCompany(): List<Company>

    @Query("SELECT * FROM LocationCompany ORDER BY name")
    fun getAllLocation(): List<LocationCompany>

    @Insert
    fun insert(locationCompany: LocationCompany)

    @Insert
    fun insert(company: Company)
}