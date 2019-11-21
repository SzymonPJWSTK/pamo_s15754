package com.pjatk.bmi_2;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pjatk.bmi_2.model.Question;


public class QuestionFragment extends Fragment {
    private Question _question;
    private TextView _questionText;
    private QuizActivity _quizActivity;
    private Button[] _answers = new Button[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        _questionText = view.findViewById(R.id.questionText);
        _answers[0] = view.findViewById(R.id.answer1);
        _answers[1] = view.findViewById(R.id.answer2);
        _answers[2] = view.findViewById(R.id.answer3);
        _answers[3] = view.findViewById(R.id.answer4);

        _questionText.setText(_question.getQuestionText());
        for (int i = 0; i < 4; i++){
            _answers[i].setEnabled(true);
            _answers[i].setText(_question.getAnswers().get(i).getAnswerText());
            _answers[i].setTag(i);
            _answers[i].setOnClickListener(answerClicked);
        }
    }

    public void SetArguments(Question question, QuizActivity quizActivity){
        _question = question;
        _quizActivity = quizActivity;
    }

    private View.OnClickListener answerClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = (int) v.getTag();

            v.setEnabled(false);

            if(_question.getAnswers().get(id).getIsCorrect()){
                _quizActivity.OnQuestionCorrect();
            }else{
                _quizActivity.OnQuestionBad();
            }
        }
    };
}