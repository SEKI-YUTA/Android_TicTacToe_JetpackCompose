package com.example.tictactoe.other

sealed class BoardCellState {
    object NotApplyed: BoardCellState()
    object ApplyedToFirst: BoardCellState()
    object ApplyedToSecond: BoardCellState()
}
