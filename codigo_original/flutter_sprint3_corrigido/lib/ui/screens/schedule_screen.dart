import 'package:flutter/material.dart';

import '../../data/mock_data.dart';
import '../../model/donation_appointment.dart';
import '../../model/milk_bank.dart';
import '../../navigation/app_routes.dart';
import '../../theme/app_theme.dart';
import '../components/primary_button.dart';
import '../components/screen_padding.dart';
import '../components/section_title.dart';

class ScheduleScreen extends StatefulWidget {
  const ScheduleScreen({super.key, this.bank});

  final MilkBank? bank;

  @override
  State<ScheduleScreen> createState() => _ScheduleScreenState();
}

class _ScheduleScreenState extends State<ScheduleScreen> {
  String? _selectedSlot;
  final TextEditingController _addressController = TextEditingController(text: 'Rua Vergueiro, 1000 - São Paulo');

  @override
  void dispose() {
    _addressController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final bank = widget.bank ?? MockData.milkBanks.first;

    return Scaffold(
      appBar: AppBar(title: const Text('Agendamento')),
      body: ScreenPadding(
        child: ListView(
          children: [
            SectionTitle(
              title: 'Escolha o melhor horário',
              subtitle: 'Unidade selecionada: ${bank.name}',
            ),
            Wrap(
              spacing: 10,
              runSpacing: 10,
              children: bank.availableTimes
                  .map(
                    (slot) => ChoiceChip(
                      label: Text(slot),
                      selected: _selectedSlot == slot,
                      onSelected: (_) => setState(() => _selectedSlot = slot),
                    ),
                  )
                  .toList(),
            ),
            const SizedBox(height: 22),
            Text('Endereço para contato/coleta', style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.w900)),
            const SizedBox(height: 10),
            TextField(
              controller: _addressController,
              maxLines: 2,
              decoration: const InputDecoration(
                hintText: 'Informe endereço ou observação para a equipe',
                prefixIcon: Icon(Icons.home_outlined),
              ),
            ),
            const SizedBox(height: 20),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(18),
                child: Row(
                  children: [
                    const Icon(Icons.info_outline, color: AppTheme.primary),
                    const SizedBox(width: 12),
                    Expanded(
                      child: Text(
                        'O agendamento é simulado. Em uma versão real, o banco de leite confirmaria os dados e entraria em contato.',
                        style: Theme.of(context).textTheme.bodyMedium?.copyWith(color: AppTheme.muted),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 20),
            PrimaryButton(
              label: 'Confirmar solicitação',
              icon: Icons.check_circle_outline,
              onPressed: _selectedSlot == null
                  ? null
                  : () {
                      final appointment = DonationAppointment(
                        bank: bank,
                        slot: _selectedSlot!,
                        addressHint: _addressController.text,
                      );
                      Navigator.pushNamed(context, AppRoutes.confirmation, arguments: appointment);
                    },
            ),
          ],
        ),
      ),
    );
  }
}
