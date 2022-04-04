package com.shiftstudio.workflow.sample.tictactoe.authworkflow

import com.shiftstudio.workflow.sample.tictactoe.databinding.LoginLayoutBinding
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewFactory.Companion.fromViewBinding
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler

@OptIn(WorkflowUiExperimentalApi::class)
internal val LoginViewFactory: ScreenViewFactory<LoginScreen> =
    fromViewBinding(LoginLayoutBinding::inflate) { rendering, _ ->
        loginErrorMessage.text = rendering.errorMessage

        loginButton.setOnClickListener {
            rendering.onLogin(loginEmail.text.toString(), loginPassword.text.toString())
        }

        root.backPressedHandler = { rendering.onCancel() }
    }
