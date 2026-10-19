package com.example.pr8_grats

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Объявляем переменные для элементов UI и хелпера БД
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etSubject: EditText
    private lateinit var etGrade: EditText
    private lateinit var tvDisplayData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация базы данных
        dbHelper = DatabaseHelper(this)

        // Находим элементы на экране
        etSubject = findViewById(R.id.etSubject)
        etGrade = findViewById(R.id.etGrade)
        tvDisplayData = findViewById(R.id.tvDisplayData)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnView: Button = findViewById(R.id.btnView)

        // Обработчик нажатия на кнопку "Сохранить"
        btnSave.setOnClickListener {
            val subject = etSubject.text.toString().trim()
            val grade = etGrade.text.toString().trim()

            // Проверка на пустые поля
            if (subject.isEmpty() || grade.isEmpty()) {
                Toast.makeText(this, R.string.toast_fill_all, Toast.LENGTH_SHORT).show()
            } else {
                // Записываем данные в БД
                val isInserted = dbHelper.insertData(subject, grade)
                if (isInserted) {
                    Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show()
                    // Очищаем поля после успешного сохранения
                    etSubject.text.clear()
                    etGrade.text.clear()
                } else {
                    Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Обработчик нажатия на кнопку "Показать данные"
        btnView.setOnClickListener {
            val data = dbHelper.getAllData()
            if (data.isEmpty()) {
                tvDisplayData.text = getString(R.string.tv_empty_db)
            } else {
                tvDisplayData.text = getString(R.string.tv_data_title) + data
            }
        }
    }
}