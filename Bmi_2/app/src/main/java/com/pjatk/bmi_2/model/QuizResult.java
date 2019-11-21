package com.pjatk.bmi_2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.pjatk.bmi_2.R;

import java.util.Date;

@Entity(tableName = "quizresult")
public class QuizResult {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "correct")
    public int correct;
    @ColumnInfo(name = "wrong")
    public int wrong;
    @ColumnInfo(name = "percent")
    public int percent;
    @ColumnInfo(name = "date")
    public Date date;

    public QuizResult(){

    }

    public QuizResult(int correct, int wrong)
    {
        this.date = new Date();
        CalculatePercent(correct,wrong);
    }

    private void CalculatePercent(int correct,int wrong){
        this.correct = correct;
        this.wrong = wrong;
        int sum = correct + wrong;
        double p = (double)correct/(double) sum;
        p *= 100;
        percent = (int)p;
    }

    public String getColor(){
        if(percent >= 51)
            return "#4CAF50";
        else
            return "#E91E36";
    }
}
