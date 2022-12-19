package com.example.myapplication20200706

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication20200706.MyApplication.Companion.cate
import com.example.myapplication20200706.MyApplication.Companion.cyclecount
import com.example.myapplication20200706.databinding.ActivityItemMainBinding
import com.example.myapplication20200706.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.firestore.auth.User

class ItemMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemMainBinding
    lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityItemMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addFab.setOnClickListener{
            if(MyApplication.checkAuth()){
                startActivity(Intent(this, AddActivity::class.java))
            }
            else{
                Toast.makeText(this,"인증진행해주세요..", Toast.LENGTH_SHORT).show()
            }
        }

        if(cate!=null){
            binding.mainRecyclerView.visibility= View.VISIBLE
            makeRecyclerView()

        }
        else{
            binding.mainRecyclerView.visibility= View.GONE
            binding.addFab.visibility=View.VISIBLE
        }

    }
    private fun makeRecyclerView(){
        MyApplication.db.collection("news")
            .get()
            .addOnSuccessListener { result->
                val itemList= mutableListOf<ItemData>()
                for(document in result){
                    val item=document.toObject(ItemData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                }
                adapter=MyAdapter(this,itemList)
                binding.mainRecyclerView.layoutManager= LinearLayoutManager(this)
                binding.mainRecyclerView.adapter= adapter
                if(intent.getStringExtra("notify")=="2"){
                    Log.d("mobileApp","noti")
                    adapter!!.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"서버 데이터 획득 실패",Toast.LENGTH_SHORT).show()
            }

    }

}