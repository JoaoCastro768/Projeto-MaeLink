import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../theme/app_theme.dart';
import '../components/screen_padding.dart';
import '../components/section_title.dart';

class StatusScreen extends StatelessWidget {
  const StatusScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Acompanhamento')),
      body: ScreenPadding(
        child: ListView(
          children: [
            const SectionTitle(
              title: 'Status da doação',
              subtitle: 'Linha do tempo simulada para demonstrar o acompanhamento do fluxo.',
            ),
            ...MockData.donationSteps.asMap().entries.map((entry) {
              final index = entry.key;
              final step = entry.value;
              return Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Column(
                    children: [
                      Container(
                        width: 32,
                        height: 32,
                        decoration: BoxDecoration(
                          color: step.done ? AppTheme.primary : Colors.white,
                          shape: BoxShape.circle,
                          border: Border.all(color: AppTheme.primary),
                        ),
                        child: Icon(step.done ? Icons.check : Icons.more_horiz, color: step.done ? Colors.white : AppTheme.primary, size: 18),
                      ),
                      if (index < MockData.donationSteps.length - 1)
                        Container(width: 2, height: 66, color: AppTheme.primary.withOpacity(.25)),
                    ],
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.only(bottom: 18),
                      child: Card(
                        child: Padding(
                          padding: const EdgeInsets.all(16),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(step.title, style: const TextStyle(fontWeight: FontWeight.w900)),
                              const SizedBox(height: 4),
                              Text(step.description, style: const TextStyle(color: AppTheme.muted)),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              );
            }),
          ],
        ),
      ),
    );
  }
}
