package com.koru.capital.profile.data.dto

import com.google.gson.annotations.SerializedName


data class UserDataWrapperDto(
    @SerializedName("user")
    val user: UserProfileDto?
)