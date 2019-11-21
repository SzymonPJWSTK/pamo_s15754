package com.pjatk.bmi_2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.pjatk.bmi_2.db.AppDatabase;
import com.pjatk.bmi_2.model.Question;
import com.pjatk.bmi_2.model.QuizResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

     private static final int QUIZ_LENGTH = 5;
     private int _askedQuestions;
     private int _badAnswers;
     private int _correctAnswers;
     private TextView _questionNumberDisplay;
     private JSONArray _questions;
     private List<QuizResult> _quizResults = new ArrayList<QuizResult>();
     private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDatabase = AppDatabase.getInstance(this);
        _quizResults = appDatabase.quizDao().getAll();

        setContentView(R.layout.activity_quiz);
        _questionNumberDisplay = findViewById(R.id.quizQuestionNumber);
        _askedQuestions = 0;

        try {
            JSONObject questionsReader = new JSONObject(ReadJsonFileToString("quiz.json"));
            _questions = questionsReader.getJSONArray("questions");
            NewQuestion();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void NewQuestion() throws JSONException {
        if(_askedQuestions == QUIZ_LENGTH) {
            EndQuiz();
            return;
        }

        int questionIndex = new Random().nextInt(_questions.length() - _askedQuestions) + _askedQuestions;
        Question questionToAsk = new Question(_questions.getJSONObject(questionIndex));
        SwapQuestion(questionIndex,_askedQuestions);
        _askedQuestions++;

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.SetArguments(questionToAsk,this);
        ReplaceFragment(questionFragment);

        UpdateQuestionNumber(_askedQuestions, QUIZ_LENGTH);
    }

    public void OnQuestionCorrect(){
        _correctAnswers++;

        try {
            NewQuestion();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void OnQuestionBad(){
        _badAnswers++;
    }

    private void UpdateQuestionNumber(int questionNumber, int maxQuestions){
        _questionNumberDisplay.setText(getResources().getString(R.string.questionTItle_begining)
                + " "  + questionNumber + " " +
                getResources().getString(R.string.questionTItle_from)
                + " " + maxQuestions);
    }

    private void SwapQuestion(int previousIndex, int newIndex) throws JSONException {
        JSONObject temp = _questions.getJSONObject(previousIndex);
        _questions.put(previousIndex, _questions.getJSONObject(newIndex));
        _questions.put(newIndex,temp);
    }

    private void ReplaceFragment(Fragment newFragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsHolder, newFragment);
        fragmentTransaction.commit();
    }

    private void EndQuiz(){
        _questionNumberDisplay.setText(getResources().getString(R.string.quizEnd));
        QuizSummaryFragment summaryFragment = new QuizSummaryFragment();
        QuizResult quizResult = new QuizResult(_correctAnswers,_badAnswers);
        _quizResults.add(quizResult);
        appDatabase.quizDao().insertAll(quizResult);

        summaryFragment.Init(this,quizResult);
        ReplaceFragment(summaryFragment);
    }

    public void ShowChart() {

        WebView myWebView = findViewById(R.id.webview);
        myWebView.setVisibility(View.VISIBLE);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String htmlData = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.charts.load('current', {'packages':['corechart']});"
                + "      google.charts.setOnLoadCallback(drawChart);"

                + "      function drawChart() {"

                + "        var data = google.visualization.arrayToDataTable(["
                + "         ['Data', 'Wynik', { role: 'style' }],";

        for (int i = 0; i < _quizResults.size(); i++) {
            htmlData += "['" + _quizResults.get(i).date + "', " + _quizResults.get(i).percent + ", '" + _quizResults.get(i).getColor() + "']";

            if (i != _quizResults.size() - 1) {
                htmlData += ",";
            }
        }

        htmlData += "        ]);"
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
                + "</html>";

        Log.d("html", htmlData);
        myWebView.loadData(htmlData, "text/html", "UTF-8");
    }

    public void Return(){
        finish();
    }

    private  String ReadJsonFileToString(String path){
        String json = null;

        try {
            InputStream is = getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }
}
