package com.szymon.bmicalculator

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.szymon.bmicalculator.model.Question


class QuestionFragment : Fragment() {
    private var _question: Question? = null
    private var _questionText: TextView? = null
    private var _quizActivity: QuizActivity? = null
    private val _answers = arrayOfNulls<Button>(4)

    private val answerClicked = View.OnClickListener { v ->
        val id = v.tag as Int

        v.isEnabled = false

        if (_question?.getAnswers()?.get(id)?.getIsCorrect()!!) {
            _quizActivity!!.onQuestionCorrect()
        } else {
            _quizActivity!!.onQuestionBad()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_question_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _questionText = view.findViewById(R.id.questionText)
        _answers[0] = view.findViewById(R.id.answer1)
        _answers[1] = view.findViewById(R.id.answer2)
        _answers[2] = view.findViewById(R.id.answer3)
        _answers[3] = view.findViewById(R.id.answer4)

        _questionText!!.setText(_question!!.getQuestionText())
        for (i in 0..3) {
            _answers[i]?.isEnabled = true
            _answers[i]?.text = _question!!.getAnswers()[i].getAnswerText()
            _answers[i]?.tag = i
            _answers[i]?.setOnClickListener(answerClicked)
        }
    }

    fun SetArguments(question: Question, quizActivity: QuizActivity) {
        _question = question
        _quizActivity = quizActivity
    }
}