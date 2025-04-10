package com.jennysival.burgoverde.utils.helper

import android.content.Context
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.jennysival.burgoverde.R

class UserInputValidator(private val context: Context) {

    fun validateFields(
        fields: List<Pair<TextInputLayout, String>>,
        passwordLayout: TextInputLayout? = null,
        confirmPasswordLayout: TextInputLayout? = null,
        validations: List<Pair<TextInputLayout, (String) -> Boolean>> = emptyList()
    ): Boolean {
        val emptyFieldsAreValid = validateEmptyFields(fields)
        val emailFieldsAreValid = validateEmailFields(fields, validations)
        val passwordsAreValid = validatePasswords(passwordLayout, confirmPasswordLayout)
        return emptyFieldsAreValid && emailFieldsAreValid && passwordsAreValid
    }

    private fun validateEmptyFields(fields: List<Pair<TextInputLayout, String>>): Boolean {
        var allValid = true

        fields.forEach { (layout, text) ->
            if (text.trim().isEmpty()) {
                layout.error = context.getString(R.string.burgoverde_error_required_field)
                allValid = false
            } else {
                layout.error = null
            }
        }

        return allValid
    }

    private fun validateEmailFields(
        fields: List<Pair<TextInputLayout, String>>,
        validations: List<Pair<TextInputLayout, (String) -> Boolean>>
    ): Boolean {
        var allValid = true

        validations.forEach { (layout, rule) ->
            val value = fields.find { it.first == layout }?.second ?: ""
            if (!rule(value)) {
                layout.error = context.getString(R.string.burgoverde_invalid_email)
                allValid = false
            } else {
                layout.error = null
            }
        }

        return allValid
    }

    private fun validatePasswords(
        passwordLayout: TextInputLayout?,
        confirmPasswordLayout: TextInputLayout?
    ): Boolean {
        if (passwordLayout == null || confirmPasswordLayout == null) return true

        val password = passwordLayout.editText?.text.toString()
        val confirmPassword = confirmPasswordLayout.editText?.text.toString()

        return if (password != confirmPassword) {
            confirmPasswordLayout.error =
                context.getString(R.string.burgoverde_error_password_mismatch)
            false
        } else {
            confirmPasswordLayout.error = null
            true
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
