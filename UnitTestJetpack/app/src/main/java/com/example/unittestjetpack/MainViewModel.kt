package com.example.unittestjetpack

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(private val cuboidModel: CuboidModel) {

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun changeText(string: String) {
        _snackbarText.value = Event(string)
    }

    private lateinit var timer: CountDownTimer
    var timerValue = MutableLiveData<Long>()
    var finished = MutableLiveData<Boolean>()
    private val _seconds = MutableLiveData<Int>()
    fun seconds(): LiveData<Int> {
        return _seconds
    }

    fun startTimer() {
        timer = object : CountDownTimer(timerValue.value!!.toLong(), 1000) {
            override fun onFinish() {
                finished.value = true
            }

            override fun onTick(p0: Long) {
                val timeLeft = p0 / 1000
                _seconds.value = timeLeft.toInt()
            }
        }.start()
    }

    fun stopTimer() {
        timer.cancel()
        timerValue.value = 0
        finished.value = true
    }


    fun getCircumference() = cuboidModel.getCircumference()

    fun getSurfaceArea() = cuboidModel.getSurfaceArea()

    fun getVolume() = cuboidModel.getVolume()

    fun save(w: Double, l: Double, h: Double) {
        cuboidModel.save(w, l, h)
    }


}