import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../navigation/app_routes.dart';
import '../../theme/app_theme.dart';
import '../components/primary_button.dart';
import '../components/screen_padding.dart';
import '../components/status_pill.dart';

class BankDetailScreen extends StatelessWidget {
  const BankDetailScreen({super.key, required this.bankId});

  final String bankId;

  @override
  Widget build(BuildContext context) {
    final bank = MockData.bankById(bankId);

    return Scaffold(
      appBar: AppBar(title: const Text('Detalhe da unidade')),
      body: ScreenPadding(
        child: ListView(
          children: [
            Card(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    StatusPill(label: bank.urgency, warning: bank.stockNeedPercent > 80),
                    const SizedBox(height: 12),
                    Text(bank.name, style: Theme.of(context).textTheme.headlineSmall?.copyWith(fontWeight: FontWeight.w900)),
                    const SizedBox(height: 8),
                    Text(bank.description, style: const TextStyle(color: AppTheme.muted)),
                    const SizedBox(height: 16),
                    _InfoRow(icon: Icons.location_on_outlined, label: bank.address),
                    _InfoRow(icon: Icons.schedule_outlined, label: bank.openingHours),
                    _InfoRow(icon: Icons.call_outlined, label: bank.phone),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            Text('Serviços disponíveis', style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.w900)),
            const SizedBox(height: 10),
            ...bank.services.map((service) => Card(
                  child: ListTile(
                    leading: const Icon(Icons.check_circle_outline, color: AppTheme.primary),
                    title: Text(service),
                  ),
                )),
            const SizedBox(height: 16),
            Text('Horários mockados', style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.w900)),
            const SizedBox(height: 10),
            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: bank.availableTimes.map((slot) => Chip(label: Text(slot))).toList(),
            ),
            const SizedBox(height: 22),
            PrimaryButton(
              label: 'Agendar doação nesta unidade',
              icon: Icons.calendar_month_outlined,
              onPressed: () => Navigator.pushNamed(context, AppRoutes.schedule, arguments: bank),
            ),
          ],
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({required this.icon, required this.label});

  final IconData icon;
  final String label;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 20, color: AppTheme.primary),
          const SizedBox(width: 10),
          Expanded(child: Text(label)),
        ],
      ),
    );
  }
}
