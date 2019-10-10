package com.pczapiewski.pjatk.mytipproject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends Activity {

    private double _weight = 0.0;
    private double _height = 0.0;

    private TextView _bmiOut;
    private TextView _compartmentOut;


    // called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call superclass onCreate
        setContentView(R.layout.activity_main); // inflate the GUI

        _bmiOut = (TextView) findViewById(R.id.bmiOutputTextView);
        _compartmentOut = (TextView) findViewById(R.id.compartmenOutTextView);

        EditText weightEdit = (EditText) findViewById(R.id.weightEditText);
        weightEdit.addTextChangedListener(weightEditTextWatcher);
        EditText heightEdit = (EditText) findViewById(R.id.heightEditText);
        heightEdit.addTextChangedListener(heightEditTextWatcher);
    }

    private  void Calculate(){
        if(_height == 0.0 || _weight == 0.0)
        {
            _bmiOut.setText("BMI:");
            _compartmentOut.setText("Przedział:");
            return;
        }

        double bmi = Math.round((_weight/Math.pow(_height,2)) * 100.0)/100.0;
        _bmiOut.setText("BMI: " + bmi);
        _compartmentOut.setText("Przedział: " + getCompartment(bmi));
    }

    private final TextWatcher weightEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                _weight = Double.parseDouble(s.toString());
            }catch(NumberFormatException e){
                _weight = 0.0;
            }

            Calculate();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher heightEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    _height = Double.parseDouble(s.toString());
                }catch(NumberFormatException e){
                    _height = 0.0;
            }

            Calculate();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private String getCompartment(double bmi){
        if(bmi >= 25.0){
            return "Nadwaga";
        }else if(bmi < 25.0 && bmi >= 18.5){
            return "Wartość prawidłowa";
        }else{
            return "Niedowaga";
        }
    }
}
