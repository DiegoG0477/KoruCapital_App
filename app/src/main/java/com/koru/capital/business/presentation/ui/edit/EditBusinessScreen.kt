// capital/business/presentation/ui/edit/EditBusinessScreen.kt
package com.koru.capital.business.presentation.ui.edit

import android.Manifest // <-- Import Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.* // Keep general imports
import androidx.compose.ui.platform.LocalContext // <-- Import LocalContext
import androidx.core.content.FileProvider // <-- Import FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.* // <-- Import Accompanist Permissions
import com.koru.capital.BuildConfig // <-- Import BuildConfig for authority
import com.koru.capital.business.presentation.ui.add.AddBusinessContent // Reusing Add content
import com.koru.capital.business.presentation.viewmodel.AddBusinessUiState // For mapper
import com.koru.capital.business.presentation.viewmodel.EditBusinessUiState
import com.koru.capital.business.presentation.viewmodel.EditBusinessViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// Helper function to create a temporary image file URI (can be moved to a shared file)
private fun createImageUri(context: Context): Uri {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir: File = File(context.cacheDir, "images").apply { mkdirs() }
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider", // Match authority
        imageFile
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditBusinessScreen(
    businessId: String, // Passed via navigation args
    onBackClick: () -> Unit,
    viewModel: EditBusinessViewModel = hiltViewModel() // ViewModel scoped to this screen
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // --- State for temporary camera URI ---
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // --- ActivityResultLaunchers ---
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            viewModel.onImageSelected(uri) // Update VM state
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                viewModel.onImageSelected(tempCameraUri) // Update VM state with temp URI
            } else {
                tempCameraUri = null // Reset temp URI if failed/cancelled
            }
        }
    )

    // --- Permission Handling ---
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // --- LaunchedEffect for Success Navigation ---
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBackClick()
            viewModel.resetSuccessState()
        }
    }

    // --- UI Content ---
    // Reuse AddBusinessContent, adapt state via the helper function
    AddBusinessContent(
        uiState = uiState.toAddBusinessUiState(), // Map state
        onBackClick = onBackClick,
        onBusinessNameChanged = viewModel::onBusinessNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onInvestmentChanged = viewModel::onInvestmentChanged,
        onProfitChanged = viewModel::onProfitChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onStateSelected = viewModel::onStateSelected,
        onMunicipalitySelected = viewModel::onMunicipalitySelected,
        onBusinessModelChanged = viewModel::onBusinessModelChanged,
        onMonthlyIncomeChanged = viewModel::onMonthlyIncomeChanged,
        // Pass lambdas for image actions
        onGalleryClick = {
            galleryLauncher.launch("image/*")
        },
        onCameraClick = {
            if (cameraPermissionState.status.isGranted) {
                tempCameraUri = createImageUri(context)
                cameraLauncher.launch(tempCameraUri)
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        onClearImageClick = {
            viewModel.onImageSelected(null) // Clear URI in ViewModel
            tempCameraUri = null
        },
        onSubmit = viewModel::submitUpdate // Call update function
        // Pass existingImageUrl to the mapper or modify AddBusinessContent to accept it
    )
}

// Helper function to adapt state (Modify to include existingImageUrl)
private fun EditBusinessUiState.toAddBusinessUiState(): AddBusinessUiState {
    return AddBusinessUiState(
        businessName = this.businessName,
        description = this.description,
        investment = this.investment,
        profitPercentage = this.profitPercentage,
        selectedCategoryId = this.selectedCategoryId,
        selectedStateId = this.selectedStateId,
        selectedMunicipalityId = this.selectedMunicipalityId,
        businessModel = this.businessModel,
        monthlyIncome = this.monthlyIncome,
        selectedImageUri = this.selectedImageUri, // New URI selected by user
        // existingImageUrl = this.existingImageUrl, // <-- Add this if AddBusinessContent is modified
        isLoading = this.isSaving, // Map saving state
        isSuccess = this.isSuccess,
        errorMessage = this.errorMessage,
        states = this.states,
        municipalities = this.municipalities,
        categories = this.categories,
        // Pass loading indicators if AddBusinessContent uses them
        isLoadingStates = this.isLoadingStates,
        isLoadingMunicipalities = this.isLoadingMunicipalities,
        isLoadingCategories = this.isLoadingCategories
    )
}