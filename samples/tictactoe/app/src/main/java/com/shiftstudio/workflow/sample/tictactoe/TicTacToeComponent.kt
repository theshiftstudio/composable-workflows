package com.shiftstudio.workflow.sample.tictactoe

import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthService
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthService.AuthRequest
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthService.AuthResponse
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthService.SecondFactorRequest
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthWorkflow
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.RealAuthComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.RealAuthService
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.RealAuthWorkflow
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.RealGameLog
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.RealRunGameWorkflow
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.RealTakeTurnsWorkflow
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.RunGameWorkflow
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.TakeTurnsWorkflow
import com.shiftstudio.workflow.sample.tictactoe.mainworkflow.TicTacToeComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.mainworkflow.TicTacToeWorkflow
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import timber.log.Timber
import java.io.File

/**
 * Pretend generated code of a pretend DI framework.
 */
@OptIn(WorkflowUiExperimentalApi::class)
class TicTacToeComponent : ViewModel() {
    private val countingIdlingResource = CountingIdlingResource("AuthServiceIdling")
    val idlingResource: IdlingResource = countingIdlingResource

    private val realAuthService = RealAuthService()

    private val authService = object : AuthService {
        override fun login(request: AuthRequest): Single<AuthResponse> {
            return realAuthService.login(request)
                .doOnSubscribe { countingIdlingResource.increment() }
                .doAfterTerminate { countingIdlingResource.decrement() }
        }

        override fun secondFactor(request: SecondFactorRequest): Single<AuthResponse> {
            return realAuthService.secondFactor(request)
                .doOnSubscribe { countingIdlingResource.increment() }
                .doAfterTerminate { countingIdlingResource.decrement() }
        }
    }

    private fun authWorkflow(): AuthWorkflow = RealAuthWorkflow(authService)

    private fun authComposableWorkflowFactory(): () -> AuthComposableWorkflow {
        return {
            RealAuthComposableWorkflow(authService)
        }
    }

    private fun gameLog() = RealGameLog(mainThread())

    private fun gameWorkflow(): RunGameWorkflow =
        RealRunGameWorkflow(takeTurnsWorkflow(), gameLog())

    private fun takeTurnsWorkflow(): TakeTurnsWorkflow = RealTakeTurnsWorkflow()

    private val ticTacToeWorkflow = TicTacToeWorkflow(authWorkflow(), gameWorkflow())

    private val ticTacToeComposableWorkflow =
        TicTacToeComposableWorkflow(authComposableWorkflowFactory())

    fun ticTacToeModelFactory(
        owner: SavedStateRegistryOwner,
        filesDir: File,
    ): TicTacToeModel.Factory = TicTacToeModel.Factory(
        owner,
        ticTacToeWorkflow,
        ticTacToeComposableWorkflow,
        traceFilesDir = filesDir,
    )

    companion object {
        init {
            Timber.plant(Timber.DebugTree())

            val stock = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread, error ->
                Timber.e(error)
                stock?.uncaughtException(thread, error)
            }
        }
    }
}
