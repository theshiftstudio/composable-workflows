package com.shiftstudio.workflow.sample.hello.infrastructure

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.findActivity(): ComponentActivity {
    val context = this

    return context.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            if (ctx is ComponentActivity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException(
            "Expected an activity context for creating a HiltViewModelFactory but instead found: $ctx"
        )
    }
}
