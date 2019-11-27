package com.szymon.bmicalculator

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.szymon.bmicalculator.model.QuizResult

class QuizSummaryFragment : Fragment() {

    private var _quizActivity: QuizActivity? = null
    private var _quizResult: QuizResult? = null

    private var _correctAnswersText: TextView? = null
    private var _wrongAnswersText: TextView? = null
    private var _sumText: TextView? = null

    private val returnClicked = View.OnClickListener { _quizActivity!!.`return`() }

    private val showChart = View.OnClickListener { _quizActivity!!.showChart() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_quiz_summary_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _correctAnswersText = view.findViewById(R.id.correctAnswers)
        _wrongAnswersText = view.findViewById(R.id.wrongAnswers)
        _sumText = view.findViewById(R.id.sum)
        val returnBtn : Button = view.findViewById(R.id.returnToMain)
        returnBtn.setOnClickListener(returnClicked)
        val chart : Button = view.findViewById(R.id.showChart)
        chart.setOnClickListener(showChart)
        _correctAnswersText!!.text = resources.getString(R.string.correctAnswers) + " " + _quizResult!!.correct
        _wrongAnswersText!!.text = resources.getString(R.string.wrongAnswers) + " " + _quizResult!!.wrong
        _sumText!!.text = resources.getString(R.string.sum) + " " + _quizResult!!.percent + "%"
    }

    fun Init(quizActivity: QuizActivity, quizResult: QuizResult) {
        _quizActivity = quizActivity
        _quizResult = quizResult
    }
}
