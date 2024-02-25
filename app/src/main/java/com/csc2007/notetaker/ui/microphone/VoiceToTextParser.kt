package com.csc2007.notetaker.ui.microphone

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VoiceToTextParserState(
    val spokenText: StringBuilder = StringBuilder(),
    val isSpeaking: Boolean = false,
    val err: String? = null
)

class VoiceToTextParser(
    private val app: Application
) : RecognitionListener {
    private val _state = MutableStateFlow(VoiceToTextParserState())
    private val audioManager = app.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(app)
    private lateinit var intent: Intent

    val state = _state.asStateFlow()

    fun startListening(languageCode: String = "en") {
        audioManager.setStreamVolume(
            AudioManager.STREAM_NOTIFICATION,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
            0
        )
        _state.update { VoiceToTextParserState() }

        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(
                    err = "Recognition is not available"
                )
            }
        }

        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                languageCode
            )
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "" // Disable audio feedback by setting prompt to null
            )
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening() {
        audioManager.setStreamVolume(
            AudioManager.STREAM_NOTIFICATION,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
            0
        )

        _state.update {
            it.copy(
                isSpeaking = false
            )
        }

        recognizer.stopListening()
        recognizer.cancel()
    }

    private fun restartRecognizer() {
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0)

        if (_state.value.isSpeaking) {
            Handler(Looper.getMainLooper()).postDelayed({
                recognizer.stopListening()
                recognizer.cancel()
                recognizer.startListening(intent)
            }, 100)
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update {
            it.copy(
                err = null
            )
        }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        if (_state.value.isSpeaking) {
            restartRecognizer()
        } else {
            _state.update {
                it.copy(
                    isSpeaking = false
                )
            }
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
//            return
        } else if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            restartRecognizer()
        }

        _state.update {
            it.copy(
                err = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->

                _state.update { curState ->
                    val text = if (curState.spokenText.isBlank()) result else " $result"

                    curState.copy(
                        spokenText = curState.spokenText.append(text)
                    )
                }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}