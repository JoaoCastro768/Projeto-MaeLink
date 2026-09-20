package br.com.fiap.maelink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.fiap.maelink.ui.theme.MaeMuted
import br.com.fiap.maelink.ui.theme.MaeTeal

@Composable
fun PrimaryActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaeTeal)
    ) {
        Text(text = text, modifier = Modifier.padding(vertical = 6.dp))
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaeMuted)
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    description: String,
    tag: String? = null,
    onClick: (() -> Unit)? = null
) {
    val cardModifier = Modifier
        .fillMaxWidth()
        .background(Color.White, RoundedCornerShape(18.dp))
        .border(1.dp, Color(0xFFE6EEEE), RoundedCornerShape(18.dp))
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
        .padding(18.dp)

    Column(modifier = cardModifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (tag != null) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaeTeal,
                    modifier = Modifier
                        .background(Color(0xFFE7F5F2), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaeMuted)
    }
}

@Composable
fun SmallMetric(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE6EEEE), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaeTeal)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaeMuted)
    }
}
