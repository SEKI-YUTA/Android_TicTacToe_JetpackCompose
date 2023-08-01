package com.example.tictactoe

import androidx.lifecycle.ViewModel
import com.example.tictactoe.other.BoardCellState
import com.example.tictactoe.other.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState = _gameState.asStateFlow()

    val _nextPlayer = MutableStateFlow(true)
    val nextPlayer = _nextPlayer.asStateFlow()

    val _board = MutableStateFlow(
        mutableListOf(
            mutableListOf<BoardCellState>(BoardCellState.NotApplyed, BoardCellState.NotApplyed, BoardCellState.NotApplyed),
            mutableListOf<BoardCellState>(BoardCellState.NotApplyed, BoardCellState.NotApplyed, BoardCellState.NotApplyed),
            mutableListOf<BoardCellState>(BoardCellState.NotApplyed, BoardCellState.NotApplyed, BoardCellState.NotApplyed),
        )
    )
    val board = _board.asStateFlow()


    fun updateBoardCellState(row: Int, col: Int, state: BoardCellState) {
        _board.value = _board.value.apply {
            this[row][col] = state
        }
        checkWinner()
        if(gameState.value is GameState.Ended) return
        checkGameEnd()
        _nextPlayer.value = !_nextPlayer.value
    }

    // 勝ったかどうかを判定する関数
    fun checkWinner() {
        // 3x3のボードで縦、横、斜めのいずれかで3つ並んでいるかどうかを判定する
        val board = board.value
        val winner = (0..2).any { row ->
            board[row][0] != BoardCellState.NotApplyed &&
            board[row][0] == board[row][1] &&
            board[row][1] == board[row][2]
        } || (0..2).any { col ->
            board[0][col] != BoardCellState.NotApplyed &&
            board[0][col] == board[1][col] &&
            board[1][col] == board[2][col]
        } || (board[1][1] != BoardCellState.NotApplyed &&
            ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
            (board[0][2] == board[1][1] && board[1][1] == board[2][0])))
        if(winner) {
            _gameState.value = GameState.Ended(
                winner = nextPlayer.value,
                isDraw = false
            )
        }
    }

    fun resetBoard() {
        _board.value = mutableListOf(
            mutableListOf<BoardCellState>(BoardCellState.NotApplyed, BoardCellState.NotApplyed, BoardCellState.NotApplyed),
            mutableListOf<BoardCellState>(BoardCellState.NotApplyed, BoardCellState.NotApplyed, BoardCellState.NotApplyed),
            mutableListOf<BoardCellState>(BoardCellState.NotApplyed, BoardCellState.NotApplyed, BoardCellState.NotApplyed),
        )
    }

    // 全てのマスが埋まったかどうかを判定する関数
    fun checkGameEnd() {
        val filled = board.value.flatten().all {
            it != BoardCellState.NotApplyed
        }
        if(filled) {
            _gameState.value = GameState.Ended(
                winner = nextPlayer.value,
                isDraw = true
            )
        }
    }

    fun startGame() {
        _gameState.value = GameState.Playing
    }
}