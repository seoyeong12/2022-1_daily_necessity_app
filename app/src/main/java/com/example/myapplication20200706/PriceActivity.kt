package com.example.myapplication20200706

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication20200706.databinding.ActivityCycleBinding
import com.example.myapplication20200706.databinding.ActivityPriceBinding
import com.example.myapplication20200706.databinding.DialogInputBinding
import com.example.myapplication20200706.databinding.ItemMainBinding

class PriceViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class PriceAdapter(val context: Context, val itemList:MutableList<ItemData>): RecyclerView.Adapter<PriceViewHolder>(){
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return PriceViewHolder(ItemMainBinding.inflate(layoutInflater))
    }
    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val data=itemList.get(position)
        holder.binding.run{
            itemEmailView.text=data.email
            itemDateView.text=data.date.toString()
            itemCategoryView.text=" 카테고리:  "+data.category.toString()
            itemContentView.text="        이름:   "+data.content
            itemCountView.visibility= View.GONE
            itemCycleView.visibility= View.GONE
        }
        holder.binding.root.setOnClickListener{
            val intent =Intent()
            intent.action="ACTION_PRICE2"
            intent.data= Uri.parse("http://www.google.com")
            intent.putExtra("content",data.content)
            holder.binding.root.context.startActivity(intent)
            true
        }
    }
}

class PriceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.db.collection("news")
            .get()
            .addOnSuccessListener { result->
                val itemList= mutableListOf<ItemData>()
                for(document in result){
                    val item=document.toObject(ItemData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                }
                binding.priceRecyclerView.layoutManager= LinearLayoutManager(this)
                binding.priceRecyclerView.adapter= PriceAdapter(this,itemList)
            }
            .addOnFailureListener{
                Toast.makeText(this,"서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }
}