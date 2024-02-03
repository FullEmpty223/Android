package com.umc.anddeul.checklist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.anddeul.checklist.model.Root
import com.umc.anddeul.checklist.network.ChecklistInterface
import com.umc.anddeul.databinding.FragmentChecklistBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale

class ChecklistFragment : Fragment() {

    lateinit var binding: FragmentChecklistBinding
    private var checklist = ArrayList<Checklist>()
    private var dateSet = ArrayList<DateSet>()
    val checklistRVAdapter by lazy {
        ChecklistRVAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChecklistBinding.inflate(inflater, container, false)
//        checklist.add(Checklist("체크리스트 내용", "율", "이미지파일", false))
//        checklist.add(Checklist("체크리스트 내용", "도도", "d", false))
//        checklist.add(Checklist("체크리스트 내용", "도도", "d", false))
//        checklist.add(Checklist("체크리스트 내용", "율", "이미지파일", false))
//        checklist.add(Checklist("체크리스트 내용", "도도", "d", false))
//        checklist.add(Checklist("체크리스트 내용", "도도", "d", false))

        //리사이클러뷰 연결
        binding.checklistRecylerView.adapter = checklistRVAdapter
        binding.checklistRecylerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val calendarRVAdapter = CalendarRVAdapter(dateSet)
        binding.calendarRecylerView.adapter = calendarRVAdapter
        binding.calendarRecylerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        //달력 코드
        setListView()

        //현재 날짜
        val dateStamp : String = SimpleDateFormat("MM월 dd일").format(Date())
        binding.checkliTvDate.text = dateStamp


        //토큰 가져오기
        val spf: SharedPreferences = context!!.getSharedPreferences("myToken", Context.MODE_PRIVATE)
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrYWthb19pZCI6WyIzMzA0MTMzMDkzIl0sImlhdCI6MTcwNjY4MzkxMH0.ncVxzwxBVaiMegGD0VU5pI5i9GJjhrU8kUIYtQrSLSg"

        val retrofit = Retrofit.Builder()
            .baseUrl("http://umc-garden.store")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ChecklistInterface::class.java)

        //날짜 구하기 어떻게 할가...


        val readCallback = object : Callback<Root> {
            override fun onResponse(call: Call<Root>, response: Response<Root>) {
                if(response.isSuccessful) {
                    val root: Root? = response.body()
                    checklistRVAdapter.list = root?.result
                    checklistRVAdapter.notifyDataSetChanged()
                }else {
                    Log.d("apiCallback", "response unsuccessful")
                    Log.d("apiCallback", response.errorBody()!!.toString())
                }
            }

            override fun onFailure(call: Call<Root>, t: Throwable) {
                Log.d("apiCallback", "API CALL Failure ${t.message}")
            }
        }

        val readCall : Call<Root> = service.getChecklist(
            "userid",
            "2000-01-01"
        )

        readCall.enqueue(readCallback)


        return binding.root
    }

    private fun setListView() {
        val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("dd"))
        val lastDay = lastDayOfMonth.toString().toInt()

        for (i : Int in 1..lastDay) {
            val date = LocalDate.of(LocalDate.now().year, LocalDate.now().month, i)
            val dayOfWeek: DayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US)
            val day = dayOfWeek.toString()
            var kor_day = "요일"

            when (day) {
                "MONDAY" -> kor_day = "월"
                "TUESDAY" -> kor_day = "화"
                "WEDNESDAY" -> kor_day = "수"
                "THURSDAY" -> kor_day = "목"
                "FRIDAY" -> kor_day = "금"
                "SATURDAY" -> kor_day = "토"
                "SUNDAY" -> kor_day = "일"
            }

            dateSet.add(DateSet(i.toString(), kor_day))
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            // 선택된 이미지 URI 가져오기
//            val selectedImageUri = data?.data
//
//            // URI가 null이 아니면 이미지뷰에 이미지 설정
//            selectedImageUri?.let {
//                // 이미지뷰에 선택된 이미지 설정
//                holder.binding.checkliIvPhoto.setImageURI(it)
//            }
//        }
//    }



}