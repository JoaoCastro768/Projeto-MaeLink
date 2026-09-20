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
fun ConfirmationScreen(centerId: Int, slot: String, onGoHome: () -> Unit, onOpenStatus: () -> Unit) {
    val center = MockMaeLinkRepository.centers.firstOrNull { it.id == centerId }
        ?: MockMaeLinkRepository.centers.first()

    MaeScaffold(title = "Solicitação enviada") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaeBackground)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Tudo certo por aqui", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Esta tela simula o retorno visual após uma interação importante do usuário.", color = MaeMuted)
            Spacer(modifier = Modifier.height(16.dp))
            InfoCard(
                title = "Solicitação #1001",
                description = "Unidade: ${center.name}\nData simulada: 28/08/2026\nHorário: $slot\nStatus: aguardando confirmação da unidade",
                tag = "Agendado"
            )
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryActionButton("Acompanhar status", onOpenStatus)
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryActionButton("Voltar ao início", onGoHome)
        }
    }
}
