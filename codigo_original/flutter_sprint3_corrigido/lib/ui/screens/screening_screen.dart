import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../navigation/app_routes.dart';
import '../../theme/app_theme.dart';
import '../components/primary_button.dart';
import '../components/screen_padding.dart';
import '../components/section_title.dart';

class ScreeningScreen extends StatefulWidget {
  const ScreeningScreen({super.key});

  @override
  State<ScreeningScreen> createState() => _ScreeningScreenState();
}

class _ScreeningScreenState extends State<ScreeningScreen> {
  final Map<String, bool> _answers = {
    for (final question in MockData.screeningQuestions) question.id: false,
  };

  bool get _readyToContinue => _answers.values.every((answer) => answer);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Pré-triagem')),
      body: ScreenPadding(
        child: ListView(
          children: [
            const SectionTitle(
              title: 'Vamos começar com segurança',
              subtitle: 'Esta triagem não substitui a avaliação do banco de leite. Ela apenas organiza o encaminhamento inicial.',
            ),
            ...MockData.screeningQuestions.map(
              (question) => Padding(
                padding: const EdgeInsets.only(bottom: 12),
                child: Card(
                  child: CheckboxListTile(
                    value: _answers[question.id],
                    activeColor: AppTheme.primary,
                    onChanged: (value) => setState(() => _answers[question.id] = value ?? false),
                    title: Text(question.title, style: const TextStyle(fontWeight: FontWeight.w800)),
                    subtitle: Text(question.description),
                    controlAffinity: ListTileControlAffinity.leading,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 8),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(18),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      _readyToContinue ? 'Perfil pronto para encaminhamento' : 'Complete os pontos acima',
                      style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w900),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _readyToContinue
                          ? 'Agora a doadora pode escolher uma unidade e simular o agendamento.'
                          : 'O app só libera o próximo passo quando a doadora confirma os pontos mínimos de orientação.',
                      style: const TextStyle(color: AppTheme.muted),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 18),
            PrimaryButton(
              label: 'Ver bancos de leite próximos',
              icon: Icons.local_hospital_outlined,
              onPressed: _readyToContinue ? () => Navigator.pushNamed(context, AppRoutes.banks) : null,
            ),
          ],
        ),
      ),
    );
  }
}
