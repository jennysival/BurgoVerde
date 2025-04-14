package com.jennysival.burgoverde.utils.helper

import android.net.Uri
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import com.jennysival.burgoverde.utils.IMAGE_PICKER_PATH

class ImagePickerHelper(
    caller: ActivityResultCaller,
    private val onImagePicked: (Uri) -> Unit
) {
    private val launcher =
        caller.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onImagePicked(it) }
        }

    fun pickImage() {
        launcher.launch(IMAGE_PICKER_PATH)
    }
}
