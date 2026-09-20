import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../model/donation_appointment.dart';
import '../../navigation/app_routes.dart';
import '../../theme/app_theme.dart';
import '../components/primary_button.dart';
import '../components/screen_padding.dart';

class ConfirmationScreen extends StatelessWidget {
  const ConfirmationScreen({super.key, this.appointment});

  final DonationAppointment? appointment;

  @override
  Widget build(BuildContext context) {
    final safeAppointment = appointment ?? DonationAppointment(
      bank: MockData.milkBanks.first,
      slot: MockData.milkBanks.first.availableTimes.first,
      addressHint: 'Endereço informado pela doadora',
    );

    return Scaffold(
      appBar: AppBar(title: const Text('Solicitação enviada')),
      body: ScreenPadding(
        child: ListView(
          children: [
            const SizedBox(height: 24),
            Container(
              width: 86,
              height: 86,
              decoration: BoxDecoration(color: AppTheme.success.withOpacity(.14), shape: BoxShape.circle),
              child: const Icon(Icons.check, color: AppTheme.success, size: 44),
            ),
            const SizedBox(height: 24),
            Text(
              'Agendamento mockado criado com sucesso',
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.w900),
            ),
            const SizedBox(height: 12),
            Text(
              'A equipe do banco de leite entraria em contato para confirmar a doação e orientar os próximos passos.',
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.bodyLarge?.copyWith(color: AppTheme.muted),
            ),
            const SizedBox(height: 24),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(18),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _Line(label: 'Unidade', value: safeAppointment.bank.name),
                    _Line(label: 'Horário', value: safeAppointment.slot),
                    _Line(label: 'Referência', value: safeAppointment.addressHint),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),
            PrimaryButton(
              label: 'Acompanhar status da doação',
              icon: Icons.timeline_outlined,
              onPressed: () => Navigator.pushNamedAndRemoveUntil(context, AppRoutes.status, (route) => route.settings.name == AppRoutes.home),
            ),
            const SizedBox(height: 10),
            TextButton(
              onPressed: () => Navigator.pushNamedAndRemoveUntil(context, AppRoutes.home, (route) => false),
              child: const Text('Voltar ao início'),
            ),
          ],
        ),
      ),
    );
  }
}

class _Line extends StatelessWidget {
  const _Line({required this.label, required this.value});

  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: const TextStyle(color: AppTheme.muted, fontSize: 12)),
          const SizedBox(height: 2),
          Text(value, style: const TextStyle(fontWeight: FontWeight.w800)),
        ],
      ),
    );
  }
}
