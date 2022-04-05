package com.shiftstudio.workflow.sample.hello.infrastructure

import timber.log.Timber

/**
 * A [Timber.Tree] for debug builds.
 * Automatically shows a Hyperlink to the calling Class and Linenumber in the Logs.
 * Allows quick lookup of the caller source just by clicking on the Hyperlink in the Log.
 * @param showMethodName Whether or not to show the method name as well
 */
class HyperlinkedDebugTree(private val showMethodName: Boolean = true) : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement) =
        with(element) { "($fileName:$lineNumber) ${if (showMethodName) " $methodName()" else ""}" }
}
