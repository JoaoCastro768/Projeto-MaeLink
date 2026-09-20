package br.com.fiap.maelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun CentersScreen(onBack: () -> Unit, onCenterSelected: (Int) -> Unit) {
    MaeScaffold(title = "Bancos de leite", onBack = onBack) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaeBackground)
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionTitle(
                    title = "Unidades próximas",
                    subtitle = "Dados simulados para mostrar como o MãeLink orienta o encaminhamento."
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(MockMaeLinkRepository.centers) { center ->
                val pickup = if (center.acceptsHomePickup) " • coleta em casa" else ""
                InfoCard(
                    title = center.name,
                    description = "${center.neighborhood} • ${center.distanceKm} km • ${center.openingHours}$pickup",
                    tag = center.milkNeedLevel.label,
                    onClick = { onCenterSelected(center.id) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Toque em uma unidade para ver detalhes e simular agendamento.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaeMuted,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
