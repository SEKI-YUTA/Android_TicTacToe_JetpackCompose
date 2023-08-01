package com.example.tictactoe

sealed class BoardCellState {
    object NotApplyed: BoardCellState()
    object ApplyedToFirst: BoardCellState()
    object ApplyedToSecond: BoardCellState()
}
