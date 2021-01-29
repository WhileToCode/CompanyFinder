package com.example.companyfinder

import android.os.AsyncTask
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyDetail : AppCompatActivity() {
    inner class QueryCompanyTask(private val service:CompanyService,
                                 private val dao: CompanyDao,
                                 private val layout: RelativeLayout): AsyncTask<LocationCompany, String, Company>()
    {


        fun formatDateFr(date: String):String
        {
            val date_tmp = date.split("-")
            return "${date_tmp[2]}/${date_tmp[1]}/${date_tmp[0]}"
        }

        override fun onPreExecute()
        {
            layout.visibility = View.INVISIBLE

        }

        override fun doInBackground(vararg params: LocationCompany?): Company?
        {
            val query = params[0] ?: return null
            return service.loadCompany(query)
        }

        override fun onPostExecute(result: Company?)
        {
            var date = result?.date_creation
            var num_siret = result?.siret


            layout.findViewById<TextView>(R.id.date_creation).text = String.format(getString(R.string.date_creation), date, result?.date_creation)
            layout.findViewById<TextView>(R.id.nom_entreprise).text =  result?.Name_enterprise
            layout.findViewById<TextView>(R.id.adresse).text =  result?.adresse
            layout.findViewById<TextView>(R.id.type).text = result?.type
            layout.findViewById<TextView>(R.id.Siret).text = String.format(getString(R.string.siret), num_siret, result?.siret)

            layout.visibility = View.VISIBLE

        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_company)
        val db = CompanyDatabase.getDatabase(this)
        val dao = db.dao()
        val svc = CompanyService(dao)
        if (dao.getAllCompany().isEmpty()) {
            QueryCompanyTask(svc, dao, findViewById<RelativeLayout>(R.id.relative)).execute()
        } else {
                android.R.layout.simple_dropdown_item_1line
                dao.getAllCompany()
        }
        val location = intent.getSerializableExtra("siret") as LocationCompany

        val layout = findViewById<RelativeLayout>(R.id.relative)
        QueryCompanyTask(svc, dao, layout).execute(location)
    }
}