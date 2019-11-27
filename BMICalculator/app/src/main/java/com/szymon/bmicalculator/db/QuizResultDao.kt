package com.szymon.bmicalculator.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import com.szymon.bmicalculator.model.QuizResult

@Dao
interface QuizResultsDao {
    @get:Query("SELECT * FROM quizresult")
    val all: List<QuizResult>

    @Insert
    fun insertAll(quizResult: QuizResult)
}
