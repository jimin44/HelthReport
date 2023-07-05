package com.example.healthreport.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.healthreport.R;

public class DialogFragmentBmi extends DialogFragment {
    RadioGroup radioGroup;
    RadioButton male, female;
    EditText editTextWeight, editTextHeight;
    boolean ismale;
    double weight, height;
    Button culcul;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.dialogfragment_bmi, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        editTextWeight = myView.findViewById(R.id.editTextNumberDecimal_bmi_weight);
        editTextHeight = myView.findViewById(R.id.editTextNumberDecimal_bmi_height);
        culcul = myView.findViewById(R.id.button_bmi_calcul);
        male = myView.findViewById(R.id.radio_button_male);
        female = myView.findViewById(R.id.radio_button_female);

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

        culcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!male.isChecked() && !female.isChecked() || editTextWeight.length() == 0 || editTextHeight.length() == 0){
                    Toast.makeText(getActivity(), "모든 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    weight = Integer.parseInt(editTextWeight.getText().toString());
                    height = Double.parseDouble(editTextHeight.getText().toString()) / 100;
                    double bmi = Math.round(weight/(height*height)*10)/10.0;
                    Bundle bundle = new Bundle();
                    bundle.putString("bmi", String.valueOf(bmi));
                    DialogFragmentBmiResult dialogFragmentBmiResult = new DialogFragmentBmiResult();
                    dialogFragmentBmiResult.setArguments(bundle);
                    dialogFragmentBmiResult.show(getChildFragmentManager(), null);
                }
            }
        });

        return myView;
    }
}
