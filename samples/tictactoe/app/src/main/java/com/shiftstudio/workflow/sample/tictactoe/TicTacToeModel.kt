package com.shiftstudio.workflow.sample.tictactoe

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.shiftstudio.workflow.sample.tictactoe.mainworkflow.TicTacToeComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.mainworkflow.TicTacToeWorkflow
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.renderWorkflowIn
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class TicTacToeModel(
    private val savedState: SavedStateHandle,
    val workflow: TicTacToeWorkflow,
    val composableWorkflow: TicTacToeComposableWorkflow,
    private val traceFilesDir: File,
) : ViewModel() {
    private val running = Job()

    @OptIn(WorkflowUiExperimentalApi::class)
    val renderings: StateFlow<Screen> by lazy {
        val traceFile = traceFilesDir.resolve("workflow-trace-tictactoe.json")

        renderWorkflowIn(
            workflow = workflow,
            scope = viewModelScope,
            savedStateHandle = savedState,
        ) {
            running.complete()
        }
    }

    suspend fun waitForExit() = running.join()

    class Factory(
        owner: SavedStateRegistryOwner,
        private val workflow: TicTacToeWorkflow,
        val composableWorkflow: TicTacToeComposableWorkflow,
        private val traceFilesDir: File,
    ) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle,
        ): T {
            if (modelClass == TicTacToeModel::class.java) {
                @Suppress("UNCHECKED_CAST")
                return TicTacToeModel(handle, workflow, composableWorkflow, traceFilesDir) as T
            }

            throw IllegalArgumentException("Unknown ViewModel type $modelClass")
        }
    }
}
