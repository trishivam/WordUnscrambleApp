package com.example.wordunscrambleapp

import android.os.Bundle
import android.provider.CalendarContract
import android.provider.UserDictionary.Words
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordunscrambleapp.data.guessCountMAX
import com.example.wordunscrambleapp.data.maxNumberOfWords
import com.example.wordunscrambleapp.data.scoreIncreases
import com.example.wordunscrambleapp.ui.UI.GameViewModel
import com.example.wordunscrambleapp.ui.theme.WordUnscrambleAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordUnscrambleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordUnscrambleApp()
                }
            }
        }
    }
}

@Composable
fun WordUnscrambleApp() {
    GameScreen()
}

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUIState by gameViewModel._uiState.collectAsState()
    val mediumPadding = 8.dp
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Unscramble",
            style = typography.displayMedium.copy(fontWeight = FontWeight.Bold),
        )
        GameLayout(
            currentscrumbleWords = gameUIState.currentscrumbleWords,
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            userGuess = gameViewModel.userGuess,
            isGuessWrong = gameUIState.isGuessedWordWrong,
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            wordCount = gameUIState.currentWordCount,
            currentguessWordCount = gameUIState.currentGuessCount,
//            skipWord = { gameViewModel.skipWord() },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding((mediumPadding)),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { gameViewModel.checkUserGuess() },
                colors = ButtonDefaults.buttonColors(Color(0xFF4355B9))
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    fontSize = 16.sp
                )
            }
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {  }
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    fontSize = 16.sp,
                )
            }
        }
        GameStatus(score = gameUIState.score, modifier = Modifier.padding(20.dp))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatus(
    score: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFFE3E1EC) )
    ) {
        Text(
            text = stringResource(R.string.score,score),
            modifier = Modifier.padding(8.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout(
    currentscrumbleWords: String,
    userGuess:String,
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    isGuessWrong:Boolean,
    currentguessWordCount: Int,
    wordCount: Int,
    modifier:Modifier = Modifier,
    ) {
    val mediumPadding = 8.dp
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(Color(0xFFE3E1EC) )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(mediumPadding)
        ) {
            Text(

                text = "$wordCount/$maxNumberOfWords",
                style = typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .clip(shape = shapes.medium)
                    .background(color = Color(0xFF4355B9))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End)
            )
            // Unsrable Word
            Text(
                text = currentscrumbleWords,
                style = typography.displayMedium
            )
            // Instruction
            Text(

                text = if (currentguessWordCount < guessCountMAX ){
                           stringResource(R.string.instructions, guessCountMAX-currentguessWordCount)}
                else{
                    "You have lost"

                },

                textAlign = TextAlign.Center,
                style = typography.titleMedium,

            )
            OutlinedTextField(
                value = userGuess,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(containerColor = colorScheme.surface),
                label = { Text(text = stringResource(R.string.enter_your_words)) },
                onValueChange = onUserGuessChanged,
//                isError = isGuessWrong,
                supportingText = {
                    if (isGuessWrong) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Wrong Guess",
                            color = colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                )
            )

        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    WordUnscrambleAppTheme {
//        Greeting("Android")
//    }
//}