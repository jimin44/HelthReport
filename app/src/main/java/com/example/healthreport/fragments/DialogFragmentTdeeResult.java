package com.example.healthreport.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.healthreport.R;

public class DialogFragmentTdeeResult extends DialogFragment {
    private int tdee;
    private int bmr;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.dialogfragment_tdee_result, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView_tdee_daily = myView.findViewById(R.id.textView_tdee_daily);
        TextView textView_tdee_weekly = myView.findViewById(R.id.textView_tdee_weekly);
        TextView textView_bmr = myView.findViewById(R.id.textView_bmr);

        if (getArguments() != null)
        {
            tdee = (int) getArguments().getDouble("tdee");
            bmr = (int) getArguments().getDouble("bmr");
            textView_tdee_daily.setText(tdee +" kcal");
            textView_tdee_weekly.setText(tdee * 7 +" kcal");
            textView_bmr.setText(bmr +" kcal");
        }

        return myView;
    }
}
