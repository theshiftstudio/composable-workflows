@file:OptIn(WorkflowUiExperimentalApi::class)

package com.shiftstudio.workflow.sample.tictactoe.authworkflow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.shiftstudio.workflow.ComposableWorkflow
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthService.AuthRequest
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthState.Authorizing
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthState.AuthorizingSecondFactor
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthState.LoginPrompt
import com.shiftstudio.workflow.sample.tictactoe.authworkflow.AuthState.SecondFactorPrompt
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.container.BackStackScreen
import kotlinx.coroutines.rx2.await

typealias AuthComposableWorkflow = ComposableWorkflow<Unit, AuthResult, BackStackScreen<Screen>>

class RealAuthComposableWorkflow constructor(
    private val authService: AuthService,
) : AuthComposableWorkflow {

    @Composable
    override fun render(renderProps: Unit, output: (AuthResult) -> Unit): BackStackScreen<Screen> {
        var state by rememberSaveable {
            mutableStateOf<AuthState>(LoginPrompt())
        }

        return when (state) {
            is LoginPrompt -> {
                val renderState = state as LoginPrompt
                BackStackScreen(
                    LoginScreen(
                        errorMessage = renderState.errorMessage,
                        onLogin = { email, password ->
                            state = when {
                                email.isValidEmail -> Authorizing(email, password)
                                else -> LoginPrompt(email.emailValidationErrorMessage)
                            }
                        },
                        onCancel = {
                            output(AuthResult.Canceled)
                        }
                    )
                )
            }

            is Authorizing -> {
                LaunchedEffect(Unit) {
                    val renderState = state as Authorizing
                    val request = AuthRequest(
                        email = renderState.email, password = renderState.password
                    )
                    val response = authService.login(request).await()

                    when {
                        response.isLoginFailure -> state = LoginPrompt(response.errorMessage)
                        response.twoFactorRequired -> state = SecondFactorPrompt(response.token)
                        else -> output(AuthResult.Authorized(response.token))
                    }
                }

                BackStackScreen(
                    LoginScreen(),
                    AuthorizingScreen("Logging in…")
                )
            }

            is SecondFactorPrompt -> {
                val renderState = state as SecondFactorPrompt
                BackStackScreen(
                    LoginScreen(),
                    SecondFactorScreen(
                        renderState.errorMessage,
                        onSubmit = { secondFactor ->
                            state = AuthorizingSecondFactor(renderState.tempToken, secondFactor)
                        },
                        // onSubmit = context.eventHandler { secondFactor ->
                        //     (state as? SecondFactorPrompt)?.let { oldState ->
                        //         state = AuthState.AuthorizingSecondFactor(oldState.tempToken,
                        //             secondFactor)
                        //     }
                        // },
                        onCancel = {
                            state = LoginPrompt()
                        }
                        // onCancel = context.eventHandler { state = LoginPrompt() }
                    )
                )
            }

            is AuthorizingSecondFactor -> {
                val renderState = state as AuthorizingSecondFactor
                val request = AuthService.SecondFactorRequest(
                    renderState.tempToken, renderState.secondFactor
                )

                LaunchedEffect(Unit) {
                    val response = authService.secondFactor(request).await()
                    when {
                        response.isLoginFailure -> state = LoginPrompt(response.errorMessage)
                        response.twoFactorRequired -> state = SecondFactorPrompt(response.token)
                        else -> output(AuthResult.Authorized(response.token))
                    }
                }

                BackStackScreen(
                    LoginScreen(),
                    SecondFactorScreen(),
                    AuthorizingScreen("Submitting one time token…")
                )
            }
        }
    }
}
