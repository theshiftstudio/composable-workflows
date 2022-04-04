@file:OptIn(WorkflowUiExperimentalApi::class)

package com.shiftstudio.workflow.sample.tictactoe.mainworkflow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.shiftstudio.workflow.ComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthResult
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.GamePlayScreen
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.PlayerInfo
import com.shiftstudio.workflow.sample.tictactoe.mainworkflow.MainState.Authenticating
import com.shiftstudio.workflow.sample.tictactoe.mainworkflow.MainState.RunningGame
import com.squareup.sample.container.panel.PanelOverlay
import com.squareup.sample.container.panel.ScrimScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BodyAndModalsScreen

class TicTacToeComposableWorkflow constructor(
    private val authWorkflowFactory: () -> AuthComposableWorkflow,
) : ComposableWorkflow<Unit, Unit, BodyAndModalsScreen<ScrimScreen<*>, *>> {

    @Composable
    override fun render(
        renderProps: Unit,
        output: (Unit) -> Unit,
    ): BodyAndModalsScreen<ScrimScreen<*>, *> {
        var state by remember {
            mutableStateOf<MainState>(Authenticating)
        }

        val bodyAndModals: BodyAndModalsScreen<*, *> = when (state) {
            is Authenticating -> {
                val authWorkflow = remember {
                    authWorkflowFactory()
                }
                val authBackStack = authWorkflow.render(renderProps = Unit) { authResult ->
                    when (authResult) {
                        is AuthResult.Canceled -> output(Unit)
                        is AuthResult.Authorized -> state = RunningGame
                    }
                }
                // We always show an empty GameScreen behind the PanelOverlay that
                // hosts the authWorkflow's renderings because that's how the
                // award winning design team wanted it to look. Yes, it's a cheat
                // that TicTacToeWorkflow is aware of the GamePlayScreen type, and that
                // cheat is probably the most realistic thing about this sample.
                val emptyGameScreen = GamePlayScreen()

                // BodyAndModalsScreen(emptyGameScreen, PanelOverlay(authBackStack))
                BodyAndModalsScreen(authBackStack, emptyList())
            }

            RunningGame -> {

                BodyAndModalsScreen(GamePlayScreen(
                    playerInfo = PlayerInfo(xName = "Tudi", oName = "Opula")
                ), emptyList())
                // val gameRendering = context.renderChild(runGameWorkflow) { startAuth }
                //
                // if (gameRendering.namePrompt == null) {
                //     BodyAndModalsScreen(gameRendering.gameScreen, gameRendering.alerts)
                // } else {
                //     // To prompt for player names, the child puts up a ScreenOverlay, which
                //     // we replace here with a tasteful PanelOverlay.
                //     //
                //     // If the name prompt gets canceled, we'd like a visual effect of
                //     // popping back to the auth flow in that same panel. To get this effect
                //     // we:
                //     //  - run an authWorkflow
                //     //  - append namePrompt.content to that BackStackScreen
                //     //  - and put that whole thing in the PanelOverlay
                //     //
                //     // We use the "fake" uniquing name to make sure authWorkflow session from the
                //     // Authenticating state was allowed to die, so that this one will start fresh
                //     // in its logged out state.
                //     val stubAuthBackStack = context.renderChild(authWorkflow, "fake") { WorkflowAction.noAction() }
                //     val fullBackStack = stubAuthBackStack +
                //         BackStackScreen(gameRendering.namePrompt.content)
                //     val allModals = listOf(PanelOverlay(fullBackStack)) + gameRendering.alerts
                //
                //     BodyAndModalsScreen(gameRendering.gameScreen, allModals)
                // }
            }
        }

        // Add the scrim. Dim it only if there is a panel showing.
        val dim = bodyAndModals.modals.any { modal -> modal is PanelOverlay<*> }
        return bodyAndModals.mapBody { body -> ScrimScreen(body, dimmed = dim) }
    }
}