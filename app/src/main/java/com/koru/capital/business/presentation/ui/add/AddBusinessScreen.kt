// capital/business/presentation/ui/add/AddBusinessScreen.kt
package com.koru.capital.business.presentation.ui.add

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
import com.koru.capital.business.presentation.viewmodel.AddBusinessViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// Helper function to create a temporary image file URI
private fun createImageUri(context: Context): Uri {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    // Use cache directory for temp files
    val storageDir: File = File(context.cacheDir, "images").apply { mkdirs() }
    val imageFile = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg",        /* suffix */
        storageDir     /* directory */
    )
    // Get the content URI using FileProvider
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider", // Match authority in Manifest
        imageFile
    )
}


@OptIn(ExperimentalPermissionsApi::class) // Opt-in for Accompanist Permissions
@Composable
fun AddBusinessScreen(
    onBackClick: () -> Unit,
    viewModel: AddBusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // --- State for temporary camera URI ---
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // --- ActivityResultLaunchers ---
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            // Pass selected URI to ViewModel (can be null if user cancels)
            viewModel.onImageSelected(uri)
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                // Image captured successfully, pass the temp URI to ViewModel
                viewModel.onImageSelected(tempCameraUri)
            } else {
                // Handle failure or cancellation (optional: show message)
                println("Camera capture failed or was cancelled.")
                tempCameraUri = null // Reset temp URI if capture failed
            }
        }
    )

    // --- Permission Handling ---
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // --- LaunchedEffect for Success Navigation ---
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBackClick()
            viewModel.resetFormState()
        }
    }

    // --- UI Content ---
    AddBusinessContent(
        uiState = uiState,
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
            // TODO: Consider adding storage permission check for older Android versions if needed
            galleryLauncher.launch("image/*") // Launch gallery picker
        },
        onCameraClick = {
            if (cameraPermissionState.status.isGranted) {
                // Create temp URI *before* launching camera
                tempCameraUri = createImageUri(context)
                cameraLauncher.launch(tempCameraUri)
            } else {
                // Request permission
                cameraPermissionState.launchPermissionRequest()
                // TODO: Optionally show rationale before requesting or handle permanent denial
            }
        },
        onClearImageClick = {
            viewModel.onImageSelected(null) // Clear image in ViewModel
            tempCameraUri = null // Clear temp camera URI too
        },
        onSubmit = viewModel::submitBusiness
    )
}