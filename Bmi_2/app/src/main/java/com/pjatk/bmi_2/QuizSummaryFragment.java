package com.pjatk.bmi_2;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.webkit.WebSettings;

import com.pjatk.bmi_2.model.QuizResult;

public class QuizSummaryFragment extends Fragment  {

    private QuizActivity _quizActivity;
    private QuizResult _quizResult;

    private TextView _correctAnswersText;
    private TextView _wrongAnswersText;
    private TextView _sumText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_quiz_summary, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        _correctAnswersText = view.findViewById(R.id.correctAnswers);
        _wrongAnswersText = view.findViewById(R.id.wrongAnswers);
        _sumText = view.findViewById(R.id.sum);
        Button returnBtn = view.findViewById(R.id.returnToMain);
        returnBtn.setOnClickListener(returnClicked);
        Button chart = view.findViewById(R.id.showChart);
        chart.setOnClickListener(showChart);
        _correctAnswersText.setText(getResources().getString(R.string.correctAnswers ) + " " + _quizResult.correct);
        _wrongAnswersText.setText(getResources().getString(R.string.wrongAnswers) + " " + _quizResult.wrong);
        _sumText.setText(getResources().getString(R.string.sum) + " " + _quizResult.percent + "%");
    }

    public void Init(QuizActivity quizActivity, QuizResult quizResult) {
        _quizActivity = quizActivity;
        _quizResult = quizResult;
    }

    private View.OnClickListener returnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _quizActivity.Return();
        }
    };

    private View.OnClickListener showChart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _quizActivity.ShowChart();
        }
    };
}
