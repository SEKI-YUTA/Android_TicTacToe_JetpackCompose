package com.example.tictactoe.other

sealed class GameState {
    object NotStarted: GameState()
    data class Ended(
        val winner: Boolean,
        val isDraw: Boolean
    ) : GameState()
    object Playing: GameState()
}
