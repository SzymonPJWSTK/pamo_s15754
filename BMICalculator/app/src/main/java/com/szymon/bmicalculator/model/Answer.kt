package com.szymon.bmicalculator.model

class Answer(answerText: String, isCorrect: Boolean) {
    private var _answerText: String = ""
    private var _isCorrect: Boolean = false

   init {
       _answerText = answerText
       _isCorrect = isCorrect
   }
    fun getAnswerText(): String {
        return _answerText
    }

    fun getIsCorrect(): Boolean? {
        return _isCorrect
    }
}