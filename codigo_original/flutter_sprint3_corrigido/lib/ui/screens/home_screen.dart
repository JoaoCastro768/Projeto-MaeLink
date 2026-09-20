import 'package:flutter/material.dart';

import '../../navigation/app_routes.dart';
import '../../theme/app_theme.dart';
import '../components/action_card.dart';
import '../components/screen_padding.dart';
import '../components/section_title.dart';
import '../components/status_pill.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('MãeLink')),
      body: ScreenPadding(
        child: ListView(
          children: [
            Container(
              padding: const EdgeInsets.all(22),
              decoration: BoxDecoration(
                gradient: const LinearGradient(colors: [AppTheme.primary, AppTheme.secondary]),
                borderRadius: BorderRadius.circular(28),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const StatusPill(label: 'MVP Sprint 3'),
                  const SizedBox(height: 16),
                  Text(
                    'Conectando doadoras aos bancos de leite humano',
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(color: Colors.white, fontWeight: FontWeight.w900),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    'Triagem, orientação e agendamento em um fluxo simples e humanizado.',
                    style: Theme.of(context).textTheme.bodyLarge?.copyWith(color: Colors.white.withOpacity(.92)),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 22),
            const SectionTitle(
              title: 'O que você quer fazer?',
              subtitle: 'Fluxos principais implementados com dados mockados.',
            ),
            ActionCard(
              icon: Icons.fact_check_outlined,
              title: 'Fazer pré-triagem',
              description: 'Responda perguntas iniciais para entender o próximo passo.',
              onTap: () => Navigator.pushNamed(context, AppRoutes.screening),
            ),
            const SizedBox(height: 12),
            ActionCard(
              icon: Icons.local_hospital_outlined,
              title: 'Encontrar banco de leite',
              description: 'Veja unidades próximas e escolha a melhor opção.',
              onTap: () => Navigator.pushNamed(context, AppRoutes.banks),
            ),
            const SizedBox(height: 12),
            ActionCard(
              icon: Icons.timeline_outlined,
              title: 'Acompanhar doação',
              description: 'Simule o status de uma doação agendada.',
              onTap: () => Navigator.pushNamed(context, AppRoutes.status),
            ),
            const SizedBox(height: 12),
            ActionCard(
              icon: Icons.menu_book_outlined,
              title: 'Orientações para doar',
              description: 'Cuidados de coleta, armazenamento e encaminhamento.',
              onTap: () => Navigator.pushNamed(context, AppRoutes.guide),
            ),
          ],
        ),
      ),
    );
  }
}
