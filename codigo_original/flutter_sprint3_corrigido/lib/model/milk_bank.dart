class MilkBank {
  const MilkBank({
    required this.id,
    required this.name,
    required this.city,
    required this.address,
    required this.distance,
    required this.urgency,
    required this.phone,
    required this.openingHours,
    required this.description,
    required this.services,
    required this.availableTimes,
    required this.stockNeedPercent,
  });

  final String id;
  final String name;
  final String city;
  final String address;
  final String distance;
  final String urgency;
  final String phone;
  final String openingHours;
  final String description;
  final List<String> services;
  final List<String> availableTimes;
  final int stockNeedPercent;
}
