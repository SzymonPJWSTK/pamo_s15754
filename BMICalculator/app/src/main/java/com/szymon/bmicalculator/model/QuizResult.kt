package com.szymon.bmicalculator.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "quizresult")
class QuizResult(correct: Int, wrong: Int) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "correct")
    var correct: Int = 0
    @ColumnInfo(name = "wrong")
    var wrong: Int = 0
    @ColumnInfo(name = "percent")
    var percent: Int = 0
    @ColumnInfo(name = "date")
    var date: Date? = null

    val color: String
        get() = if (percent >= 51)
            "#4CAF50"
        else
            "#E91E36"

    init {
        this.date = Date()
        calculatePercent(correct, wrong)
    }

    private fun calculatePercent(correct: Int, wrong: Int) {
        this.correct = correct
        this.wrong = wrong
        val sum = correct + wrong
        var p = correct.toDouble() / sum.toDouble()
        p *= 100.0
        percent = p.toInt()
    }
}