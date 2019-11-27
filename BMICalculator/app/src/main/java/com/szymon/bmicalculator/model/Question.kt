package com.szymon.bmicalculator.model

import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Question(question: JSONObject) {
    private var _questionText: String = ""
    private val _answers = ArrayList<Answer>()

    init{
        _questionText = question.getString("question")
        val answersArray = question.getJSONArray("answers")
        for (i in 0 until answersArray.length()) {
            val `object` = answersArray.getJSONObject(i)
            _answers.add(Answer(`object`.getString("text"), `object`.getBoolean("isCorrect")))
        }

        _answers.shuffle()
    }


    fun getQuestionText(): String {
        return _questionText
    }

    fun getAnswers(): ArrayList<Answer> {
        return _answers
    }
}