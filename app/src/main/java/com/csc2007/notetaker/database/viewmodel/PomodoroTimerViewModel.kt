package com.csc2007.notetaker.database.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.csc2007.notetaker.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class PomodoroTimerViewModel() : ViewModel() {
    private var countDownTimer: CountDownTimer? = null

    var startingMinutes = MutableStateFlow(0)
    var startingSeconds = MutableStateFlow(10)

    var originalMinutesInMillis = MutableStateFlow(TimeUnit.MINUTES.toMillis(startingMinutes.value.toLong()))
    var originalSecondsInMillis = MutableStateFlow(TimeUnit.SECONDS.toMillis(startingSeconds.value.toLong()))

    private val initialTotalTimeInMillis = MutableStateFlow(originalMinutesInMillis.value + originalSecondsInMillis.value)
    private val _timeLeft = MutableStateFlow(initialTotalTimeInMillis.value)
    var timeLeft: StateFlow<Long> = _timeLeft

    val countDownInterval = 1000L

    private var _timerState = MutableStateFlow(false)
    var timerState: StateFlow<Boolean> = _timerState

    private val _displayMinutes = MutableStateFlow(startingMinutes.value.toString())
    var displayMinutes: StateFlow<String> = _displayMinutes
    
    private val _displaySeconds = MutableStateFlow(startingSeconds.value.toString())
    var displaySeconds: StateFlow<String> = _displaySeconds

    fun startTimer(context: Context) {

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
                playSound(context)
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

        _displayMinutes.value = startingMinutes.value.toString()
        _displaySeconds.value = startingSeconds.value.toString()
        _timeLeft.value = initialTotalTimeInMillis.value
    }

    fun adjustTimer(minutes: Int, seconds: Int) {
        countDownTimer?.cancel()
        _timerState.value = false

        startingMinutes.value = minutes
        startingSeconds.value = seconds
        _displayMinutes.value = startingMinutes.value.toString()
        _displaySeconds.value = startingSeconds.value.toString()

        originalMinutesInMillis.value = TimeUnit.MINUTES.toMillis(startingMinutes.value.toLong())
        originalSecondsInMillis.value = TimeUnit.SECONDS.toMillis(startingSeconds.value.toLong())
        initialTotalTimeInMillis.value = originalMinutesInMillis.value + originalSecondsInMillis.value

        _timeLeft.value = initialTotalTimeInMillis.value
    }

    private fun playSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.completed_sound)
        mediaPlayer.start()
    }
}