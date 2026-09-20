package br.com.fiap.maelink.model

data class DonationAppointment(
    val id: Int,
    val centerName: String,
    val date: String,
    val timeRange: String,
    val status: AppointmentStatus,
    val steps: List<DonationStep>
)

enum class AppointmentStatus(val label: String) {
    SCHEDULED("Coleta agendada"),
    WAITING_CONTACT("Aguardando contato"),
    RECEIVED("Leite recebido"),
    COMPLETED("Doação finalizada")
}

data class DonationStep(
    val title: String,
    val description: String,
    val completed: Boolean
)
