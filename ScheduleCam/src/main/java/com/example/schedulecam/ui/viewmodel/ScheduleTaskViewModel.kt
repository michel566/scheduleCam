package com.example.schedulecam.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ScheduleTaskViewModel: ViewModel() {
    private var job: Job? = null
    private val _timeElapsed = MutableLiveData<Int>()
    val timeElapsed: LiveData<Int> get() = _timeElapsed

    companion object{
        private const val DELAY_MILLIS = 1000L
    }

    fun startTarefa(totalTimeCountMillis: Long, task: () -> Unit) {
        var secondsElapsed = 0
        job = viewModelScope.launch {
            var totalTimeTask = 0L

            while (isActive) {
                _timeElapsed.postValue(secondsElapsed)
                secondsElapsed++
                // Sua tarefa agendada aqui

                // Espera 1 segundo (1000 ms)
                totalTimeTask += DELAY_MILLIS
                delay(DELAY_MILLIS)

                if (totalTimeTask == totalTimeCountMillis) {
                    task.invoke()
                    secondsElapsed = 0
                    totalTimeTask = 0L
                    Log.d("TarefaAgendada", "Executando tarefa agendada a cada minuto")
                }
            }
        }
    }

    fun stopTarefa() {
        job?.cancel()
    }
}