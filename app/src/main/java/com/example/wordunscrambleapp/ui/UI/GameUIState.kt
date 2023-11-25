package com.example.wordunscrambleapp.ui.UI

data class GameUIState (
    val currentscrumbleWords: String = "",
    val isGuessedWordWrong: Boolean = false,
    val currentWordCount: Int = 1,
    val currentGuessCount: Int = 0,
    val score: Int = 0
)