package com.shiftstudio.workflow

import androidx.compose.runtime.Composable


public interface ComposableWorkflow<in PropsT, out OutputT, out RenderingT> {

    @Composable
    public fun render(
        renderProps: PropsT,
        output: (OutputT) -> Unit
    ): RenderingT
}
