package com.example.companyfinder

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Company (@PrimaryKey var date_creation: String="r",
                    var Name_enterprise: String="r",
                    var adresse : String="r",
                    var type : String="r",
                    var siret : String="r")