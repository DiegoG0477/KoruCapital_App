package com.koru.capital.profile.domain.usecase

import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return repository.getMyProfile()
    }
}