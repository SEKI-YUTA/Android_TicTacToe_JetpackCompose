package com.example.tictactoe

sealed class GameState {
    object NotStarted: GameState()
    object Started: GameState()
    data class Ended(
        val winner: Boolean
    ) : GameState()
    data class Playing(
        val currentPlayer: Boolean,
    ): GameState()
}
