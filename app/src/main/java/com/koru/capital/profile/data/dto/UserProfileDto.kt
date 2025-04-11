package com.koru.capital.profile.data.dto

import com.google.gson.annotations.SerializedName


data class UserProfileDto(
    @SerializedName("id")
    val userId: String,

    @SerializedName("name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("profile_image_url")
    val profileImageUrl: String?,

    @SerializedName("biography")
    val biography: String?,

    @SerializedName("linkedin_profile")
    val linkedinProfile: String?,

    @SerializedName("instagram_handle")
    val instagramHandle: String?,

    @SerializedName("created_at")
    val memberSince: String?
)