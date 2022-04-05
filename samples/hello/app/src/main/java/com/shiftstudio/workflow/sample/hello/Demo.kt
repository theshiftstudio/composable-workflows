@file:OptIn(WorkflowUiExperimentalApi::class, WorkflowUiExperimentalApi::class)

package com.shiftstudio.workflow.sample.hello

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shiftstudio.workflow.sample.hello.infrastructure.theme.HelloMaterial3AndroidTheme
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backstack.BackStackContainer
import com.squareup.workflow1.ui.compose.WorkflowRendering
import com.squareup.workflow1.ui.compose.composeViewFactory
import com.squareup.workflow1.ui.plus
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
fun Demo(modifier: Modifier = Modifier) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val context = LocalContext.current
        val helloWorkflow1 = remember(context) {
            EntryPointAccessors.fromApplication<HelloEntryPoint>(context).helloWorkflow()
        }

        val helloWorkflow2 = remember(context) {
            EntryPointAccessors.fromApplication<HelloEntryPoint>(context).helloWorkflow()
        }

        val environment = remember {
            val viewRegistries = ViewRegistry(BackStackContainer) + HelloBinding
            ViewEnvironment(mapOf(ViewRegistry to viewRegistries))
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Greeting("Android")

            val rendering1 = helloWorkflow1.render(renderProps = "Tudor") {}

            WorkflowRendering(
                rendering = rendering1,
                viewEnvironment = environment,
                modifier = Modifier
                    .border(2.dp, Color.Magenta)
                    .weight(1f),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                val rendering2 = helloWorkflow2.render(renderProps = "Andra") {}

                val rendering2Copy = helloWorkflow2.render(renderProps = "Andra, Copy") {}

                WorkflowRendering(
                    rendering = rendering2,
                    viewEnvironment = environment,
                    modifier = Modifier
                        .border(2.dp, Color.Green)
                        .weight(1f),
                )

                WorkflowRendering(
                    rendering = rendering2Copy,
                    viewEnvironment = environment,
                    modifier = Modifier
                        .border(2.dp, Color.Yellow)
                        .weight(1f),
                )
            }
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HelloEntryPoint {
    fun helloWorkflow(): HelloWorkflow
}

val HelloBinding = composeViewFactory<HelloWorkflow.Rendering> { rendering, _ ->
    Column(
        modifier = Modifier
            .clickable(onClick = rendering.onClick)
            .fillMaxSize()
    ) {
        Text(
            text = "${rendering.clicks}",
            modifier = Modifier
                .padding(16.dp),
        )

        Text(rendering.message, fontSize = 12.sp)

        OutlinedTextField(
            value = rendering.name,
            onValueChange = rendering.onNameChange,
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloMaterial3AndroidTheme {
        Greeting("Android")
    }
}
