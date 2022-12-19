package com.example.myapplication20200706

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication20200706.MyApplication.Companion.networkService
import com.example.myapplication20200706.databinding.ActivityPrice2Binding
import com.example.myapplication20200706.databinding.ActivityPriceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class PriceActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityPrice2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val cont=getIntent().getStringExtra("content")
        var page:Int ?=16000
            when(cont){
                "라면"->page=1500
                "콜라"->page=6000
                "욕실세정제"->page=7000
                "바디워시"->page=8000
                "햇반"->page=10500
                "식용유"->page=13000
                "우유"->page=15200
                "세제"->page=16000
                "지퍼백"->page=16200
                "바디크림"->page=16500
                "샴푸"->page=17000
            }
            val call: Call<PageListModel> = MyApplication.networkService.getList(
                page ?:16000,
                10,
                "json",
                "z+wHZF+QhM3qDDea4/pqyFhpSIVt9PrgBQEZ1AcK4ETLrzBUZKIuFGp1P2T8Pg5LzFilO04bEIO5C0inmJnutA==",
            )

        call?.enqueue(object :Callback<PageListModel>{
            override fun onResponse(call: Call<PageListModel>, response: Response<PageListModel>) {
                if(response.isSuccessful){
                    Log.d("mobileApp","$response")
                    binding.price2RecyclerView.layoutManager=LinearLayoutManager(this@PriceActivity2)
                    binding.price2RecyclerView.adapter=ModelAdapter(this@PriceActivity2,response.body()?.data)
                    binding.price2RecyclerView.addItemDecoration(DividerItemDecoration(this@PriceActivity2, DividerItemDecoration.VERTICAL))
                }
            }
            override fun onFailure(call: Call<PageListModel>, t: Throwable) {
                Log.d("mobileApp","failure")
            }
        })
    }
}