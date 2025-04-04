package com.jennysival.burgoverde.utils

import com.google.android.material.textfield.TextInputLayout

class UserInputValidator {

    fun validateFields(fields: List<Pair<TextInputLayout, String>>): Boolean {
        var isValid = true

        for ((layout, text) in fields) {
            if (text.isEmpty()) {
                layout.error = REQUIRED_FIELD
                isValid = false
            } else {
                layout.error = null
            }
        }

        return isValid
    }
}
