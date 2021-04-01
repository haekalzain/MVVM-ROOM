package com.example.unittestjetpack

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.unittestjetpack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainViewModel = MainViewModel(CuboidModel())
        activityMainBinding.btnSave.setOnClickListener(this)
        activityMainBinding.btnCalculateSurfaceArea.setOnClickListener(this)
        activityMainBinding.btnCalculateCircumference.setOnClickListener(this)
        activityMainBinding.btnCalculateVolume.setOnClickListener(this)
//        mainViewModel.snackbarText.observe(this, EventObserver {
//            activityMainBinding.tvTest.text = it
//        })
//        btn_tes_live.setOnClickListener {mainViewModel.changeText(activityMainBinding.etLive.text.toString())}
        mainViewModel.seconds().observe(this, Observer {
            activityMainBinding.tvTest.text = it.toString()
        })
        mainViewModel.finished.observe(this, Observer {
            if (it) {
                activityMainBinding.btnTesStart.isEnabled = true
            }
        })
        activityMainBinding.btnTesStart.setOnClickListener {
            if (activityMainBinding.etTimer.text.toString()
                    .isEmpty() || activityMainBinding.etTimer.text.length < 4
            ) {
                makeText(this, "invalid number", Toast.LENGTH_SHORT).show()
            } else {
                activityMainBinding.btnTesStart.isEnabled = false
                mainViewModel.timerValue.value =
                    activityMainBinding.etTimer.text.toString().toLong()
                mainViewModel.startTimer()
            }
        }
        activityMainBinding.btnTesStop.setOnClickListener {
            activityMainBinding.tvTest.text = "0"
            mainViewModel.stopTimer()
        }
        activityMainBinding.btnToNotes.setOnClickListener {
            val intent = Intent(this@MainActivity, NotesAppActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(v: View) {

        val length = activityMainBinding.edtLength.text.toString().trim()
        val width = activityMainBinding.edtWidth.text.toString().trim()
        val height = activityMainBinding.edtHeight.text.toString().trim()
        when {
            TextUtils.isEmpty(length) -> {
                activityMainBinding.edtLength.error = "Field ini tidak boleh kosong"
            }
            TextUtils.isEmpty(width) -> {
                activityMainBinding.edtWidth.error = "Field ini tidak boleh kosong"
            }
            TextUtils.isEmpty(height) -> {
                activityMainBinding.edtHeight.error = "Field ini tidak boleh kosong"
            }
            else -> {
                val valueLength = length.toDouble()
                val valueWidth = width.toDouble()
                val valueHeight = height.toDouble()
                when (v.id) {
                    R.id.btn_save -> {
                        mainViewModel.save(valueLength, valueWidth, valueHeight)
                        visible()
                    }
                    R.id.btn_calculate_circumference -> {
                        activityMainBinding.tvResult.text =
                            mainViewModel.getCircumference().toString()
                        gone()
                    }
                    R.id.btn_calculate_surface_area -> {
                        activityMainBinding.tvResult.text =
                            mainViewModel.getSurfaceArea().toString()
                        gone()
                    }
                    R.id.btn_calculate_volume -> {
                        activityMainBinding.tvResult.text = mainViewModel.getVolume().toString()
                        gone()
                    }
                }
            }
        }
    }

    private fun visible() {
        activityMainBinding.btnCalculateVolume.visibility = View.VISIBLE
        activityMainBinding.btnCalculateCircumference.visibility = View.VISIBLE
        activityMainBinding.btnCalculateSurfaceArea.visibility = View.VISIBLE
        activityMainBinding.btnSave.visibility = View.GONE
    }

    private fun gone() {
        activityMainBinding.btnCalculateVolume.visibility = View.GONE
        activityMainBinding.btnCalculateCircumference.visibility = View.GONE
        activityMainBinding.btnCalculateSurfaceArea.visibility = View.GONE
        activityMainBinding.btnSave.visibility = View.VISIBLE
    }
}