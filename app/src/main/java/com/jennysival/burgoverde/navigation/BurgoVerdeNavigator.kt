package com.jennysival.burgoverde.navigation

interface BurgoVerdeNavigator {

    fun navigateToHome(actionId: Int, message: String?)

    fun navigateToLogin(actionId: Int)

    fun navigateToRegistration(actionId: Int)

}
