package com.example.healthreport.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.healthreport.R;

public class DialogFragmentTdee extends DialogFragment {

    boolean ismale;
    int activity;
    int age;
    double weight;
    double height;
    double tdee;
    double bmr;
    RadioGroup radioGroup;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.dialogfragment_tdee, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Spinner spinner = (Spinner) myView.findViewById(R.id.spinner_activity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activity_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity = i;
                Log.i("index", String.valueOf(activity));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroup = myView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_button_male:
                        ismale = true;
                        Log.i("", String.valueOf(ismale));
                        break;
                    case R.id.radio_button_female:
                        ismale = false;
                        Log.i("", String.valueOf(ismale));
                        break;
                }
            }
        });

        EditText editText_age = myView.findViewById(R.id.editTextNumberDecimal_age);
        EditText editText_weight = myView.findViewById(R.id.editTextNumberDecimal_weight);
        EditText editText_height = myView.findViewById(R.id.editTextNumberDecimal_height);
        RadioButton radioButton_male = myView.findViewById(R.id.radio_button_male);
        RadioButton radioButton_female = myView.findViewById(R.id.radio_button_female);
        Button button_calcul = myView.findViewById(R.id.button_calcul);
        button_calcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText_age.length() == 0 || editText_weight.length() == 0||
                        editText_height.length() == 0 || (!radioButton_male.isChecked() && !radioButton_female.isChecked())){
                    Toast.makeText(getActivity(), "모든 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                age = Integer.parseInt(editText_age.getText().toString());
                weight = Double.parseDouble(editText_weight.getText().toString());
                height = Double.parseDouble(editText_height.getText().toString());



                if(ismale) bmr = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
                if(!ismale) bmr = 655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age);

                if(activity == 0) tdee = bmr * 1.2;
                if(activity == 1) tdee = bmr * 1.375;
                if(activity == 2) tdee = bmr * 1.55;
                if(activity == 3) tdee = bmr * 1.725;
                if(activity == 4) tdee = bmr * 1.9;

                bmr = (int) Math.round(bmr);
                tdee = (int) Math.round(tdee);

                Log.i("bmr", String.valueOf(bmr));
                Log.i("tdee", String.valueOf(tdee));

                Bundle bundle = new Bundle();
                bundle.putDouble("bmr", bmr);
                bundle.putDouble("tdee", tdee);
                DialogFragmentTdeeResult dialogFragmentTdeeResult = new DialogFragmentTdeeResult();
                dialogFragmentTdeeResult.setArguments(bundle);
                dialogFragmentTdeeResult.show(getChildFragmentManager(), null);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTdeeResult).commit();
            }
        });

        return myView;
    }


}
