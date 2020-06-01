package com.example.hsetestwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class EqualException() : Exception() // Если элементы spinner-ов совпадают при конвертации
class FormatException() : Exception() // Если при конвертации пустое поле ввода
class MainActivity : AppCompatActivity() {
    var From : String = ""
    var To: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // access the items of the list
        val currencies = resources.getStringArray(R.array.currencies)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, currencies
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    From = currencies[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        if (spinner2 != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, currencies
            )
            spinner2.adapter = adapter

            spinner2.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    To = currencies[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    fun convert(view: View) {
        try {
            var res : Double
            val from = editTextNumber.text.toString()
            if (from == "") throw FormatException()
            if (From == To) throw EqualException()

            if (From == "USD" && To == "RUB") res = from.toDouble() * 74.0
            else res = from.toDouble() / 74.0

            Res.setText(String.format("%.2f", res))

        }
        catch (e: FormatException) {
            val toast = Toast.makeText(this, "Ошибка: пустое поле ввода", Toast.LENGTH_SHORT)
            toast.show()
            Res.setText("")
        }
        catch (e: EqualException) {
            val toast = Toast.makeText(this, "Ошибка: одинаковые валюты", Toast.LENGTH_SHORT)
            toast.show()
            Res.setText("")
        }

    }
}