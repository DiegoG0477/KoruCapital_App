package com.koru.capital.profile.data.mapper

import com.koru.capital.profile.data.dto.UpdateProfileRequestDto
import com.koru.capital.profile.data.dto.UserProfileDto
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.model.UserProfileUpdate
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        userId = this.userId,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        profileImageUrl = this.profileImageUrl,
        bio = this.biography,
        linkedInUrl = formatLinkedInUrl(this.linkedinProfile),
        instagramUrl = formatInstagramUrl(this.instagramHandle),
        joinDate = formatJoinDate(this.memberSince)
    )
}

private fun formatLinkedInUrl(urlOrHandle: String?): String? {
    return urlOrHandle
}

private fun formatInstagramUrl(handle: String?): String? {
    return handle?.let { "https://instagram.com/$it" } // Example: build URL from handle
}

private fun formatJoinDate(isoDate: String?): String? {
    return try {
        isoDate?.let {
            ZonedDateTime.parse(it).format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        }
    } catch (e: Exception) {
        isoDate
    }
}

fun UserProfileUpdate.toRequestDto(): UpdateProfileRequestDto {
    return UpdateProfileRequestDto(
        firstName = this.firstName,
        lastName = this.lastName,
        biography = this.bio,
        linkedinProfile = this.linkedInUrl,
        instagramHandle = extractInstagramHandle(this.instagramUrl),
        profileImageUrl = this.profileImageUrl
    )
}

private fun extractInstagramHandle(url: String?): String? {
    return url?.substringAfterLast('/', "")?.takeIf { it.isNotBlank() }
}
