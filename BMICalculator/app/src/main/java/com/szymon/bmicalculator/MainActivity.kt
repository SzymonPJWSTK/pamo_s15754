package com.szymon.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    enum class COMPARTMENT {
        UNDER_WEIGHT, OVER_WEIGHT, V_OVER_WEIGHT, NORMAL_WEIGHT
    }

    private var _age = 0
    private var _weight = 0.0
    private var _height = 0.0
    private var _isFemale: Boolean = false
    private var _bmiOut: TextView? = null
    private var _compartmentOut: TextView? = null
    private var _ppmOut: TextView? = null

    private var _homeScreen: ConstraintLayout? = null
    private var _calculatorScreen: ConstraintLayout? = null
    private var _mifflinScreen: ConstraintLayout? = null

    private var _mealImageView: ImageView? = null

    private var _compartmentStrings: MutableMap<COMPARTMENT, String>? = null
    private var _compartmentImage: MutableMap<COMPARTMENT, Int>? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var result = false
        when (item.itemId) {
            R.id.navigation_home -> {
                _homeScreen?.visibility = View.VISIBLE
                _calculatorScreen?.visibility = View.INVISIBLE
                _mifflinScreen?.visibility = View.INVISIBLE
                result = true
            }
            R.id.navigation_bmi -> {
                _homeScreen?.visibility = View.INVISIBLE
                _calculatorScreen?.visibility = View.VISIBLE
                _mifflinScreen?.visibility = View.INVISIBLE
               result = true
            }
            R.id.navigation_miffiln -> {
                _homeScreen?.visibility = View.INVISIBLE
                _calculatorScreen?.visibility = View.INVISIBLE
                _mifflinScreen?.visibility = View.VISIBLE
                calculateMifflin()
                result = true
            }
        }
         result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _compartmentStrings =  HashMap()
        _compartmentStrings?.put(COMPARTMENT.NORMAL_WEIGHT,resources.getString(R.string.compartment_normal_weight))
        _compartmentStrings?.put(COMPARTMENT.UNDER_WEIGHT,resources.getString(R.string.compartment_under_weight))
        _compartmentStrings?.put(COMPARTMENT.OVER_WEIGHT,resources.getString(R.string.compartment_over_weight))
        _compartmentStrings?.put(COMPARTMENT.V_OVER_WEIGHT,resources.getString(R.string.compartment_v_over_weight))

        _compartmentImage = HashMap()
        _compartmentImage?.put(COMPARTMENT.NORMAL_WEIGHT, R.drawable.schabowy)
        _compartmentImage?.put(COMPARTMENT.UNDER_WEIGHT,R.drawable.duzy)
        _compartmentImage?.put(COMPARTMENT.OVER_WEIGHT,R.drawable.zdrowe)
        _compartmentImage?.put(COMPARTMENT.V_OVER_WEIGHT,R.drawable.salatka)

        _isFemale = false
        _homeScreen = findViewById(R.id.welcomeScreen)
        _calculatorScreen = findViewById(R.id.bmiCalculatorScreen)
        _mifflinScreen = findViewById(R.id.mifflinScreen)
        _bmiOut = findViewById(R.id.bmiOutputTextView)
        _compartmentOut = findViewById(R.id.compartmenOutTextView)
        _ppmOut = findViewById(R.id.mifflinOutTextView)
        _mealImageView = findViewById(R.id.mealImageView)

        val weightEdit = findViewById<EditText>(R.id.weightEditText)
        val heightEdit = findViewById<EditText>(R.id.heightEditText)
        val ageEdit = findViewById<EditText>(R.id.ageEditText)
        val sexSwitch = findViewById<Switch>(R.id.sexSwitch)
        val startQuizBtn = findViewById<Button>(R.id.startQuizBtn)
        startQuizBtn.setOnClickListener(startQuizListener)


        weightEdit.addTextChangedListener(weightEditTextWatcher)
        heightEdit.addTextChangedListener(heightEditTextWatcher)
        ageEdit.addTextChangedListener(ageEditTextWatcher)
        sexSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _isFemale = isChecked
            calculateMifflin()
        }



        _homeScreen?.visibility = View.VISIBLE
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun calculateMifflin() {
        if (_height == 0.0 || _weight == 0.0 || _age == 0) {
            _ppmOut?.setText(R.string.fill_alld_data)
            return
        }

        val s = if (_isFemale) -161 else 5
        val ppm = 10 * _weight + 6.25 * (_height * 100) - _age * 5 + s
        _ppmOut?.text = resources.getString(R.string.ppm) + ppm
    }

    private fun calculateBmi() {
        if (_height == 0.0 || _weight == 0.0) {
            _bmiOut?.text = resources.getString(R.string.bmi) + " " + resources.getString(R.string.fill_alld_data)
            _compartmentOut?.text = resources.getString(R.string.compartment) + " " + resources.getString(R.string.fill_alld_data)
            _mealImageView?.visibility = View.INVISIBLE
            return
        }

        val bmi = Math.round(_weight / Math.pow(_height, 2.0) * 100.0) / 100.0
        _bmiOut?.text = resources.getString(R.string.bmi) + " " + bmi
        val compartment = getCompartment(bmi)
        _compartmentOut?.text = resources.getString(R.string.compartment) + " " + _compartmentStrings?.get(compartment)
        _mealImageView?.setImageDrawable(resources.getDrawable(_compartmentImage?.get(compartment)!!))
        _mealImageView?.visibility = View.VISIBLE
    }

    private fun getCompartment(bmi: Double): COMPARTMENT {
        return if (bmi >= 30.0) {
            COMPARTMENT.V_OVER_WEIGHT
        } else if (bmi < 30.0 && bmi >= 25.0) {
            COMPARTMENT.OVER_WEIGHT
        } else if (bmi < 25.0 && bmi >= 18.5) {
            COMPARTMENT.NORMAL_WEIGHT
        } else {
            COMPARTMENT.UNDER_WEIGHT
        }
    }

    private val ageEditTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            try {
                _age = Integer.parseInt(s.toString())
            } catch (e: NumberFormatException) {
                _age = 0
            }

            calculateMifflin()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

    private val weightEditTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            try {
                _weight = java.lang.Double.parseDouble(s.toString())
            } catch (e: NumberFormatException) {
                _weight = 0.0
            }

            calculateBmi()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

    private val heightEditTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            try {
                _height = java.lang.Double.parseDouble(s.toString())
            } catch (e: NumberFormatException) {
                _height = 0.0
            }

            calculateBmi()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

    private val startQuizListener = View.OnClickListener {
        val quizActivity = Intent(applicationContext, QuizActivity::class.java)
        startActivity(quizActivity)
    }
}
