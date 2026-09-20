package br.com.fiap.maelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.maelink.data.MockMaeLinkRepository
import br.com.fiap.maelink.ui.components.InfoCard
import br.com.fiap.maelink.ui.components.MaeScaffold
import br.com.fiap.maelink.ui.components.PrimaryActionButton
import br.com.fiap.maelink.ui.theme.MaeBackground
import br.com.fiap.maelink.ui.theme.MaeMuted

@Composable
fun ScheduleScreen(centerId: Int, onBack: () -> Unit, onConfirm: (Int) -> Unit) {
    val center = MockMaeLinkRepository.centers.firstOrNull { it.id == centerId }
        ?: MockMaeLinkRepository.centers.first()
    var selectedSlotIndex by remember { mutableStateOf(1) }
    val selectedSlot = MockMaeLinkRepository.timeSlots[selectedSlotIndex]

    MaeScaffold(title = "Agendar atendimento", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaeBackground)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Escolha um horário", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(center.name, color = MaeMuted)
            Spacer(modifier = Modifier.height(16.dp))

            MockMaeLinkRepository.timeSlots.forEachIndexed { index, slot ->
                Row(
                    modifier = Modifier
                        .clickable { selectedSlotIndex = index }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedSlotIndex == index, onClick = { selectedSlotIndex = index })
                    Text(slot, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(
                title = "Resumo",
                description = "Unidade: ${center.neighborhood}\nData simulada: 28/08/2026\nHorário: $selectedSlot",
                tag = "Mock"
            )
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryActionButton("Confirmar solicitação", onClick = { onConfirm(selectedSlotIndex) })
        }
    }
}
