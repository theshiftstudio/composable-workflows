package com.squareup.workflow1.ui.container

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_ESCAPE
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import com.squareup.workflow1.ui.R
import com.squareup.workflow1.ui.Screen
import com.squareup.workflow1.ui.ScreenViewHolder
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler
import com.squareup.workflow1.ui.show
import com.squareup.workflow1.ui.startShowing
import com.squareup.workflow1.ui.toViewFactory
import kotlin.reflect.KClass

/**
 * Convenient base class for building [ScreenOverlay] UIs that are compatible
 * with [View.backPressedHandler], and which honor the [ModalArea] constraint
 * placed in the [ViewEnvironment] by the standard [BodyAndModalsScreen] container.
 *
 * Ironically, [Dialog] instances are created with [FLAG_NOT_TOUCH_MODAL], to ensure
 * that events outside of the bounds reported by [updateBounds] reach views in
 * lower windows. See that method for details.
 */
@WorkflowUiExperimentalApi
public abstract class ModalScreenOverlayDialogFactory<O : ScreenOverlay<*>>(
  override val type: KClass<in O>
) : OverlayDialogFactory<O> {

  /**
   * Called from [buildDialog]. Builds (but does not show) the [Dialog] to
   * display a [contentView] built for a [ScreenOverlay.content].
   */
  public abstract fun buildDialogWithContentView(contentView: View): Dialog

  /**
   * If the [ScreenOverlay] displayed by a [dialog] created by this
   * factory is contained in a [BodyAndModalsScreen], this method will
   * be called to report the bounds of the managing view. It is expected
   * that such a dialog will be restricted to those bounds.
   *
   * Honoring this contract makes it easy to define areas of the display
   * that are outside of the "shadow" of a modal dialog. Imagine an app
   * with a status bar that should not be covered by modals.
   *
   * The default implementation calls straight through to [Dialog.setBounds].
   * Custom implementations are not required to call `super`.
   *
   * @see Dialog.setBounds
   */
  public open fun updateBounds(
    dialog: Dialog,
    bounds: Rect
  ) {
    dialog.setBounds(bounds)
  }

  final override fun buildDialog(
    initialRendering: O,
    initialEnvironment: ViewEnvironment,
    context: Context
  ): Dialog {
    // Put a no-op backPressedHandler behind the given rendering, to
    // ensure that the `onBackPressed` call below will not leak up to handlers
    // that should be blocked by this modal session.
    val wrappedContentRendering = BackButtonScreen(initialRendering.content) { }

    val contentViewHolder = wrappedContentRendering.toViewFactory(initialEnvironment)
      .startShowing(wrappedContentRendering, initialEnvironment, context).apply {
        // If the content view has no backPressedHandler, add a no-op one to
        // ensure that the `onBackPressed` call below will not leak up to handlers
        // that should be blocked by this modal session.
        if (view.backPressedHandler == null) view.backPressedHandler = { }
      }

    return buildDialogWithContentView(contentViewHolder.view).also { dialog ->
      val window = requireNotNull(dialog.window) { "Dialog must be attached to a window." }

      // Stick the contentViewHolder in a tag, where updateDialog can find it later.
      window.peekDecorView()?.setTag(R.id.workflow_modal_dialog_content, contentViewHolder)
        ?: throw IllegalStateException("Expected decorView to have been built.")

      val realWindowCallback = window.callback
      window.callback = object : Window.Callback by realWindowCallback {
        override fun dispatchKeyEvent(event: KeyEvent): Boolean {
          val isBackPress = (event.keyCode == KEYCODE_BACK || event.keyCode == KEYCODE_ESCAPE) &&
            event.action == ACTION_UP

          return when {
            isBackPress -> contentViewHolder.environment.get(ModalScreenOverlayOnBackPressed)
              .onBackPressed(contentViewHolder.view) == true
            else -> realWindowCallback.dispatchKeyEvent(event)
          }
        }
      }

      window.setFlags(FLAG_NOT_TOUCH_MODAL, FLAG_NOT_TOUCH_MODAL)
      dialog.maintainBounds(contentViewHolder.environment) { d, b -> updateBounds(d, Rect(b)) }
    }
  }

  final override fun updateDialog(
    dialog: Dialog,
    rendering: O,
    environment: ViewEnvironment
  ) {

    dialog.window?.peekDecorView()
      ?.let {
        @Suppress("UNCHECKED_CAST")
        it.getTag(R.id.workflow_modal_dialog_content) as? ScreenViewHolder<Screen>
      }
      ?.show(
        // Have to preserve the wrapping done in buildDialog.
        BackButtonScreen(rendering.content) { },
        environment
      )
  }
}
