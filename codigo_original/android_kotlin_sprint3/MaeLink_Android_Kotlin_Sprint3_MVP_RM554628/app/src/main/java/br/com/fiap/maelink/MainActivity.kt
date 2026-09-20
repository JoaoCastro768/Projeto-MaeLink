package br.com.fiap.maelink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.fiap.maelink.navigation.MaeLinkNavHost
import br.com.fiap.maelink.ui.theme.MaeLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaeLinkTheme {
                MaeLinkNavHost()
            }
        }
    }
}
