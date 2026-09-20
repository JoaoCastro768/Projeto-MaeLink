import 'milk_bank.dart';

class DonationAppointment {
  const DonationAppointment({
    required this.bank,
    required this.slot,
    required this.addressHint,
  });

  final MilkBank bank;
  final String slot;
  final String addressHint;
}
