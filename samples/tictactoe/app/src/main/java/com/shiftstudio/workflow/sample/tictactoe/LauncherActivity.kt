@file:OptIn(WorkflowUiExperimentalApi::class)

package com.shiftstudio.workflow.sample.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthViewFactories
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.TicTacToeViewFactories
import com.shiftstudio.workflow.sample.tictactoe.ui.theme.ComposableworkflowsTheme
import com.squareup.sample.container.SampleContainers
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.WorkflowRendering
import com.squareup.workflow1.ui.merge
import com.squareup.workflow1.ui.modal.AlertContainer
import com.squareup.workflow1.ui.plus

class LauncherActivity : ComponentActivity() {
    /** Exposed for use by espresso tests. */
    // lateinit var idlingResource: IdlingResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // idlingResource = component.idlingResource

        val component: TicTacToeComponent by viewModels()

        // setContentView(
        //     WorkflowLayout(this).apply {
        //         take(lifecycle, model.renderings.map { it.withRegistry(viewRegistry) })
        //     }
        // )

        setContent {
            ComposableworkflowsTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val stateRegistryOwner = LocalSavedStateRegistryOwner.current
                    val model: TicTacToeModel = viewModel(
                        factory = component.ticTacToeModelFactory(
                            owner = stateRegistryOwner,
                            filesDir = this.filesDir,
                        ),
                    )

                    // val model: TicTacToeModel by viewModels { component.ticTacToeModelFactory(this) }

                    Column(modifier = Modifier.fillMaxSize()) {
                        Greeting("Android")

                        // val rendering by model.workflow.renderAsState(props = Unit) {
                        //
                        // }

                        val rendering = model.composableWorkflow.render(renderProps = Unit) {
                            finish()
                        }

                        WorkflowRendering(
                            rendering = rendering,
                            viewEnvironment = env,
                            modifier = Modifier.border(2.dp, Color.Magenta),
                        )
                    }
                }
            }
        }

        // lifecycleScope.launch {
        //     model.renderings.collect { Timber.d("rendering: %s", it) }
        // }
        // lifecycleScope.launch {
        //     model.waitForExit()
        //     finish()
        // }
    }

    private companion object {
        val viewRegistry = SampleContainers +
            AuthViewFactories +
            TicTacToeViewFactories +
            AlertContainer

        val env = ViewEnvironment.EMPTY merge viewRegistry
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposableworkflowsTheme {
        Greeting("Android")
    }
}