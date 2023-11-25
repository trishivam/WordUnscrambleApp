package com.example.wordunscrambleapp.ui.UI

import android.provider.UserDictionary.Words
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.wordunscrambleapp.data.allWords
import com.example.wordunscrambleapp.data.scoreIncreases
import com.example.wordunscrambleapp.data.scoreIncreases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel(){

    val _uiState = MutableStateFlow(GameUIState())
    var userGuess by mutableStateOf("")
        private set
    private lateinit var currentWords: String
    private val usedWord: MutableSet<String> = mutableSetOf()
    private fun pickRandomWordAndShuffle(): String
    {
        currentWords  = allWords.random()
        // check ths word is not used before
        if (usedWord.contains(currentWords)){
            pickRandomWordAndShuffle()
        }
        else{
            usedWord.add(currentWords)
            return shuffleCurrentWord(currentWords)
        }
        return ""
    }

    private fun shuffleCurrentWord(word:String):String{
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(word)){
            tempWord.shuffle()
        }
        return String(tempWord)

    }

    // Update User Guess
    fun updateUserGuess( guessWords: String){
        userGuess = guessWords
    }
    // Check User Guess
    fun checkUserGuess(){
        if(userGuess.trim().equals(currentWords, ignoreCase = true)){
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for next round

            val updatedScore = _uiState.value.score.plus(scoreIncreases)
            updatedGameState(updatedScore)
        }
        else{

            // User's guess is wrong, show an error
            _uiState.update { currentState -> currentState.copy(
                isGuessedWordWrong = true,
                currentGuessCount = currentState.currentGuessCount + 1
                ) }
        }
        // Reset User Guess
        updateUserGuess("")
    }

    // Update The Game State ( Update the Score and Pick a new word for Words data file
    private fun updatedGameState(updatedScore:Int){
        _uiState.update { currentState -> currentState.copy(
            isGuessedWordWrong = false,
            currentscrumbleWords = pickRandomWordAndShuffle(),
            currentGuessCount = 0,
            currentWordCount = currentState.currentWordCount.inc(),
            score = updatedScore
        ) }
    }

    // Skip word
    fun skipWord(){
        updatedGameState(_uiState.value.score)
        //reset Game
        updateUserGuess("")
    }

    // Rest Game
    fun resetGame(){
        usedWord.clear()
        _uiState.value = GameUIState(currentscrumbleWords = pickRandomWordAndShuffle())
    }
    init {
        resetGame()
    }
}