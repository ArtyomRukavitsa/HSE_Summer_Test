package com.example.hsetestwork

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result


class EqualException() : Exception() // Если элементы spinner-ов совпадают при конвертации
class FormatException() : Exception() // Если при конвертации пустое поле ввода



class MainActivity : AppCompatActivity() {
    var From : String = ""
    var To: String = ""
    var Current: Double = 0.0

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

    inner class doAsync() : AsyncTask<Double, Void, Double>() {
        override fun doInBackground(vararg params: Double?): Double? {
            var coef : Double = 0.0
            val result = URL("https://api.exchangeratesapi.io/latest?base=$From").readText()
            var str = result.replace("{\"rates\":{", "")
            str = str.replace("}", "")
            val r = str.split(',')

            for (el in r) {
                if (To in el) coef = el.split(':')[1].toDouble()
            }
            return coef
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Double){
            super.onPostExecute(result)
            Current = result
        }
    }
 //   private fun setCurrent(str: String) {
   //     Current = str.toDouble()
    //}
   // private fun findCoef() : Double {
//        var global : String? = null;
//        "https://api.exchangeratesapi.io/latest?base=$From".httpGet()
//            .responseString { request, response, result ->
//                when (result) {
//                    is Result.Failure -> {
//                        val ex = result.getException()
//                    }
//                    is Result.Success -> {
//                        val data = result.get()
//                        var str = data.replace("{\"rates\":{", "")
//                        str = str.replace("}", "")
//                        val r = str.split(',')
//
//                        for (el in r) {
//                            if (To in el)  textView4.text = el.split(':')[1]
//                        }
//                    }
//                }
//            }
//        return textView4.text.toString().toDouble()
       // var coef : Double = 0.0
        //doAsync {



        //}.execute()
        //return coef
  //  }

    fun convert(view: View) {
        try {
            var res : Double
            val from = editTextNumber.text.toString()
            if (from == "") throw FormatException()
            if (From == To) throw EqualException()
            doAsync().execute()
            res = from.toDouble() * Current
            textView4.setText("Курс: 1 $From = $Current $To")
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