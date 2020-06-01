package com.example.hsetestwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun convert(view: View) {
        try {
        val from = editTextNumber.text.toString()
        val res = from.toDouble() / 74.0
        Res.setText(res.toString())
        }
        catch (e: Exception) {
            val toast = Toast.makeText(this, "Ошибка: пустое поле ввода", Toast.LENGTH_SHORT)
            toast.show()
        }

    }
}