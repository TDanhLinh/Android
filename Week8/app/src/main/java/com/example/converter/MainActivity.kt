package com.example.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editView1: EditText
    private lateinit var editView2: EditText
    private lateinit var currencySpinner1: Spinner
    private lateinit var currencySpinner2: Spinner
    private lateinit var currencyUnit1: TextView
    private lateinit var currencyUnit2: TextView

    // Exchange rates with USD as the base currency
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.922008,
        "GBP" to 0.768636,
        "INR" to 84.084349,
        "AUD" to 1.517471,
        "CAD" to 1.391510,
        "SGD" to 1.322578,
        "CHF" to 0.866620,
        "MYR" to 4.380553,
        "JPY" to 153.075709,
        "CNY" to 7.124875,
        "VND" to 25291.0
    )

    private val currencyList = exchangeRates.keys.toList()
    private var isUpdating = false  // Flag to prevent infinite loop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        editView1 = findViewById(R.id.editView1)
        editView2 = findViewById(R.id.editView2)
        currencySpinner1 = findViewById(R.id.currencySpinner1)
        currencySpinner2 = findViewById(R.id.currencySpinner2)
        currencyUnit1 = findViewById(R.id.currencyUnit1)
        currencyUnit2 = findViewById(R.id.currencyUnit2)

        // Set up currency spinners with options
        setupCurrencySpinners()

        // Set default selections for USD and VND
        currencySpinner1.setSelection(currencyList.indexOf("USD"))
        currencySpinner2.setSelection(currencyList.indexOf("VND"))

        // Update currency units on spinner selection
        currencySpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currencyUnit1.text = currencySpinner1.selectedItem.toString()
                convertCurrency(editView1, editView2, true)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        currencySpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                currencyUnit2.text = currencySpinner2.selectedItem.toString()
                convertCurrency(editView2, editView1, false)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Add text change listeners for both EditTexts
        editView1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) convertCurrency(editView1, editView2, true)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editView2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) convertCurrency(editView2, editView1, false)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCurrencySpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner1.adapter = adapter
        currencySpinner2.adapter = adapter
    }

    private fun convertCurrency(inputField: EditText, outputField: EditText, isEditView1: Boolean) {
        val inputAmount = inputField.text.toString().toDoubleOrNull()
        if (inputAmount == null) {
            outputField.setText("")  // Clear output if input is invalid
            return
        }

        // Select currency based on the active input field
        val fromCurrency = if (isEditView1) currencySpinner1.selectedItem.toString() else currencySpinner2.selectedItem.toString()
        val toCurrency = if (isEditView1) currencySpinner2.selectedItem.toString() else currencySpinner1.selectedItem.toString()

        // Get conversion rates for selected currencies
        val fromRate = exchangeRates[fromCurrency] ?: return
        val toRate = exchangeRates[toCurrency] ?: return

        // Prevent infinite updates by setting a flag
        isUpdating = true
        val convertedAmount = inputAmount * (toRate / fromRate)
        outputField.setText("%.2f".format(convertedAmount))
        isUpdating = false  // Reset flag

        Log.d("Conversion", "Converted $inputAmount $fromCurrency to $convertedAmount $toCurrency")
    }
}
