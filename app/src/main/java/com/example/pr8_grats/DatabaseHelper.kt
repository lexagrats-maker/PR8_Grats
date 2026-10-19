package com.example.pr8_grats

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Класс для управления локальной базой данных SQLite
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GradesData.db" // Имя файла базы данных
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "grades_table" // Имя таблицы
        const val COLUMN_ID = "id"
        const val COLUMN_SUBJECT = "subject" // Поле: Название дисциплины
        const val COLUMN_GRADE = "grade"     // Поле: Оценка
    }

    // Вызывается при первом создании базы данных
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_SUBJECT TEXT, $COLUMN_GRADE TEXT)"
        db?.execSQL(createTableQuery)
    }

    // Вызывается при изменении версии БД (например, при добавлении новых столбцов)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Функция для добавления новой записи
    fun insertData(subject: String, grade: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_SUBJECT, subject)
        contentValues.put(COLUMN_GRADE, grade)

        // insert возвращает ID новой строки или -1, если произошла ошибка
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    // Функция для получения всех записей в виде одной строки для отображения
    fun getAllData(): String {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val stringBuilder = StringBuilder()

        // Проходим по всем строкам таблицы
        if (cursor.moveToFirst()) {
            do {
                val subjectIndex = cursor.getColumnIndex(COLUMN_SUBJECT)
                val gradeIndex = cursor.getColumnIndex(COLUMN_GRADE)

                val subject = cursor.getString(subjectIndex)
                val grade = cursor.getString(gradeIndex)

                // Формируем красивую строку для вывода
                stringBuilder.append("• $subject — $grade\n")
            } while (cursor.moveToNext())
        }
        cursor.close() // Обязательно закрываем курсор!
        return stringBuilder.toString()
    }
}