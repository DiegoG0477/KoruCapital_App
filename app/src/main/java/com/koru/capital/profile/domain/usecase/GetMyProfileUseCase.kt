package com.koru.capital.profile.domain.usecase

import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        // Assumes the repository knows how to get the *current* user's profile
        // This might involve reading a stored token/ID or an interceptor adding auth headers
        return repository.getMyProfile()
    }
}