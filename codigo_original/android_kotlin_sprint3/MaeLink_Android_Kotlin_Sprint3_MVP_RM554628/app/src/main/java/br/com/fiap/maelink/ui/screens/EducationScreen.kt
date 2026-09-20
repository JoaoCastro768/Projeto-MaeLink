package br.com.fiap.maelink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.maelink.data.MockMaeLinkRepository
import br.com.fiap.maelink.ui.components.InfoCard
import br.com.fiap.maelink.ui.components.MaeScaffold
import br.com.fiap.maelink.ui.components.SectionTitle
import br.com.fiap.maelink.ui.theme.MaeBackground

@Composable
fun EducationScreen(onBack: () -> Unit) {
    MaeScaffold(title = "Orientações", onBack = onBack) { padding ->
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
                    title = "Informação clara para doar com segurança",
                    subtitle = "Conteúdos mockados que representam a central educativa do MãeLink."
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(MockMaeLinkRepository.contents) { content ->
                InfoCard(
                    title = content.title,
                    description = "${content.category} • ${content.readTime}\n${content.summary}",
                    tag = "Ler"
                )
            }
        }
    }
}
