package com.example.healthreport.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogFragmentUserUptate extends DialogFragment {
    EditText name, age, height, weight;
    Button ok;
    DatabaseReference databaseReference;
    Calendar calendar;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.dialogfragment_user_update, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        name = myView.findViewById(R.id.editText_user_editName);
        age = myView.findViewById(R.id.editText_user_editAge);
        height = myView.findViewById(R.id.editText_user_editHeight);
        weight = myView.findViewById(R.id.editText_user_editWeight);

        ok = myView.findViewById(R.id.button_user_update_ok);

        databaseReference = ((MainActivity)getActivity()).databaseReference;

        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                age.setText(dataSnapshot.child("age").getValue().toString());
                height.setText(dataSnapshot.child("height").getValue().toString());
                weight.setText(dataSnapshot.child("weight").child("now").getValue().toString());
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar =  new GregorianCalendar();
                String year = mFormat.format(calendar.getTime()).substring(0,4);
                String mon = mFormat.format(calendar.getTime()).substring(5,7);
                String day = mFormat.format(calendar.getTime()).substring(8,10);
                databaseReference.child("name").setValue(name.getText().toString());
                databaseReference.child("age").setValue(age.getText().toString());
                databaseReference.child("height").setValue(height.getText().toString());
                databaseReference.child("weight").child("now").setValue(weight.getText().toString());
                databaseReference.child("weight").child(year).child(mon).child(day).setValue(weight.getText().toString());
                dismiss();
            }
        });

        return myView;
    }
}
