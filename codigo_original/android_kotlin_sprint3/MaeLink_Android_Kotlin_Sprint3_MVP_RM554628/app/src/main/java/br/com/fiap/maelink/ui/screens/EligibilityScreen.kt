package br.com.fiap.maelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import br.com.fiap.maelink.ui.components.InfoCard
import br.com.fiap.maelink.ui.components.MaeScaffold
import br.com.fiap.maelink.ui.components.PrimaryActionButton
import br.com.fiap.maelink.ui.theme.MaeBackground
import br.com.fiap.maelink.ui.theme.MaeMuted

@Composable
fun EligibilityScreen(onBack: () -> Unit, onFindCenters: () -> Unit, onOpenEducation: () -> Unit) {
    var hasSurplus by remember { mutableStateOf(true) }
    var knowsStorage by remember { mutableStateOf(false) }
    var wantsPickup by remember { mutableStateOf(true) }

    MaeScaffold(title = "Triagem rápida", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaeBackground)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(20.dp)
        ) {
            Text("Antes de encaminhar", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Perguntas simples ajudam a orientar a nutriz, sem substituir avaliação profissional.", color = MaeMuted)
            Spacer(modifier = Modifier.height(16.dp))

            EligibilityItem("Tenho leite excedente para doar", hasSurplus) { hasSurplus = it }
            EligibilityItem("Sei como guardar o leite corretamente", knowsStorage) { knowsStorage = it }
            EligibilityItem("Prefiro coleta em casa", wantsPickup) { wantsPickup = it }
            Spacer(modifier = Modifier.height(16.dp))

            val summary = if (hasSurplus) {
                "Perfil inicial compatível para receber orientações e encontrar uma unidade próxima."
            } else {
                "Ainda não há excedente informado. O app sugere conteúdos educativos antes do encaminhamento."
            }
            InfoCard("Resultado da simulação", summary, "MVP")
            Spacer(modifier = Modifier.height(14.dp))
            PrimaryActionButton("Ver unidades indicadas", onFindCenters)
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryActionButton("Ler orientações primeiro", onOpenEducation)
        }
    }
}

@Composable
private fun EligibilityItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
