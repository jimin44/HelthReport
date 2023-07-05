package com.example.healthreport.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import com.example.healthreport.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogFragmentDatePicker extends DialogFragment {
    NumberPicker numberPickerYear, numberPickerMon;
    Button ok;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    String yearPick, monPick;
    FragmentChart fragmentChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.dialogfragment_date_picker, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        numberPickerYear = myView.findViewById(R.id.picker_year);
        numberPickerMon = myView.findViewById(R.id.picker_month);
        ok = myView.findViewById(R.id.button_date_ok);

        Calendar calendar =  new GregorianCalendar();
        String year = mFormat.format(calendar.getTime()).substring(0,4);
        String day = mFormat.format(calendar.getTime()).substring(8,10);

        fragmentChart = new FragmentChart();

        numberPickerYear.setMinValue(2023);
        numberPickerYear.setMaxValue(Integer.parseInt(year));
        numberPickerMon.setMinValue(1);
        numberPickerMon.setMaxValue(12);

        Bundle bundle = getArguments();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearPick = String.valueOf(numberPickerYear.getValue());
                if(numberPickerMon.getValue()<10)
                    monPick = "0" + numberPickerMon.getValue();
                else
                    monPick = String.valueOf(numberPickerMon.getValue());

                if(bundle.get("chart").equals("1")) ((FragmentChart)getParentFragment()).setTrain(yearPick, monPick, day);
                if(bundle.get("chart").equals("2")) ((FragmentChart)getParentFragment()).setWeight(yearPick, monPick, day);
                if(bundle.get("chart").equals("3")) ((FragmentChart)getParentFragment()).setProtein(yearPick, monPick, day);
                dismiss();
            }
        });

        return myView;
    }
}
