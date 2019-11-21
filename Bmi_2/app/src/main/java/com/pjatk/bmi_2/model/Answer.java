package com.pjatk.bmi_2.model;

public class Answer {
    private String _answerText;
    private boolean _isCorrect;

    public Answer(String answerText, boolean isCorrect){
        _answerText = answerText;
        _isCorrect = isCorrect;
    }

    public String getAnswerText(){
        return _answerText;
    }

    public  Boolean getIsCorrect(){
        return _isCorrect;
    }
}
