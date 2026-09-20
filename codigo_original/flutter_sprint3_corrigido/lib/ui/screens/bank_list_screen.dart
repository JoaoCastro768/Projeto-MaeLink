import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../navigation/app_routes.dart';
import '../../theme/app_theme.dart';
import '../components/screen_padding.dart';
import '../components/section_title.dart';
import '../components/status_pill.dart';

class BankListScreen extends StatelessWidget {
  const BankListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Bancos de leite')),
      body: ScreenPadding(
        child: ListView(
          children: [
            const SectionTitle(
              title: 'Unidades disponíveis',
              subtitle: 'Dados simulados para demonstrar como o MãeLink prioriza distância, demanda e disponibilidade.',
            ),
            ...MockData.milkBanks.map(
              (bank) => Padding(
                padding: const EdgeInsets.only(bottom: 14),
                child: Card(
                  child: InkWell(
                    borderRadius: BorderRadius.circular(22),
                    onTap: () => Navigator.pushNamed(context, AppRoutes.bankDetail, arguments: bank.id),
                    child: Padding(
                      padding: const EdgeInsets.all(18),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            children: [
                              Expanded(child: Text(bank.name, style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w900))),
                              StatusPill(label: '${bank.stockNeedPercent}% demanda', warning: bank.stockNeedPercent > 80),
                            ],
                          ),
                          const SizedBox(height: 8),
                          Text(bank.address, style: const TextStyle(color: AppTheme.muted)),
                          const SizedBox(height: 12),
                          Row(
                            children: [
                              const Icon(Icons.place_outlined, size: 18, color: AppTheme.primary),
                              const SizedBox(width: 6),
                              Text(bank.distance),
                              const Spacer(),
                              Text(bank.urgency, style: const TextStyle(fontWeight: FontWeight.w700)),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
