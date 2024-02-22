package com.csc2007.notetaker.database.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class PomodoroTimerViewModel() : ViewModel() {
    private var countDownTimer: CountDownTimer? = null

    val startingMinutes = 20
    val startingSeconds = 0

    val originalMinutesInMillis = TimeUnit.MINUTES.toMillis(startingMinutes.toLong())
    val originalSecondsInMillis = TimeUnit.SECONDS.toMillis(startingSeconds.toLong())

    private val initialTotalTimeInMillis = originalMinutesInMillis + originalSecondsInMillis
    private val _timeLeft = MutableStateFlow(initialTotalTimeInMillis)
    var timeLeft: StateFlow<Long> = _timeLeft

    val countDownInterval = 1000L

    private var _timerState = MutableStateFlow(false)
    var timerState: StateFlow<Boolean> = _timerState

    private val _displayMinutes = MutableStateFlow(startingMinutes.toString())
    var displayMinutes: StateFlow<String> = _displayMinutes
    
    private val _displaySeconds = MutableStateFlow(startingSeconds.toString())
    var displaySeconds: StateFlow<String> = _displaySeconds

    fun startTimer() {

        _timerState.value = true
        countDownTimer = object : CountDownTimer(timeLeft.value, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _displayMinutes.value = (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60).toString()
                _displaySeconds.value = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60).toString()
                _timeLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                _timerState.value = false
                countDownTimer?.cancel()
            }
        }.start()
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        _timerState.value = false
    }

    fun resetTimer() {
        countDownTimer?.cancel()
        _timerState.value = false

        _displayMinutes.value = startingMinutes.toString()
        _displaySeconds.value = startingSeconds.toString()
        _timeLeft.value = initialTotalTimeInMillis
    }
}