package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel(){
    private val inEventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = inEventPlayAgain
    private val inScore = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = inScore

    init {
        inScore.value = finalScore
        Log.i("ScoreViewModel", "Final score is $finalScore")
    }

    fun onPlayAgain() { inEventPlayAgain.value = true }
    fun onPlayAgainComplete() { inEventPlayAgain.value = false }
}