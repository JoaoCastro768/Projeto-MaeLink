package br.com.fiap.maelink.navigation

sealed class AppRoute(val route: String) {
    data object Welcome : AppRoute("welcome")
    data object Eligibility : AppRoute("eligibility")
    data object Centers : AppRoute("centers")
    data object CenterDetails : AppRoute("center/{centerId}") {
        fun create(centerId: Int) = "center/$centerId"
    }
    data object Schedule : AppRoute("schedule/{centerId}") {
        fun create(centerId: Int) = "schedule/$centerId"
    }
    data object Confirmation : AppRoute("confirmation/{centerId}/{slotIndex}") {
        fun create(centerId: Int, slotIndex: Int) = "confirmation/$centerId/$slotIndex"
    }
    data object Education : AppRoute("education")
    data object Status : AppRoute("status")
}
