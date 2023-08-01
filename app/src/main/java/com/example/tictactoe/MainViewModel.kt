package com.example.tictactoe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState = _gameState.asStateFlow()

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
        _gameState.value = GameState.Playing(currentPlayer = !(gameState.value as GameState.Playing).currentPlayer)
    }

    fun startGame() {
        _gameState.value = GameState.Playing(currentPlayer = true)
    }
}