package com.shiftstudio.workflow.sample.tictactoe.gameworkflow

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.shiftstudio.workflow.sample.tictactoe.databinding.BoardBinding
import com.shiftstudio.workflow.sample.tictactoe.databinding.GamePlayLayoutBinding
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.Ending.*
import com.shiftstudio.workflow.sample.tictactoe.gameworkflow.SyncState.*
import com.squareup.workflow1.ui.*
import com.squareup.workflow1.ui.ScreenViewFactory.Companion.fromViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
internal class GameOverLayoutRunner(
    private val binding: GamePlayLayoutBinding
) : ScreenViewRunner<GameOverScreen> {

    private val saveItem: MenuItem = binding.gamePlayToolbar.menu.add("")
        .apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

    private val exitItem: MenuItem = binding.gamePlayToolbar.menu.add("Exit")
        .apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }

    override fun showRendering(
        rendering: GameOverScreen,
        viewEnvironment: ViewEnvironment
    ) {
        exitItem.setOnMenuItemClickListener {
            rendering.onPlayAgain()
            true
        }
        binding.root.backPressedHandler = { rendering.onExit() }

        when (rendering.endGameState.syncState) {
            SAVING -> {
                saveItem.isEnabled = false
                saveItem.title = "saving…"
                saveItem.setOnMenuItemClickListener(null)
            }
            SAVE_FAILED -> {
                saveItem.isEnabled = true
                saveItem.title = "Unsaved"
                saveItem.setOnMenuItemClickListener {
                    rendering.onTrySaveAgain()
                    true
                }
            }
            SAVED -> {
                saveItem.isVisible = false
                saveItem.setOnMenuItemClickListener(null)
            }
        }

        renderGame(
            binding.gamePlayBoard, binding.gamePlayToolbar, rendering.endGameState.completedGame,
            rendering.endGameState.playerInfo
        )
    }

    private fun renderGame(
        boardView: BoardBinding,
        toolbar: Toolbar,
        completedGame: CompletedGame,
        playerInfo: PlayerInfo
    ) {
        renderResult(toolbar, completedGame, playerInfo)
        completedGame.lastTurn.board.render(boardView.root)
    }

    private fun renderResult(
        toolbar: Toolbar,
        completedGame: CompletedGame,
        playerInfo: PlayerInfo
    ) {
        val symbol = completedGame.lastTurn.playing.symbol
        val playerName = completedGame.lastTurn.playing.name(playerInfo)

        toolbar.title = if (playerName.isEmpty()) {
            when (completedGame.ending) {
                Victory -> "$symbol wins!"
                Draw -> "It's a draw."
                Quitted -> "$symbol is a quitter!"
            }
        } else {
            when (completedGame.ending) {
                Victory -> "The $symbol's have it, $playerName wins!"
                Draw -> "It's a draw."
                Quitted -> "$playerName ($symbol) is a quitter!"
            }
        }
    }

    /** Note how easily we're sharing this layout with [GamePlayViewFactory]. */
    companion object : ScreenViewFactory<GameOverScreen> by fromViewBinding(
        GamePlayLayoutBinding::inflate, ::GameOverLayoutRunner
    )
}
