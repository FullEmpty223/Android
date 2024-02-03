package com.umc.anddeul.checklist.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.umc.anddeul.checklist.ChecklistFragment
import com.umc.anddeul.checklist.model.Root
import com.umc.anddeul.checklist.network.ChecklistInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChecklistService(context : Context) {

    //토큰 가져오기
    val spf: SharedPreferences = context.getSharedPreferences("myToken", Context.MODE_PRIVATE)
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrYWthb19pZCI6WyIzMzA0MTMzMDkzIl0sImlhdCI6MTcwNjY4MzkxMH0.ncVxzwxBVaiMegGD0VU5pI5i9GJjhrU8kUIYtQrSLSg"

    val retrofit = Retrofit.Builder()
        .baseUrl("http://umc-garden.store")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ChecklistInterface::class.java)

    //날짜 구하기 어떻게 할가...


//    val readCallback = object : Callback<Root> {
//        override fun onResponse(call: Call<Root>, response: Response<Root>) {
//            if(response.isSuccessful) {
//                val root: Root? = response.body()
//                //어댑터
//                //어댑터 노티
//            }else {
//                Log.d("apiCallback", "response unsuccessful")
//            }
//        }
//
//        override fun onFailure(call: Call<Root>, t: Throwable) {
//            Log.d("apiCallback", "API CALL Failure ${t.message}")
//        }
//    }
//
//    val readCall: Call<Root> = service.getChecklist(
//        "userid",
//        "2000-01-01"
//    )


}