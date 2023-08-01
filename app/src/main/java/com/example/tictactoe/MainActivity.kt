package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.other.BoardCellState
import com.example.tictactoe.other.GameState
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            val configration = LocalConfiguration.current
            val size = configration.screenWidthDp.dp / 3
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val gameState = viewModel.gameState.collectAsState()
                    val nextPlayer = viewModel.nextPlayer.collectAsState()
                    val nextPlayerStr = if (nextPlayer.value) "先行" else "後攻"
                    val boardState = viewModel.board.collectAsState()
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PlayerBadge(color = Color.Red, tag = "先攻")
                            Spacer(modifier = Modifier.height(16.dp))
                            PlayerBadge(color = Color.Blue, tag = "後攻")


                            if (gameState.value is GameState.NotStarted) {
                                Text("Not Started")
                            } else if (gameState.value is GameState.Ended) {
                                val endState = (gameState.value as GameState.Ended)
                                Text(
                                    text = if (endState.isDraw) "Draw" else "Winner: ${if (endState.winner) "先攻" else "後攻"}"
                                )
                            } else if (gameState.value is GameState.Playing) {
                                Text("Playing")
                            }
                            if (gameState.value is GameState.Playing) {
                                Text("Current Player: $nextPlayerStr")
                                repeat(3) { row ->
                                    Row {
                                        repeat(3) { col ->
                                            BoardCell(
                                                modifier = Modifier
                                                    .width(size)
                                                    .height(size),
                                                state = boardState.value[row][col],
                                                row = row,
                                                col = col,
                                                tapAction = { row, col ->
                                                    if (boardState.value[row][col] != BoardCellState.NotApplyed) return@BoardCell
                                                    viewModel.updateBoardCellState(
                                                        row = row,
                                                        col = col,
                                                        state = if (nextPlayer.value) BoardCellState.ApplyedToFirst else BoardCellState.ApplyedToSecond
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Button(onClick = {
                                viewModel.resetBoard()
                                viewModel.startGame()
                            }) {
                                Text("Start")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerBadge(color: Color, tag: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .width(30.dp)
                .height(30.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(tag)
    }
}

@Composable
fun BoardCell(
    modifier: Modifier,
    state: BoardCellState,
    row: Int,
    col: Int,
    tapAction: (row: Int, col: Int) -> Unit
) {
    val color = when (state) {
        BoardCellState.NotApplyed -> Color.White
        BoardCellState.ApplyedToFirst -> Color.Blue
        BoardCellState.ApplyedToSecond -> Color.Red
    }
    Box(
        modifier = modifier
            .clickable {
                tapAction(row, col)
            }
            .background(color)
            .border(1.dp, Color.Black)
    )
}