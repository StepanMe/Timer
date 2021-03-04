package com.example.timer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.minutes

class MainActivity : AppCompatActivity() {
    var currDateTime: LocalDateTime? = null
    var currTime: LocalTime? = null
    var currDate: LocalDate? = null

    var progTimeInSeconds: Long? = null

    var finDateTime: LocalDateTime? = null
    var finTime: LocalTime? = null
    var finDate: LocalDate? = null

    var buttonList: List<TextView>? = null
    var buttonList2: List<TextView>? = null

    lateinit var mode1: TextView
    lateinit var mode2: TextView
    lateinit var mode3: TextView
    lateinit var mode4: TextView
    lateinit var mode5: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mode1 = findViewById(R.id.tVmode1)
        mode2 = findViewById(R.id.tVmode2)
        mode3 = findViewById(R.id.tVmode3)
        mode4 = findViewById(R.id.tVmode4)
        mode5 = findViewById(R.id.tVmode5)

        //"Список" кнопок
        buttonList = listOf(mode1, mode2, mode3, mode4, mode5)

        //Программа по-умолчанию (время в секундах + оформление кнопки)
        progTimeInSeconds = 10080
        mode3.setBackgroundResource(R.drawable.button_background_active)
        mode3.setTextColor(Color.WHITE)

        //Время окончания программы по-умолчанию
        finDate = LocalDate.now().plusDays(1)   //[завтра]
        finTime = LocalTime.of(7, 0, 0) //7:00 утра
        finDateTime = LocalDateTime.of(finDate, finTime)

        //Выводим в поле "Время окончания" текст вида "завтра, 07:00"
        textView11.text = fakeDateTimeString(finDateTime!!)

        val mainButton = findViewById<Button>(R.id.button)
        val timerButton = findViewById<ImageButton>(R.id.imageButton)

    }

    //Нажатие на кнопки (выбор режима)
    fun onModeClick (v: View) {
        when (v.id) {
            R.id.tVmode1 -> {
                progTimeInSeconds = 9720
                mode1.setBackgroundResource(R.drawable.button_background_active)
                mode1.setTextColor(Color.WHITE)
                }
            R.id.tVmode2 -> {
                progTimeInSeconds = 7800
                mode2.setBackgroundResource(R.drawable.button_background_active)
                mode2.setTextColor(Color.WHITE)
            }
            R.id.tVmode3 -> {
                progTimeInSeconds = 10080
                mode3.setBackgroundResource(R.drawable.button_background_active)
                mode3.setTextColor(Color.WHITE)
            }
            R.id.tVmode4 -> {
                progTimeInSeconds = 7620
                mode4.setBackgroundResource(R.drawable.button_background_active)
                mode4.setTextColor(Color.WHITE)
            }
            R.id.tVmode5 -> {
                progTimeInSeconds = 7020
                mode5.setBackgroundResource(R.drawable.button_background_active)
                mode5.setTextColor(Color.WHITE)
            }
        }
        buttonList2 = buttonList!!.filter { it != v }
        buttonList2!!.forEach {
            it.setBackgroundResource(R.drawable.button_background_inactive)
            it.setTextColor(Color.GRAY)
        }
    }

    fun onCalcClick (v: View) {
        var f = finDateTime
        var res = ""
        var res2 = ""
        var diff: Long = 0

        //Из времени окончания вычитаем продолжительность выполнения программы в секундах
        f = f!!.minusSeconds(progTimeInSeconds!!)
        //Считаем разницу по времени
        val d = Duration.between(LocalDateTime.now(), f)

        var d2 = d
        //diff = (d2.toMinutes() % 30).toLong()

        when (d2.toMinutes() % 30) {
            //Если
            in 1..10 -> {}
            in 11..20 -> {}
            in 21..29 -> {}
        }


        if (diff <= 15) {
            res2 = "Реальное время окончания — " + finDateTime!!.minusMinutes(diff).toString()
        } else {
            res2 = "Реальное время окончания2 — " + finDateTime!!.plusMinutes(diff).toString()
        }
        //Toast.makeText(this, (d2.toMinutes() % 30).toString(), Toast.LENGTH_SHORT).show()
        Toast.makeText(this, res2, Toast.LENGTH_LONG).show()
//        d2 = d2.minusMinutes((d2.toMinutes() / 30))
//        Toast.makeText(this, d2.toHours().toString()+":"+ d2.minusHours(d2.toHours()).toMinutes(), Toast.LENGTH_SHORT).show()

        //Если программа успевает выполниться
        if (d.toMinutes() > 0) {
//            res = String.format("%02d:%02d",d.toHours(), d.minusHours(d.toHours()).minusMinutes(d.toMinutes() % 30).toMinutes())
            res = String.format("%02d:%02d",d.toHours(), d.minusHours(d.toHours()).toMinutes())
//            Toast.makeText(this, "Успеваем!", Toast.LENGTH_SHORT).show()
            textView3.text = res
        } else {
            res = "Ой!"
            Toast.makeText(this, getString(R.string.error_time), Toast.LENGTH_LONG).show()
            textView3.text = res
        }
    }

    fun onDateTimeClick(v: View) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                //Дата-время, когда должна закончиться программа
                finDate = LocalDate.of(year, month + 1, day)
                finTime = LocalTime.of(hour, minute)
                finDateTime = LocalDateTime.of(finDate, finTime)
                textView11.text = fakeDateTimeString(finDateTime!!)
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun fakeDateTimeString (ldt: LocalDateTime): String {
        val d = ldt.toLocalDate()
        val p = Period.between(LocalDate.now(), d)
        var t = ""
        when (p.get(ChronoUnit.DAYS).toInt()) {
            0 -> t = "сегодня, "
            1 -> t = "завтра, "
            2 -> t = "послезавтра, "
        }
        t += ldt.toLocalTime()
        return t
    }
}