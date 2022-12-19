package com.example.myapplication20200706

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication20200706.databinding.ItemMainBinding
import com.example.myapplication20200706.databinding.ItemModelBinding

class ModelViewHolder(val binding:ItemModelBinding): RecyclerView.ViewHolder(binding.root)
class ModelAdapter (val context: Context, val datas:MutableList<ItemModel>?):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return datas?.size ?:0
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ModelViewHolder(ItemModelBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as ModelViewHolder).binding
        val model=datas!![position]
        binding.name.text=model.상품명
        binding.price.text=model.판매가격.toString()
        binding.place.text=model.판매업소
    }
}