package com.example.courseworkcomp1786;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private TextView dobControl;
    private EditText nameInput;
    private EditText locationInput;
    private EditText lengthInput;
    private EditText descriptionInput;
    private RadioGroup radioGroup;
    private Spinner sp;
    private Button submitBtn;
    private String parking;
    private final String[] difficultyLevel = {"High", "Medium", "Low"};
    private DatabaseHelper dbHelper; // Declare dbHelper as a member variable
    private String name;
    private String location;
    private String length;
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LocalDate day1 = LocalDate.now();
            int year = day1.getYear();
            int month = day1.getMonthValue();
            int day = day1.getDayOfMonth();
            return new DatePickerDialog(getActivity(), this, year, month - 1, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            LocalDate dob = LocalDate.of(year, month + 1, day);
            ((MainActivity) getActivity()).updateDOB(dob);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dobControl = findViewById(R.id.dob_control);
        nameInput = findViewById(R.id.namehike_input);
        locationInput = findViewById(R.id.location_input);
        lengthInput = findViewById(R.id.length_input);
        descriptionInput = findViewById(R.id.description_input);
        radioGroup = findViewById(R.id.radioGroup);
        sp = findViewById(R.id.spinner);

        // Create an adapter
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevel);
        // Connect adapter to spinner
        sp.setAdapter(dataAdapter);
        submitBtn = findViewById(R.id.submit_btn);
        dbHelper = new DatabaseHelper(this); // Initialize dbHelper

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtn();
            }
        });

        dobControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void submitBtn() {
        name = nameInput.getText().toString();
        location = locationInput.getText().toString();
        length = lengthInput.getText().toString();
        String description = descriptionInput.getText().toString();
        parking = null;

        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        String spinnerSelection = sp.getSelectedItem().toString();
        if (radioButtonId != -1 && !spinnerSelection.equals("Select Difficulty")) {
            RadioButton radioButton = findViewById(radioButtonId);
            parking = radioButton.getText().toString();

            // Show the confirmation dialog here
            displayAlert(name, location, dobControl.getText().toString(), parking, length, spinnerSelection);
        } else {
            Toast.makeText(this, "Please select a difficulty level and choose a spinner value.", Toast.LENGTH_LONG).show();
        }
    }

    public void updateDOB(LocalDate dob) {
        dobControl.setText(dob.toString());
    }

    public void displayAlert(String name, String location, String dob, String parking, String length, String spinnerSelection) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage(
                        "New hike will be added" + "\n" +
                                "Name: " + name + "\n" +
                                "Location: " + location + "\n" +
                                "Date: " + dob + "\n" +
                                "Parking available: " + parking + "\n" +
                                "Length of the hike: " + length + "\n" +
                                "Difficult level: " + spinnerSelection + "\n\n" +
                                "Are you sure?"
                )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long hikeId = dbHelper.insertDetails(name, location, dob, parking, length, spinnerSelection);
                        Toast.makeText(MainActivity.this, "Hike has been created with id: " + hikeId, Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }
}
