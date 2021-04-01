/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private const val TAG = "GameViewModel"
class GameViewModel : ViewModel() {
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game - 45 seconds
        const val COUNTDOWN_TIME = 45000L
    }
    // The current word - it's *Mutable*LiveData to allow for setters to be called on it
    private val inWord = MutableLiveData<String>()
    val word : LiveData<String>
        get() = inWord //return internal word

    // The current score
    private val inScore = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = inScore //return internal score

    private val inEventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = inEventGameFinish

    private val timer : CountDownTimer
    private val inCurrentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
        get() = inCurrentTime

    val currentTimeString = Transformations.map(currentTime) {time ->
        DateUtils.formatElapsedTime(time)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        inEventGameFinish.value = false

        resetList()
        nextWord()
        inScore.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                //Needs to be divided by 1,000, otherwise it looks like it's counting down 1,000 times faster than real life
                inCurrentTime.value = millisUntilFinished/ ONE_SECOND
            }

            override fun onFinish() {
                inCurrentTime.value = DONE
                inEventGameFinish.value = true
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        //Don't forget to clean up after yourself
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf("queen", "hospital", "basketball", "cat", "change", "snail", "soup",
                "calendar", "sad", "desk", "guitar", "home", "railway", "zebra", "jelly", "car",
                "crow", "trade", "bag", "roll", "bubble", "drink", "football", "sandwich",
                "baseball", "spaghetti"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList() //The game won't end just because all of the words have been guessed
        }
        inWord.value = wordList.removeAt(0)
    }

    /** Informs Observers that the finish has been handled, and stops the finish event being
     * triggered by every screen configuration change. **/
    fun onGameFinishComplete() {
        inEventGameFinish.value = false
    }
    /** Methods for buttons presses **/
    fun onSkip() {
        inScore.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        inScore.value = (score.value)?.plus(1)
        nextWord()
    }
}