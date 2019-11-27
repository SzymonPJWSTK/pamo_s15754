package com.szymon.bmicalculator

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.TextView

import com.szymon.bmicalculator.db.AppDatabase
import com.szymon.bmicalculator.model.Question
import com.szymon.bmicalculator.model.QuizResult

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList
import java.util.Random

class QuizActivity : AppCompatActivity() {
    private var _askedQuestions: Int = 0
    private var _badAnswers: Int = 0
    private var _correctAnswers: Int = 0
    private var _questionNumberDisplay: TextView? = null
    private var _questions: JSONArray? = null
    private var _quizResults: MutableList<QuizResult> = ArrayList<QuizResult>()
    private var appDatabase: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(this)
        _quizResults = (appDatabase?.quizDao()?.all as MutableList<QuizResult>?)!!

        setContentView(R.layout.activity_quiz)
        _questionNumberDisplay = findViewById(R.id.quizQuestionNumber)
        _askedQuestions = 0

        try {
            val questionsReader = JSONObject(readJsonFileToString("quiz.json"))
            _questions = questionsReader.getJSONArray("questions")
            newQuestion()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    @Throws(JSONException::class)
    private fun newQuestion() {
        if (_askedQuestions == QUIZ_LENGTH) {
            endQuiz()
            return
        }

        val questionIndex = Random().nextInt(_questions!!.length() - _askedQuestions) + _askedQuestions
        val questionToAsk = Question(_questions!!.getJSONObject(questionIndex))
        swapQuestion(questionIndex, _askedQuestions)
        _askedQuestions++

        val questionFragment = QuestionFragment()
        questionFragment.SetArguments(questionToAsk, this)
        replaceFragment(questionFragment)

        updateQuestionNumber(_askedQuestions, QUIZ_LENGTH)
    }

    fun onQuestionCorrect() {
        _correctAnswers++

        try {
            newQuestion()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun onQuestionBad() {
        _badAnswers++
    }

    private fun updateQuestionNumber(questionNumber: Int, maxQuestions: Int) {
        _questionNumberDisplay!!.text = (resources.getString(R.string.questionTItle_begining)
                + " " + questionNumber + " " +
                resources.getString(R.string.questionTItle_from)
                + " " + maxQuestions)
    }

    @Throws(JSONException::class)
    private fun swapQuestion(previousIndex: Int, newIndex: Int) {
        val temp = _questions!!.getJSONObject(previousIndex)
        _questions!!.put(previousIndex, _questions!!.getJSONObject(newIndex))
        _questions!!.put(newIndex, temp)
    }

    private fun replaceFragment(newFragment: Fragment) {
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsHolder, newFragment)
        fragmentTransaction.commit()
    }

    private fun endQuiz() {
        _questionNumberDisplay!!.text = resources.getString(R.string.quizEnd)
        val summaryFragment = QuizSummaryFragment()
        val quizResult = QuizResult(_correctAnswers, _badAnswers)
        _quizResults.add(quizResult)
        appDatabase!!.quizDao().insertAll(quizResult)

        summaryFragment.Init(this, quizResult)
        replaceFragment(summaryFragment)
    }

    fun showChart() {
        val myWebView : WebView = findViewById(R.id.webview)
        myWebView.setVisibility(View.VISIBLE)
        val webSettings = myWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)

        var htmlData = ("<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.charts.load('current', {'packages':['corechart']});"
                + "      google.charts.setOnLoadCallback(drawChart);"

                + "      function drawChart() {"

                + "        var data = google.visualization.arrayToDataTable(["
                + "         ['Data', 'Wynik', { role: 'style' }],")

        for (i in _quizResults.indices) {
            htmlData += "['" + _quizResults[i].date + "', " + _quizResults[i].percent + ", '" + _quizResults[i].color + "']"

            if (i != _quizResults.size - 1) {
                htmlData += ","
            }
        }

        htmlData += ("        ]);"
                + "        var options = {"
                + "          title: 'Wyniki quizu w czasie'"
                + "        };"

                + "        var chart = new google.visualization.BarChart(document.getElementById('piechart'));"

                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"piechart\" style=\"width: 900px; height: 500px;\"></div>"
                + "  </body>"
                + "</html>")

        Log.d("html", htmlData)
        myWebView.loadData(htmlData, "text/html", "UTF-8")
    }

    fun `return`() {
        finish()
    }

    private fun readJsonFileToString(path: String): String? {
        var json: String? = null

        try {
            val `is` = assets.open(path)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return json
    }

    companion object {

        private const val QUIZ_LENGTH = 5
    }
}
