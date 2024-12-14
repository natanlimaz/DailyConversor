package com.natanlima.dailyconversor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.natanlima.dailyconversor.databinding.ActivityConversorBinding
import kotlin.math.abs
import kotlin.math.pow

class ConversorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversorBinding;

    var resultBundle = "0.0";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversorBinding.inflate(layoutInflater);
        setContentView(binding.root);

        window.apply {
            statusBarColor = ContextCompat.getColor(this@ConversorActivity, R.color.white);
        }

        if(savedInstanceState != null) {
            resultBundle = savedInstanceState.getString("resultBundle").toString();
            binding.tvResult.text = resultBundle;
        }

        // mass spinner
        arrayAdapter(this, R.array.mass_conversion_table, binding.spinnerMassInput, binding.spinnerMassOutput);
        // distance spinner
        arrayAdapter(this, R.array.distance_conversion_table, binding.spinnerDistanceInput, binding.spinnerDistanceOutput);
        // capacity spinner
        arrayAdapter(this, R.array.capacity_conversion_table, binding.spinnerCapacityInput, binding.spinnerCapacityOutput);

        binding.btnConverter.setOnClickListener {
            val massStr = binding.edtMass.text.toString();
            val distanceStr = binding.edtDistance.text.toString();
            val capacityStr = binding.edtCapacity.text.toString();

            if(massStr.isNotEmpty() && distanceStr.isEmpty() && capacityStr.isEmpty()) {
                val mass = massStr.toFloat();

                val convertedValue = calculate(mass, binding.spinnerMassInput, binding.spinnerMassOutput);
                val unitOfMeasurement = binding.spinnerMassOutput.selectedItem.toString();

                val result = getString(R.string.result, convertedValue, unitOfMeasurement);
                binding.tvResult.text = result;
                resultBundle = result;

            } else if(massStr.isEmpty() && distanceStr.isNotEmpty() && capacityStr.isEmpty()) {
                val distance = distanceStr.toFloat();

                val convertedValue = calculate(distance, binding.spinnerDistanceInput, binding.spinnerDistanceOutput);
                val unitOfMeasurement = binding.spinnerDistanceOutput.selectedItem.toString();

                val result = getString(R.string.result, convertedValue, unitOfMeasurement);
                binding.tvResult.text = result;
                resultBundle = result;

            }else if(massStr.isEmpty() && distanceStr.isEmpty() && capacityStr.isNotEmpty()) {
                val capacity = capacityStr.toFloat();

                val convertedValue = calculate(capacity, binding.spinnerCapacityInput, binding.spinnerCapacityOutput);
                val unitOfMeasurement = binding.spinnerMassOutput.selectedItem.toString();

                val result = getString(R.string.result, convertedValue, unitOfMeasurement);
                binding.tvResult.text = result;
                resultBundle = result;

            }
            else {
                Snackbar.make(binding.tvResult, "Preencha apenas um dos campos para converter o valor!", Snackbar.LENGTH_SHORT).show();
            }
        }

        binding.btnNewCalc.setOnClickListener {
            binding.edtMass.setText("");
            binding.edtDistance.setText("");
            binding.edtCapacity.setText("");
            binding.tvResult.text = "0.0";
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("resultBundle", resultBundle);
        super.onSaveInstanceState(outState)
    }

    private fun arrayAdapter(context: android.content.Context, @ArrayRes textArrayResId: Int, spinnerInput: Spinner, spinnerOutput: Spinner) {
        ArrayAdapter.createFromResource(context, textArrayResId, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerInput.adapter = adapter;
                spinnerOutput.adapter = adapter;
            }
    }

    private fun calculate(value: Float, spinnerInput: Spinner, spinnerOutput: Spinner): Double {
        val convertedValue = convert(
            spinnerInput.selectedItemId,
            spinnerOutput.selectedItemId,
            value
        );

        return convertedValue;
    }

    private fun convert(input: Long, output: Long, value: Float): Double {
        val steps: Long = abs(output - input);
        val result: Double;

        if(input == output) {
            return value.toDouble();
        }
        else if(input < output) {
            result = value * 10.0.pow(steps.toDouble())
        }
        else {
            result =  value / 10.0.pow(steps.toDouble());
        }

        return result;
    }
}