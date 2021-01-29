package com.example.companyfinder

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    inner class QueryTask(
            private val svc: CompanyService,
            private val listResult: ListView,
            private val dao: CompanyDao


    ) :
            AsyncTask<String, Void, List<LocationCompany>>() {

        override fun onPreExecute() {

            listResult.visibility = View.INVISIBLE
        }

        override fun doInBackground(vararg params: String?): List<LocationCompany>? {
            val query = params[0] ?: return emptyList()

            return svc.loadLocationCompany(query)
        }

        override fun onPostExecute(result: List<LocationCompany>?) {
            listResult.adapter = ArrayAdapter<LocationCompany>(
                    applicationContext,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    dao.getAllLocation()
                    //result!!
            )
            listResult.visibility = View.VISIBLE

            super.onPostExecute(result)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = findViewById<ListView>(R.id.listResults)
        val db = CompanyDatabase.getDatabase(this)
        val dao = db.dao()
        val svc = CompanyService(dao)


        findViewById<ImageButton>(R.id.imagebuttonsearch).setOnClickListener {
            val equery = nom.text.toString()
            QueryTask(svc, listResults, dao).execute(equery)
        }
        list.setOnItemClickListener { parent, view, position, id ->
            val location = list.getItemAtPosition(position) as LocationCompany


            intent = Intent(this, CompanyDetail::class.java)
            intent.putExtra("siret", location)
            this.startActivity(intent)

        }
    }

}