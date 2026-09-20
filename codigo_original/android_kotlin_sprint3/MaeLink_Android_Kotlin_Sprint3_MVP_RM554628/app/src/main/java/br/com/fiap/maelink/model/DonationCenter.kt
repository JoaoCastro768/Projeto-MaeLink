package br.com.fiap.maelink.model

data class DonationCenter(
    val id: Int,
    val name: String,
    val neighborhood: String,
    val address: String,
    val distanceKm: Double,
    val phone: String,
    val openingHours: String,
    val milkNeedLevel: MilkNeedLevel,
    val description: String,
    val acceptsHomePickup: Boolean
)

enum class MilkNeedLevel(val label: String) {
    HIGH("Alta prioridade"),
    MEDIUM("Prioridade moderada"),
    STABLE("Estoque estável")
}
