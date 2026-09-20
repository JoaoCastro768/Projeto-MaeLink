import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../theme/app_theme.dart';
import '../components/screen_padding.dart';
import '../components/section_title.dart';

class GuideScreen extends StatelessWidget {
  const GuideScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Orientações')),
      body: ScreenPadding(
        child: ListView(
          children: [
            const SectionTitle(
              title: 'Conteúdo educativo',
              subtitle: 'Informações organizadas para reduzir dúvidas e preparar a doadora para o contato com o banco de leite.',
            ),
            ...MockData.guides.map(
              (guide) => Padding(
                padding: const EdgeInsets.only(bottom: 14),
                child: Card(
                  child: Padding(
                    padding: const EdgeInsets.all(18),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(guide.title, style: Theme.of(context).textTheme.titleLarge?.copyWith(fontWeight: FontWeight.w900)),
                        const SizedBox(height: 4),
                        Text(guide.subtitle, style: const TextStyle(color: AppTheme.muted)),
                        const SizedBox(height: 12),
                        ...guide.items.map(
                          (item) => Padding(
                            padding: const EdgeInsets.only(bottom: 8),
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Icon(Icons.favorite, size: 17, color: AppTheme.primary),
                                const SizedBox(width: 8),
                                Expanded(child: Text(item)),
                              ],
                            ),
                          ),
                        ),
                      ],
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
