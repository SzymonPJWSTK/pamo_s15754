package com.pjatk.bmi_2;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public enum COMPARTMENT {UNDER_WEIGHT,OVER_WEIGHT,V_OVER_WEIGHT,NORMAL_WEIGHT}

    private int _age;
    private double _weight = 0.0;
    private double _height = 0.0;
    private boolean _isFemale;

    private TextView _bmiOut;
    private TextView _compartmentOut;
    private TextView _ppmOut;

    private ConstraintLayout _homeScreen;
    private ConstraintLayout _calculatorScreen;
    private ConstraintLayout _mifflinScreen;

    private ImageView _mealImageView;

    private Map<COMPARTMENT, String> _compartmentStrings;
    private Map<COMPARTMENT, Integer> _compartmentImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    _homeScreen.setVisibility(View.VISIBLE);
                    _calculatorScreen.setVisibility(View.INVISIBLE);
                    _mifflinScreen.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_bmi:
                    _homeScreen.setVisibility(View.INVISIBLE);
                    _calculatorScreen.setVisibility(View.VISIBLE);
                    _mifflinScreen.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_miffiln:
                    _homeScreen.setVisibility(View.INVISIBLE);
                    _calculatorScreen.setVisibility(View.INVISIBLE);
                    _mifflinScreen.setVisibility(View.VISIBLE);
                    CalculateMifflin();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _compartmentStrings = new HashMap<COMPARTMENT,String>();
        _compartmentStrings.put(COMPARTMENT.NORMAL_WEIGHT, getResources().getString(R.string.compartment_normal_weight));
        _compartmentStrings.put(COMPARTMENT.UNDER_WEIGHT, getResources().getString(R.string.compartment_under_weight));
        _compartmentStrings.put(COMPARTMENT.OVER_WEIGHT, getResources().getString(R.string.compartment_over_weight));
        _compartmentStrings.put(COMPARTMENT.V_OVER_WEIGHT,getResources().getString(R.string.compartment_v_over_weight));

        _compartmentImage = new HashMap<COMPARTMENT,Integer>();
        _compartmentImage.put(COMPARTMENT.NORMAL_WEIGHT, R.drawable.schabowy);
        _compartmentImage.put(COMPARTMENT.UNDER_WEIGHT, R.drawable.duzy);
        _compartmentImage.put(COMPARTMENT.OVER_WEIGHT, R.drawable.zdrowe);
        _compartmentImage.put(COMPARTMENT.V_OVER_WEIGHT,R.drawable.salatka);

        _isFemale = false;
        _homeScreen = (ConstraintLayout) findViewById(R.id.welcomeScreen);
        _calculatorScreen = (ConstraintLayout) findViewById(R.id.bmiCalculatorScreen);
        _mifflinScreen = (ConstraintLayout) findViewById(R.id.mifflinScreen);
        _bmiOut = (TextView) findViewById(R.id.bmiOutputTextView);
        _compartmentOut = (TextView) findViewById(R.id.compartmenOutTextView);
        _ppmOut = (TextView) findViewById(R.id.mifflinOutTextView);
        _mealImageView = (ImageView) findViewById(R.id.mealImageView);


        EditText weightEdit = (EditText) findViewById(R.id.weightEditText);
        EditText heightEdit = (EditText) findViewById(R.id.heightEditText);
        EditText ageEdit = (EditText) findViewById(R.id.ageEditText);
        Switch sexSwitch = (Switch) findViewById(R.id.sexSwitch);

        weightEdit.addTextChangedListener(weightEditTextWatcher);
        heightEdit.addTextChangedListener(heightEditTextWatcher);
        ageEdit.addTextChangedListener(ageEditTextWatcher);
        sexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                _isFemale = isChecked;
                CalculateMifflin();
            }
        });

        _homeScreen.setVisibility(View.VISIBLE);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private  void CalculateMifflin(){
        if(_height == 0.0 || _weight == 0.0 || _age == 0){
            _ppmOut.setText(R.string.fill_alld_data);
            return;
        }

        int s = _isFemale == true? -161 : 5;
        double ppm = (10 * _weight) + (6.25 * (_height * 100)) - (_age * 5) + s;
        _ppmOut.setText(getResources().getString(R.string.ppm) + ppm);
    }

    private  void CalculateBmi(){
        if(_height == 0.0 || _weight == 0.0)
        {
            _bmiOut.setText(getResources().getString(R.string.bmi) + " "  + getResources().getString(R.string.fill_alld_data));
            _compartmentOut.setText(getResources().getString(R.string.compartment) + " "  + getResources().getString(R.string.fill_alld_data));
            _mealImageView.setVisibility(View.INVISIBLE);
            return;
        }

        double bmi = Math.round((_weight/Math.pow(_height,2)) * 100.0)/100.0;
        _bmiOut.setText(getResources().getString(R.string.bmi) + " " + bmi);
        COMPARTMENT compartment = getCompartment(bmi);
        _compartmentOut.setText(getResources().getString(R.string.compartment) + " " + _compartmentStrings.get(compartment));
        _mealImageView.setImageDrawable(getResources().getDrawable(_compartmentImage.get(compartment)));
        _mealImageView.setVisibility(View.VISIBLE);
    }

    private final TextWatcher ageEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                _age = Integer.parseInt(s.toString());
            }catch(NumberFormatException e){
                _age = 0;
            }

            CalculateMifflin();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher weightEditTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
                _weight = Double.parseDouble(s.toString());
            }catch(NumberFormatException e){
                _weight = 0.0;
            }

            CalculateBmi();
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

            CalculateBmi();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private COMPARTMENT getCompartment(double bmi){
        if(bmi >= 30.0) {
            return COMPARTMENT.V_OVER_WEIGHT;
        }else if(bmi < 30.0 && bmi >= 25.0){
            return COMPARTMENT.OVER_WEIGHT;
        }else if(bmi < 25.0 && bmi >= 18.5){
            return COMPARTMENT.NORMAL_WEIGHT;
        }else{
            return COMPARTMENT.UNDER_WEIGHT;
        }
    }
}