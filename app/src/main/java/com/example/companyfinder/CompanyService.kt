package com.example.companyfinder

import android.util.JsonReader
import android.util.JsonToken
import java.io.IOException
import java.lang.IllegalStateException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class CompanyService(private var dao: CompanyDao) {
    private val apiUrl = "https://entreprise.data.gouv.fr"
    private val queryUrl = "$apiUrl/api/sirene/v1/full_text/%s?page=1&per_page=100"

    private fun readCompany(reader: JsonReader): Company {
        val entreprise = Company("", "", "", "", "")
        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()
            while (reader.hasNext()) {
                val name = reader.nextName()
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue()
                    continue
                }
                when (name) {
                    "nom_raison_sociale" -> entreprise.Name_enterprise =
                        reader.nextString()
                    "geo_adresse" -> entreprise.adresse = reader.nextString()

                    "date_creation" -> entreprise.date_creation = reader.nextString()
                    "libelle_nature_juridique_entreprise" -> entreprise.type =
                        reader.nextString()
                    "siret" -> entreprise.siret = reader.nextString()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
        }
        reader.endArray()
        return entreprise
    }

        fun loadCompany(locationCompany: LocationCompany): Company? {
        var conn: HttpsURLConnection? = null
        val company = Company()
        val siret = locationCompany.siret

        try {
            val url = URL(String.format(queryUrl, siret))
            conn = url.openConnection() as HttpsURLConnection
            //conn.setRequestProperty(this)
            conn.connect()
            val code = conn.responseCode
            if (code != HttpsURLConnection.HTTP_OK) {
                return null
            }
            val inputStream = conn.inputStream ?: return null
            val reader = JsonReader(inputStream.bufferedReader())
            reader.beginObject()
            while (reader.hasNext()) {
                if (reader.nextName() == "etablissement") {

                    try {
                        dao.insert(readCompany(reader))
                    }
                    catch (e: IllegalStateException) {
                        return null
                        }

                }
                else {
                    reader.skipValue()
                }
            }
            reader.endObject()
            return company
        } catch (e: IOException) {
            return company
        } finally {
            conn?.disconnect()
        }
    }


    private fun readLocationCompany(reader: JsonReader): LocationCompany {
        val location = LocationCompany("", "", "")
        reader.beginArray()
        while (reader.hasNext()) {
            reader.beginObject()

            while (reader.hasNext()) {
                val name = reader.nextName()
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue()
                    continue
                }
                when (name) {
                    "siret" -> location.siret = reader.nextString()

                    "nom_raison_sociale" -> location.name = reader.nextString()


                    "departement" -> {
                        if (reader.peek() == JsonToken.NULL) {

                            reader.nextNull()
                        } else {
                            location.departement = reader.nextString()
                        }
                    }
                    else -> reader.skipValue()
                }
            }
            reader.endObject()
        }
        reader.endArray()
        return location
    }

    fun loadLocationCompany(query: String): List<LocationCompany>? {
        var conn: HttpsURLConnection? = null
        try {
            val url = URL(String.format(queryUrl, query))
            conn = url.openConnection() as HttpsURLConnection
            // conn.setRequestProperty(this.queryUrl)
            conn.connect()
            val code = conn.responseCode
            if (code != HttpsURLConnection.HTTP_OK) {
                return emptyList()
            }
            val inputStream = conn.inputStream ?: return emptyList()
            val reader = JsonReader(inputStream.bufferedReader())
            val listLocation = mutableListOf<LocationCompany>()

            reader.beginObject()
            while (reader.hasNext()) {
                if (reader.nextName() == "etablissement") {

                        try {
                            dao.insert(readLocationCompany(reader))
                        } catch (e: IllegalStateException) {
                            return emptyList()
                        }
                }
                else {
                    reader.skipValue()
                }
            }
            reader.endObject()
            return listLocation
        } catch (e: IOException) {
            return emptyList()
        } finally {
            conn?.disconnect()
        }
    }
}
