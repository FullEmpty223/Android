package com.umc.anddeul.invite

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.anddeul.databinding.ActivityCreateGroupCodeBinding

class CreateGroupCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGroupCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateGroupCodeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //// 그룹 생성 세팅
        binding.groupCode.text = intent.getStringExtra("FAMILY_GROUP_CODE")
        binding.groupExplainTv.text = "\'${intent.getStringExtra("FAMILY_GROUP_NAME")}\'\n그룹 초대코드"

        //// 카카오톡으로 전송하기
        binding.sendCodeBtn.setOnClickListener {
        }
        
        //// 복사하기
        binding.copyCodeBtn.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("가족코드", binding.groupCode.text.toString())
            clipboard.setPrimaryClip(clip)
        }

    }
}