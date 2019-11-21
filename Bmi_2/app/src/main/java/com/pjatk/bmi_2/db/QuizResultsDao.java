package com.pjatk.bmi_2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.pjatk.bmi_2.model.QuizResult;

import java.util.List;

@Dao
public interface QuizResultsDao {
    @Query("SELECT * FROM quizresult")
    List<QuizResult> getAll();

    @Insert
    void insertAll(QuizResult quizResult);
}
