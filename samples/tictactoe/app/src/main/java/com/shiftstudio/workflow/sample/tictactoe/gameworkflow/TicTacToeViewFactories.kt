package com.shiftstudio.workflow.sample.tictactoe.gameworkflow

import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi

@OptIn(WorkflowUiExperimentalApi::class)
val TicTacToeViewFactories = ViewRegistry(
    NewGameViewFactory,
    GamePlayViewFactory,
    GameOverLayoutRunner
)
