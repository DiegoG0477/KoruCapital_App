package com.koru.capital.core.domain.usecase

import android.net.Uri
import com.koru.capital.core.domain.repository.FileRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    /**
     * Executes the file upload use case.
     * @param fileUri The Uri of the file to upload.
     * @param destinationPath Optional destination path (e.g., "business_images").
     * @return Result containing the URL String on success.
     */
    suspend operator fun invoke(fileUri: Uri, destinationPath: String? = "uploads"): Result<String> {
        // Add validation if needed (e.g., check file size, type - requires ContentResolver)
        return fileRepository.uploadFile(fileUri, destinationPath)
    }
}