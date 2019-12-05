package id.kuliah.crudkuliah

import kotlinx.android.synthetic.main.activity_manage_fakultas_aktifity.*


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

class ManageFakultasAktifity : AppCompatActivity() {

    lateinit var i: Intent
    lateinit var add:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_fakultas_aktifity)

        add = findViewById(R.id.btnCreate)

        i = intent
        if(i.hasExtra("editmode")) {
            if (i.getStringExtra("editmode").equals("1")) {
                onEditMode()
            }
        }
        add.setOnClickListener{
            onCreate()
        }
    }

    private fun onEditMode(){
        txt_kodefakultas.setText(i.getStringExtra("txt_kodefakultas"))
        txt_namafakultas.setText(i.getStringExtra("txt_namafakultas"))

        btnCreate.visibility = View.GONE
        btnUpdate.visibility = View.VISIBLE
        btnDelete.visibility = View.VISIBLE
    }

    private fun onCreate(){
        val loading = ProgressDialog(this)
        loading.setMessage("Menambahkan Data ..... ")
        loading.show()

        val kode_fakultas = txt_kodefakultas.text.toString()
        val nm_fakultas = txt_namafakultas.text.toString()
        println("==========================================="+kode_fakultas+nm_fakultas)

        AndroidNetworking.post(ApiEndPoint.CREATE)
            .addBodyParameter("kode_fakultas", kode_fakultas)
            .addBodyParameter("nm_fakultas", nm_fakultas)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    loading.dismiss()


                    Toast.makeText(applicationContext,response?.getString("message"), Toast.LENGTH_SHORT).show()

                    if (response?.getString("message")?.contains("successfully")!!){
                            this@ManageFakultasAktifity.finish()
                        }
                }

                override fun onError(anError: ANError?) {
                    loading.dismiss()
                    Log.d("ONERROR", anError?.errorDetail?.toString())
                    Toast.makeText(applicationContext, "Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
