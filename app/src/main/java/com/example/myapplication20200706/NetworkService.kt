package com.example.myapplication20200706

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("15083256/v1/uddi:c5d5c5b8-4e92-4adb-9d4f-cf4a59192679")
    fun getList(
        @Query("page") page:Int,
        @Query("numOfRows") pageSize:Int,
        @Query("returnType") returnType:String,
        @Query("serviceKey") apiKey:String?,
    ): Call<PageListModel>
}