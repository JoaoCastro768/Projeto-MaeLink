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
import br.com.fiap.maelink.ui.components.PrimaryActionButton
import br.com.fiap.maelink.ui.theme.MaeBackground
import br.com.fiap.maelink.ui.theme.MaeMuted

@Composable
fun CenterDetailsScreen(centerId: Int, onBack: () -> Unit, onSchedule: () -> Unit) {
    val center = MockMaeLinkRepository.centers.firstOrNull { it.id == centerId }
        ?: MockMaeLinkRepository.centers.first()
    val pickupText = if (center.acceptsHomePickup) "Sim, mediante confirmação" else "Não informado para esta unidade"

    MaeScaffold(title = "Detalhes da unidade", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaeBackground)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(center.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(center.description, color = MaeMuted)
            Spacer(modifier = Modifier.height(16.dp))
            InfoCard("Endereço", center.address, "${center.distanceKm} km")
            Spacer(modifier = Modifier.height(10.dp))
            InfoCard("Atendimento", "${center.openingHours}\nTelefone: ${center.phone}", center.milkNeedLevel.label)
            Spacer(modifier = Modifier.height(10.dp))
            InfoCard("Coleta domiciliar", pickupText, "Triagem")
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryActionButton("Simular agendamento", onSchedule)
        }
    }
}
