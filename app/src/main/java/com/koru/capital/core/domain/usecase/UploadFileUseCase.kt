package com.koru.capital.core.domain.usecase

import android.net.Uri
import com.koru.capital.core.domain.repository.FileRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileUri: Uri, destinationPath: String? = "uploads"): Result<String> {
        return fileRepository.uploadFile(fileUri, destinationPath)
    }
}