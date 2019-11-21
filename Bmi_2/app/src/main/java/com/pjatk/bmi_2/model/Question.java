package com.pjatk.bmi_2.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Question {

    private String _questionText;
    private ArrayList<Answer> _answers = new ArrayList<>();

    public  Question (JSONObject question) throws JSONException {
        _questionText = question.getString("question");
        JSONArray answersArray = question.getJSONArray("answers");
        for(int i = 0; i < answersArray.length(); i++){
            JSONObject object = answersArray.getJSONObject(i);
            _answers.add(new Answer(object.getString("text"),object.getBoolean("isCorrect")));
        }

        Collections.shuffle(_answers);
    }

    public String getQuestionText(){
        return _questionText;
    }

    public ArrayList<Answer> getAnswers(){
        return _answers;
    }
}
