package com.example.hsetestwork

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL


class EqualException() : Exception() // Если элементы spinner-ов совпадают при конвертации
class FormatException() : Exception() // Если при конвертации пустое поле ввода

// Проверка подключения к сети Интернет
object NetworkManager {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
class MainActivity : AppCompatActivity() {
    var From : String = "RUB"  // Из какой валюты переводим
    var To: String = "USD" // В какую валюту переводим

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currencies = resources.getStringArray(R.array.currencies) // Список валют

        // Первый спиннер (из какой валюты переводим)
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, currencies
            )
            spinner.adapter = adapter
            spinner.setSelection(0) // по дефолту ставлю рубли
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    From = currencies[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        // Второй спиннер (в какую валюту переводим)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        if (spinner2 != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, currencies
            )

            spinner2.adapter = adapter
            spinner2.setSelection(1) // по дефолту ставлю доллары
            spinner2.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    To = currencies[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    // AsyncTask для работы с API курсов валют
    inner class doAsync() : AsyncTask<Double, Void, Double>() {
        lateinit var pd : ProgressDialog

        override fun doInBackground(vararg params: Double?) : Double? {
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
            pd=ProgressDialog.show(this@MainActivity, "", "Checking...", true,
                false);
        }

        override fun onPostExecute(result: Double?) {
            super.onPostExecute(result)
            pd.dismiss()
        }
    }

    // Обработка одного из исключений
    fun catchError(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
        Res.setText("")
        textView4.setText("")
    }

    // Обработка нажатия на кнопку "Convert!"
    fun convert(view: View) {
        try {
            if (NetworkManager.isNetworkAvailable(this@MainActivity))
            {
                val from = editTextNumber.text.toString()
                if (from == "") throw FormatException() // Пытаемся перевести пустое поле
                if (From == To) throw EqualException() // Валюты перевода совпадают
                val my_object = doAsync()
                my_object.execute()
                val r = my_object.get()  // Результат обращения к API
                val res = from.toDouble() * r
                textView4.setText("Курс: 1 $From = ${String.format("%.3f", r)} $To")
                Res.setText(String.format("%.3f", res))
            }
            else Toast.makeText(this@MainActivity, "Проверьте подключение к сети Интернет!", Toast.LENGTH_SHORT).show()
        }
        catch (e: FormatException) {
            catchError("Ошибка: пустое поле ввода")
        }
        catch (e: EqualException) {
            catchError("Ошибка: одинаковые валюты")
        }

    }
}