package com.koru.capital.business.presentation.ui.edit

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.*
import com.koru.capital.BuildConfig
import com.koru.capital.business.presentation.ui.add.AddBusinessContent
import com.koru.capital.business.presentation.viewmodel.AddBusinessUiState
import com.koru.capital.business.presentation.viewmodel.EditBusinessUiState
import com.koru.capital.business.presentation.viewmodel.EditBusinessViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private fun createImageUri(context: Context): Uri? {
    return try {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File = File(context.cacheDir, "temp_images").apply { mkdirs() }
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", imageFile)
    } catch (e: Exception) {
        println("Error creating temp image URI: ${e.message}")
        null
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditBusinessScreen(
    businessId: String,
    onBackClick: () -> Unit,
    viewModel: EditBusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> viewModel.onImageSelected(uri) }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) { viewModel.onImageSelected(tempCameraUri) }
            else { tempCameraUri = null }
        }
    )

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { isGranted ->
        if (isGranted) {
            tempCameraUri = createImageUri(context)
            tempCameraUri?.let { cameraLauncher.launch(it) }
                ?: Toast.makeText(context, "Error al preparar la cámara", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Negocio actualizado", Toast.LENGTH_SHORT).show()
            onBackClick()
            viewModel.resetSuccessState()
        }
    }

    AddBusinessContent(
        uiState = uiState.toAddBusinessUiState(),
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
        onLaunchGallery = { galleryLauncher.launch("image/*") },
        onLaunchCamera = {
            if (cameraPermissionState.status.isGranted) {
                tempCameraUri = createImageUri(context)
                tempCameraUri?.let { cameraLauncher.launch(it) }
                    ?: Toast.makeText(context, "Error al preparar la cámara", Toast.LENGTH_SHORT).show()
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        onClearImage = { viewModel.onImageSelected(null); tempCameraUri = null },
        onSubmit = viewModel::submitUpdate
    )
}

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
        selectedImageUri = this.selectedImageUri,
        isLoading = this.isSaving || this.isLoading,
        isSuccess = this.isSuccess,
        errorMessage = this.errorMessage,
        states = this.states,
        municipalities = this.municipalities,
        categories = this.categories,
        isLoadingStates = this.isLoadingStates,
        isLoadingMunicipalities = this.isLoadingMunicipalities,
        isLoadingCategories = this.isLoadingCategories,
        isLoadingCountries = false
    )
}