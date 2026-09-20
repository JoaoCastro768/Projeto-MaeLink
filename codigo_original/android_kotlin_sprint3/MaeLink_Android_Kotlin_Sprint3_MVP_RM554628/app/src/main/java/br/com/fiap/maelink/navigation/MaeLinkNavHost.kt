package br.com.fiap.maelink.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.maelink.data.MockMaeLinkRepository
import br.com.fiap.maelink.ui.screens.CenterDetailsScreen
import br.com.fiap.maelink.ui.screens.CentersScreen
import br.com.fiap.maelink.ui.screens.ConfirmationScreen
import br.com.fiap.maelink.ui.screens.EducationScreen
import br.com.fiap.maelink.ui.screens.EligibilityScreen
import br.com.fiap.maelink.ui.screens.ScheduleScreen
import br.com.fiap.maelink.ui.screens.StatusScreen
import br.com.fiap.maelink.ui.screens.WelcomeScreen

@Composable
fun MaeLinkNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Welcome.route
    ) {
        composable(AppRoute.Welcome.route) {
            WelcomeScreen(
                onFindCenters = { navController.navigate(AppRoute.Centers.route) },
                onStartEligibility = { navController.navigate(AppRoute.Eligibility.route) },
                onOpenEducation = { navController.navigate(AppRoute.Education.route) },
                onOpenStatus = { navController.navigate(AppRoute.Status.route) }
            )
        }
        composable(AppRoute.Eligibility.route) {
            EligibilityScreen(
                onBack = { navController.popBackStack() },
                onFindCenters = { navController.navigate(AppRoute.Centers.route) },
                onOpenEducation = { navController.navigate(AppRoute.Education.route) }
            )
        }
        composable(AppRoute.Centers.route) {
            CentersScreen(
                onBack = { navController.popBackStack() },
                onCenterSelected = { centerId -> navController.navigate(AppRoute.CenterDetails.create(centerId)) }
            )
        }
        composable(
            route = AppRoute.CenterDetails.route,
            arguments = listOf(navArgument("centerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val centerId = backStackEntry.arguments?.getInt("centerId") ?: 1
            CenterDetailsScreen(
                centerId = centerId,
                onBack = { navController.popBackStack() },
                onSchedule = { navController.navigate(AppRoute.Schedule.create(centerId)) }
            )
        }
        composable(
            route = AppRoute.Schedule.route,
            arguments = listOf(navArgument("centerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val centerId = backStackEntry.arguments?.getInt("centerId") ?: 1
            ScheduleScreen(
                centerId = centerId,
                onBack = { navController.popBackStack() },
                onConfirm = { slotIndex -> navController.navigate(AppRoute.Confirmation.create(centerId, slotIndex)) }
            )
        }
        composable(
            route = AppRoute.Confirmation.route,
            arguments = listOf(
                navArgument("centerId") { type = NavType.IntType },
                navArgument("slotIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val centerId = backStackEntry.arguments?.getInt("centerId") ?: 1
            val slotIndex = backStackEntry.arguments?.getInt("slotIndex") ?: 1
            val slot = MockMaeLinkRepository.timeSlots.getOrElse(slotIndex) { MockMaeLinkRepository.timeSlots[1] }
            ConfirmationScreen(
                centerId = centerId,
                slot = slot,
                onGoHome = {
                    navController.navigate(AppRoute.Welcome.route) {
                        popUpTo(AppRoute.Welcome.route) { inclusive = true }
                    }
                },
                onOpenStatus = { navController.navigate(AppRoute.Status.route) }
            )
        }
        composable(AppRoute.Education.route) {
            EducationScreen(onBack = { navController.popBackStack() })
        }
        composable(AppRoute.Status.route) {
            StatusScreen(onBack = { navController.popBackStack() })
        }
    }
}
