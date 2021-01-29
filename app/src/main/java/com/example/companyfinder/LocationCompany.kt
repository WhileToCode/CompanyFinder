package com.example.companyfinder

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class LocationCompany(@PrimaryKey var siret: String="0",
                           var name: String="0",
                           var departement: String="aucun departement renseigné") : Serializable {

    override fun toString(): String
    {
        return "$name"
    }


}