package com.example.myapplication20200706

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication20200706.R
import com.example.myapplication20200706.databinding.ActivityAddBinding
import java.io.File
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddBinding
    lateinit var filePath:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    val requestLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode===android.app.Activity.RESULT_OK){
            Glide
                .with(applicationContext)
                .load(it.data?.data)
                .apply(RequestOptions().override(250,200))
                .centerCrop()
            val cursor=contentResolver.query(it.data?.data as Uri,
            arrayOf<String>(MediaStore.Images.Media.DATA),null,null,null);
            cursor?.moveToFirst().let{
                filePath=cursor?.getString(0) as String
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId=== R.id.menu_add_save){
            if(binding.count.text.isNotEmpty()&&binding.addEditView.text.isNotEmpty()){
                saveStore()
            }
            else{
                Toast.makeText(this,"데이터가 모두 입력되지 않았습니다.",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun saveStore(){
        val data=mapOf(
            "email" to MyApplication.email,
            "content" to binding.addEditView.text.toString(),
            "date" to Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis.toString(),
            "category" to binding.spinner.selectedItem,
            "count" to binding.count.text.toString(),
            "cycle" to binding.cycle.text.toString()
        )

        MyApplication.db.collection("news")
            .add(data)
            .addOnSuccessListener {
            finish()
            }
            .addOnFailureListener{
                Log.d("mobileApp","data save error")
            }
    }
}