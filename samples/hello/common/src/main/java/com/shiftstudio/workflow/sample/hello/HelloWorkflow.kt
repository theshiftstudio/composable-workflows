package com.shiftstudio.workflow.sample.hello

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.shiftstudio.workflow.ComposableWorkflow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class HelloWorkflow @Inject constructor() :
    ComposableWorkflow<String, Nothing, HelloWorkflow.Rendering> {

    @Parcelize
    @Immutable
    internal data class WholeState(
        val name: String,
        val clicks: Int = 0,
        val greeting: Greeting,
    ) : Parcelable {

        enum class Greeting {
            Hello,
            Goodbye;

            fun theOtherGreeting(): Greeting = when (this) {
                Hello -> Goodbye
                Goodbye -> Hello
            }
        }
    }

    data class Rendering(
        val name: String,
        val onNameChange: (String) -> Unit,
        val message: String,
        val clicks: Int,
        val onClick: () -> Unit,
    )

    @Composable
    override fun render(renderProps: String, output: (Nothing) -> Unit): Rendering {
        var state by rememberSaveable(renderProps) {
            mutableStateOf(WholeState(name = renderProps, greeting = WholeState.Greeting.Hello))
        }

        return Rendering(
            name = state.name,
            onNameChange = { newName ->
                state = state.copy(name = newName)
            },
            message = "${state.greeting.name}, ${state.name}",
            clicks = state.clicks,
            onClick = {
                state = state.copy(
                    clicks = state.clicks + 1,
                    greeting = state.greeting.theOtherGreeting(),
                )
            }
        )
    }
}
