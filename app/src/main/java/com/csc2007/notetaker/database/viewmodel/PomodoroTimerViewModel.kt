package com.csc2007.notetaker.database.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.csc2007.notetaker.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class PomodoroTimerViewModel() : ViewModel() {
    private var countDownTimer: CountDownTimer? = null

    private val _timerPomodoroComplete = MutableStateFlow(false)
    var timerPomodoroComplete: StateFlow<Boolean> = _timerPomodoroComplete

    private val _pomodoroMinutes = MutableStateFlow(15)
    var pomodoroMinutes: StateFlow<Int> = _pomodoroMinutes
    private val _pomodoroSeconds = MutableStateFlow(0)
    var pomodoroSeconds: StateFlow<Int> = _pomodoroSeconds

    private val _shortBreakMinutes = MutableStateFlow(10)
    var shortBreakMinutes: StateFlow<Int> = _shortBreakMinutes
    private val _shortBreakSeconds = MutableStateFlow(0)
    var shortBreakSeconds: StateFlow<Int> = _shortBreakSeconds

    private val _longBreakMinutes = MutableStateFlow(20)
    var longBreakMinutes: StateFlow<Int> = _longBreakMinutes
    private val _longBreakSeconds = MutableStateFlow(0)
    var longBreakSeconds: StateFlow<Int> = _longBreakSeconds

    var originalMinutesInMillis = MutableStateFlow(TimeUnit.MINUTES.toMillis(_pomodoroMinutes.value.toLong()))
    var originalSecondsInMillis = MutableStateFlow(TimeUnit.SECONDS.toMillis(_pomodoroSeconds.value.toLong()))

    private val initialTotalTimeInMillis = MutableStateFlow(originalMinutesInMillis.value + originalSecondsInMillis.value)
    private val _timeLeft = MutableStateFlow(initialTotalTimeInMillis.value)
    var timeLeft: StateFlow<Long> = _timeLeft

    val countDownInterval = 1000L

    private var _timerState = MutableStateFlow(false)
    var timerState: StateFlow<Boolean> = _timerState

    private val _displayMinutes = MutableStateFlow(_pomodoroMinutes.value.toString())
    var displayMinutes: StateFlow<String> = _displayMinutes
    
    private val _displaySeconds = MutableStateFlow(_pomodoroSeconds.value.toString())
    var displaySeconds: StateFlow<String> = _displaySeconds

    val pointsMultiplier = 2

    fun startTimer(context: Context, selectedTimer: String) {

        _timerState.value = true
        _timerPomodoroComplete.value = false
        countDownTimer = object : CountDownTimer(timeLeft.value, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _displayMinutes.value = (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60).toString()
                _displaySeconds.value = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60).toString()
                _timeLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                if (selectedTimer == "Pomodoro") {
                    _timerPomodoroComplete.value = true
                }
                _timerState.value = false
                countDownTimer?.cancel()
                playSound(context)
                resetTimer(selectedTimer)
            }
        }.start()
    }

    fun setTimerPomodoroComplete() {
        _timerPomodoroComplete.value = false
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        _timerState.value = false
    }

    fun resetTimer(selectedTimer: String) {
        countDownTimer?.cancel()
        _timerState.value = false

        if (selectedTimer == "Pomodoro") {
            _displayMinutes.value = _pomodoroMinutes.value.toString()
            _displaySeconds.value = _pomodoroSeconds.value.toString()

            originalMinutesInMillis.value = TimeUnit.MINUTES.toMillis(_pomodoroMinutes.value.toLong())
            originalSecondsInMillis.value = TimeUnit.SECONDS.toMillis(_pomodoroSeconds.value.toLong())
            initialTotalTimeInMillis.value = originalMinutesInMillis.value + originalSecondsInMillis.value

            _timeLeft.value = initialTotalTimeInMillis.value
        } else if (selectedTimer == "Short Break") {
            _displayMinutes.value = _shortBreakMinutes.value.toString()
            _displaySeconds.value = _shortBreakSeconds.value.toString()

            originalMinutesInMillis.value = TimeUnit.MINUTES.toMillis(_shortBreakMinutes.value.toLong())
            originalSecondsInMillis.value = TimeUnit.SECONDS.toMillis(_shortBreakSeconds.value.toLong())
            initialTotalTimeInMillis.value = originalMinutesInMillis.value + originalSecondsInMillis.value

            _timeLeft.value = initialTotalTimeInMillis.value
        } else if (selectedTimer == "Long Break") {
            _displayMinutes.value = _longBreakMinutes.value.toString()
            _displaySeconds.value = _longBreakSeconds.value.toString()

            originalMinutesInMillis.value = TimeUnit.MINUTES.toMillis(_longBreakMinutes.value.toLong())
            originalSecondsInMillis.value = TimeUnit.SECONDS.toMillis(_longBreakSeconds.value.toLong())
            initialTotalTimeInMillis.value = originalMinutesInMillis.value + originalSecondsInMillis.value

            _timeLeft.value = initialTotalTimeInMillis.value
        }
    }

    fun adjustTimer(minutes: Int, seconds: Int, selectedTimer: String) {
        countDownTimer?.cancel()
        _timerState.value = false

        if (selectedTimer == "Pomodoro") {
            _pomodoroMinutes.value = minutes
            _pomodoroSeconds.value = seconds

            resetTimer(selectedTimer)
        } else if (selectedTimer == "Short Break") {
            _shortBreakMinutes.value = minutes
            _shortBreakSeconds.value = seconds

            resetTimer(selectedTimer)
        } else if (selectedTimer == "Long Break") {
            _longBreakMinutes.value = minutes
            _longBreakSeconds.value = seconds

            resetTimer(selectedTimer)
        }
    }

    private fun playSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.completed_sound)
        mediaPlayer.start()
    }
}