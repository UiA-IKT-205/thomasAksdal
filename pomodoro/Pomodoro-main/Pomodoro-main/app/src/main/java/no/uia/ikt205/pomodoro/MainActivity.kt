package no.uia.ikt205.pomodoro

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliSeconds
import no.uia.ikt205.pomodoro.util.secondsToMilliSeconds

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var changeDurationSeekBar:SeekBar
    lateinit var editRepetitionsTextInput:EditText

    lateinit var countdownDisplay:TextView
    lateinit var countdownText:TextView

    var timeToCountDownInMs = 900000L //15 minutes
    var timeToCountDownInMinutes=15L // a variable to store the chosen duration for next repetition
    val timeTicks = 1000L

    var numberOfRepetitions =0 //default 0 repetitions
    val timeForBreakInMs = 900000L //15 Minutes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)
        }

        countdownDisplay = findViewById<TextView>(R.id.countDownView)
        countdownDisplay.setText(millisecondsToDescriptiveTime(minutesToMilliSeconds(15)))

        countdownText = findViewById(R.id.countdownExplenationText)


        editRepetitionsTextInput = findViewById<EditText>(R.id.numberOfRepetitionsEditText)



        changeDurationSeekBar = findViewById<SeekBar>(R.id.changeTimerseekBar)
        changeDurationSeekBar.max = 300
        changeDurationSeekBar.min = 15
        print("updated min/max values")

        changeDurationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                timeToCountDownInMs = minutesToMilliSeconds(i.toLong())
                timeToCountDownInMinutes=i.toLong()
                updateCountDownDisplay(timeToCountDownInMs)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    fun startCountDown(v: View){
        numberOfRepetitions=editRepetitionsTextInput.text.toString().toInt()

        countdownText.setText("Number of Repetitions left: $numberOfRepetitions")

        //disables all inputs
        startButton.isEnabled = false
        editRepetitionsTextInput.isEnabled = false
        changeDurationSeekBar.isEnabled = false

        if(numberOfRepetitions>=0){

            timer = object : CountDownTimer(timeToCountDownInMs, timeTicks) {

                override fun onFinish() {
                    if(numberOfRepetitions==0){
                        Toast.makeText(this@MainActivity, "Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()

                        //enables all inputs
                        startButton.isEnabled=true
                        editRepetitionsTextInput.isEnabled = true
                        changeDurationSeekBar.isEnabled = true

                    }else{
                        startTimeForBreakCountDown(v)
                        numberOfRepetitions--
                    }


                }

                override fun onTick(millisUntilFinished: Long) {
                    updateCountDownDisplay(millisUntilFinished)
                }
            }

            timer.start()
            Toast.makeText(this@MainActivity, "Remaining repetitions: $numberOfRepetitions", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(this@MainActivity, "Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            startButton.isEnabled=true
        }


    }
    fun startTimeForBreakCountDown(v: View){
        countdownText.setText("Pause for 15 min. $numberOfRepetitions repetitions left")


        timer = object : CountDownTimer(timeForBreakInMs, timeTicks) {

            override fun onFinish() {
                startCountDown(v)
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
    }



    fun updateCountDownDisplay(timeInMs: Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}