package br.com.fiap.maelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.maelink.ui.components.InfoCard
import br.com.fiap.maelink.ui.components.PrimaryActionButton
import br.com.fiap.maelink.ui.components.SmallMetric
import br.com.fiap.maelink.ui.theme.MaeBackground
import br.com.fiap.maelink.ui.theme.MaeMuted
import br.com.fiap.maelink.ui.theme.MaeTeal

@Composable
fun WelcomeScreen(
    onFindCenters: () -> Unit,
    onStartEligibility: () -> Unit,
    onOpenEducation: () -> Unit,
    onOpenStatus: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaeBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        Text("MãeLink", style = MaterialTheme.typography.headlineLarge, color = MaeTeal, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Conectando mães doadoras, bancos de leite e informação confiável.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaeMuted
        )
        Spacer(modifier = Modifier.height(22.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaeTeal, RoundedCornerShape(28.dp))
                .padding(22.dp)
        ) {
            Text(
                "Sua doação pode chegar onde mais precisa",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Faça uma triagem rápida, encontre unidades próximas e acompanhe o encaminhamento.",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) { SmallMetric("unidades simuladas", "3") }
            androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) { SmallMetric("fluxos principais", "6") }
        }
        Spacer(modifier = Modifier.height(18.dp))
        PrimaryActionButton("Começar triagem", onStartEligibility)
        Spacer(modifier = Modifier.height(10.dp))
        PrimaryActionButton("Encontrar banco de leite", onFindCenters)
        Spacer(modifier = Modifier.height(18.dp))
        InfoCard("Conteúdos", "Orientações rápidas sobre doação, armazenamento e segurança.", "Guia", onOpenEducation)
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard("Acompanhar solicitação", "Veja o status da triagem e do agendamento simulado.", "Status", onOpenStatus)
    }
}
