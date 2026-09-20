package br.com.fiap.maelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.maelink.data.MockMaeLinkRepository
import br.com.fiap.maelink.ui.components.InfoCard
import br.com.fiap.maelink.ui.components.MaeScaffold
import br.com.fiap.maelink.ui.components.SectionTitle
import br.com.fiap.maelink.ui.theme.MaeBackground
import br.com.fiap.maelink.ui.theme.MaeMuted

@Composable
fun StatusScreen(onBack: () -> Unit) {
    val appointment = MockMaeLinkRepository.appointment
    MaeScaffold(title = "Acompanhamento", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaeBackground)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            SectionTitle("Status da doação", "Fluxo simulado para demonstrar acompanhamento no MVP.")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(
                title = appointment.status.label,
                description = "${appointment.centerName}\n${appointment.date} • ${appointment.timeRange}",
                tag = "Em andamento"
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text("Linha do tempo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            appointment.steps.forEach { step ->
                val marker = if (step.completed) "✓" else "○"
                InfoCard(
                    title = "$marker ${step.title}",
                    description = step.description,
                    tag = if (step.completed) "Concluído" else "Pendente"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Text("No produto final, esta tela seria alimentada pelos serviços de agendamento e banco de leite.", color = MaeMuted)
        }
    }
}
