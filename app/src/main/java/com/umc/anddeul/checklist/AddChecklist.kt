package com.umc.anddeul.checklist

data class AddChecklist (
    val sender_idx : String,
    val receiver_dix : String,
    val due_year : Int,
    val due_month : Int,
    val due_day : Int,
    val content : String
)