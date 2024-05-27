package com.umc.anddeul.mypage

import android.content.Intent
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.umc.anddeul.MainActivity
import com.umc.anddeul.R
import com.umc.anddeul.common.GalleryManagerActivity
import com.umc.anddeul.common.RetrofitManager
import com.umc.anddeul.common.TokenManager
import com.umc.anddeul.databinding.FragmentMypageBinding
import com.umc.anddeul.home.LoadProfileImage
import com.umc.anddeul.home.UserProfileRVAdapter
import com.umc.anddeul.home.model.UserProfileDTO
import com.umc.anddeul.home.model.UserProfileData
import com.umc.anddeul.home.network.UserProfileInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding
    private val myPageViewModel: MyPageViewModel by activityViewModels()
    var token: String? = null
    lateinit var retrofitBearer: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        token = TokenManager.getToken()
        retrofitBearer = RetrofitManager.getRetrofitInstance()

        binding.mypageSettingIb.setOnClickListener {
            // MyPageSettingFragment로 이동
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .add(R.id.mypage_layout, MypageSettingFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        // 게시글 올리기
        binding.mypageUploadBtn.setOnClickListener {
            val intent = Intent(context, GalleryManagerActivity::class.java)
            startActivity(intent)
        }

        // 프로필 수정하기
        binding.mypageModifyBtn.setOnClickListener {
            // MyPageModifyFragment로 이동
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .add(R.id.main_frm, MyPageModifyFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        loadMyProfile()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadMyProfile()
    }

    // 내 프로필 조회
    fun loadMyProfile() {
        // 내 sns id 가져오기
        val spfMyId = requireActivity().getSharedPreferences("myIdSpf", Context.MODE_PRIVATE)
        val myId = spfMyId.getString("myId", "not found")

        val userProfileService = retrofitBearer.create(UserProfileInterface::class.java)

        if (myId != null) {
            userProfileService.getUserProfile(myId).enqueue(object : Callback<UserProfileDTO> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<UserProfileDTO>,
                    response: Response<UserProfileDTO>
                ) {
                    Log.e("myProfileService response code : ", "${response.code()}")
                    Log.e("myProfileService response body : ", "${response.body()}")

                    if (response.isSuccessful) {
                        val myProfileData = response.body()?.result

                        myProfileData?.let {
                            binding.mypageUsernameTv.text = myProfileData.nickname
                            binding.mypagePostNumTv.text = "게시물 ${myProfileData.postCount}개"

                            val myProfileRVAdapter = UserProfileRVAdapter(
                                myProfileData.firstPostImages,
                                myProfileData.postIdx
                            )

                            binding.mypageProfileRv.layoutManager =
                                GridLayoutManager(requireContext(), 3)
                            binding.mypageProfileRv.adapter = myProfileRVAdapter

                            myProfileRVAdapter.setMyItemClickListener(object :
                                UserProfileRVAdapter.MyItemClickInterface {
                                override fun onItemClick(postIdx: Int) {
                                    // 선택한 게시글 단일 조회
                                    getPost(postIdx)
                                }
                            })

                            val profileImageUrl = myProfileData.image
                            val imageView = binding.mypageProfileIv
                            val loadImage = LoadProfileImage(imageView)
                            loadImage.execute(profileImageUrl)

                            val gson = Gson()
                            val myProfile = myProfileData
                            val myProfileDTO =
                                gson.fromJson(gson.toJson(myProfile), UserProfileData::class.java)
                            myPageViewModel.setMyProfile(myProfileDTO)

                        }
                    }
                }

                override fun onFailure(call: Call<UserProfileDTO>, t: Throwable) {
                    Log.e("myProfileService", "Failure message: ${t.message}")
                }
            })
        }
    }

    fun getPost(postIdx: Int) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .add(R.id.mypage_layout, MyPostFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val postIdxJson = gson.toJson(postIdx)
                    putInt("postIdx", postIdxJson.toInt())
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}