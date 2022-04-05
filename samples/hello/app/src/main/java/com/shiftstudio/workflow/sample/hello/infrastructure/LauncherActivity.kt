package com.shiftstudio.workflow.sample.hello.infrastructure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.shiftstudio.workflow.sample.hello.Demo
import com.shiftstudio.workflow.sample.hello.infrastructure.theme.HelloMaterial3AndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // // Turn off the decor fitting system windows, which means we need to through handling
        // // insets
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        //
        // WindowCompat.getInsetsController(window, window.decorView)?.let {
        //     // if we set thin in themes.xml then it's not changeable in code, apparently
        //     it.isAppearanceLightStatusBars = true
        // }

        setContent {
            HelloMaterial3AndroidTheme {
                Demo(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
