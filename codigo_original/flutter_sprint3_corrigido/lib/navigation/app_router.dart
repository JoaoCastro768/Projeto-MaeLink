import 'package:flutter/material.dart';

import '../model/donation_appointment.dart';
import '../model/milk_bank.dart';
import '../ui/screens/bank_detail_screen.dart';
import '../ui/screens/bank_list_screen.dart';
import '../ui/screens/confirmation_screen.dart';
import '../ui/screens/guide_screen.dart';
import '../ui/screens/home_screen.dart';
import '../ui/screens/schedule_screen.dart';
import '../ui/screens/screening_screen.dart';
import '../ui/screens/status_screen.dart';
import 'app_routes.dart';

class AppRouter {
  static Route<dynamic> generateRoute(RouteSettings settings) {
    switch (settings.name) {
      case AppRoutes.home:
        return MaterialPageRoute(builder: (_) => const HomeScreen());
      case AppRoutes.screening:
        return MaterialPageRoute(builder: (_) => const ScreeningScreen());
      case AppRoutes.banks:
        return MaterialPageRoute(builder: (_) => const BankListScreen());
      case AppRoutes.bankDetail:
        final bankId = settings.arguments as String?;
        return MaterialPageRoute(
          builder: (_) => BankDetailScreen(bankId: bankId ?? 'blh-001'),
        );
      case AppRoutes.schedule:
        final bank = settings.arguments as MilkBank?;
        return MaterialPageRoute(builder: (_) => ScheduleScreen(bank: bank));
      case AppRoutes.confirmation:
        final appointment = settings.arguments as DonationAppointment?;
        return MaterialPageRoute(
          builder: (_) => ConfirmationScreen(appointment: appointment),
        );
      case AppRoutes.status:
        return MaterialPageRoute(builder: (_) => const StatusScreen());
      case AppRoutes.guide:
        return MaterialPageRoute(builder: (_) => const GuideScreen());
      default:
        return MaterialPageRoute(builder: (_) => const HomeScreen());
    }
  }
}
