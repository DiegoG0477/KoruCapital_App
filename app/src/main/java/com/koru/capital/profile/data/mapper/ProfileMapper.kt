package com.koru.capital.profile.data.mapper

import com.koru.capital.profile.data.dto.UpdateProfileRequestDto
import com.koru.capital.profile.data.dto.UserProfileDto
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.model.UserProfileUpdate
import java.time.format.DateTimeFormatter // Use if formatting dates
import java.time.ZonedDateTime

fun UserProfileDto.toDomain(): UserProfile {
    // Basic mapping, add error handling or default values as needed
    return UserProfile(
        userId = this.userId,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        profileImageUrl = this.profileImageUrl,
        bio = this.biography,
        linkedInUrl = formatLinkedInUrl(this.linkedinProfile), // Helper to ensure full URL?
        instagramUrl = formatInstagramUrl(this.instagramHandle), // Helper to build URL from handle?
        joinDate = formatJoinDate(this.memberSince) // Format date string if needed
    )
}

// Example helper functions (adapt as needed)
private fun formatLinkedInUrl(urlOrHandle: String?): String? {
    return urlOrHandle // Assume API returns full URL for now, or add logic
}

private fun formatInstagramUrl(handle: String?): String? {
    return handle?.let { "https://instagram.com/$it" } // Example: build URL from handle
}

private fun formatJoinDate(isoDate: String?): String? {
    return try {
        isoDate?.let {
            ZonedDateTime.parse(it).format(DateTimeFormatter.ofPattern("MMMM yyyy")) // e.g., "October 2023"
        }
    } catch (e: Exception) {
        isoDate // Return original string on error
    }
}

// Map Domain Update Model -> Update Request DTO
fun UserProfileUpdate.toRequestDto(): UpdateProfileRequestDto {
    // Handle potential URL/handle conversions if needed
    // val instagramApiField = extractInstagramHandle(this.instagramUrl)
    return UpdateProfileRequestDto(
        firstName = this.firstName,
        lastName = this.lastName,
        biography = this.bio,
        linkedinProfile = this.linkedInUrl,
        instagramHandle = extractInstagramHandle(this.instagramUrl), // Example helper
        profileImageUrl = this.profileImageUrl
    )
}

// Example helper (adapt to your needs)
private fun extractInstagramHandle(url: String?): String? {
    return url?.substringAfterLast('/', "")?.takeIf { it.isNotBlank() }
}
