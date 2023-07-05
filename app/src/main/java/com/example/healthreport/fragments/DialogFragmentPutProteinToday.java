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

public class DialogFragmentPutProteinToday extends DialogFragment {
    EditText putProtein;
    Button ok, high, middle, low;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    String year, mon, day;
    int weight;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.dialogfragment_put_protein_today, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //putProtein = myView.findViewById(R.id.editText_put_protein);
        //ok = myView.findViewById(R.id.button_ok2);
        high = myView.findViewById(R.id.button_highProtein);
        middle = myView.findViewById(R.id.button_middleProtein);
        low = myView.findViewById(R.id.button_lowProtein);

        DatabaseReference databaseReference = ((MainActivity)getActivity()).databaseReference;

        databaseReference.child("weight").child("now").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                weight = Integer.parseInt(dataSnapshot.getValue().toString());
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
                databaseReference.child("food").child(year).child(mon).child(day).child("ateProtein").setValue(weight*1.2);
                databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
                dismiss();
            }
        });

        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
                databaseReference.child("food").child(year).child(mon).child(day).child("ateProtein").setValue(weight);
                databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
                dismiss();
            }
        });

        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
                databaseReference.child("food").child(year).child(mon).child(day).child("ateProtein").setValue(weight*0.8);
                databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
                dismiss();
            }
        });

//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!putProtein.getText().toString().equals("")){
//                    Calendar calendar =  new GregorianCalendar();
//                    String year = mFormat.format(calendar.getTime()).substring(0,4);
//                    String mon = mFormat.format(calendar.getTime()).substring(5,7);
//                    String day = mFormat.format(calendar.getTime()).substring(8,10);
//
//                    int protein = Integer.parseInt(putProtein.getText().toString());
//                    databaseReference.child("food").child(year).child(mon).child(day).child("ateProtein").setValue(protein);
//                    databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
//                    dismiss();
//                }
//            }
//        });

        return myView;
    }

    public void setTime(){
        Calendar calendar =  new GregorianCalendar();
        year = mFormat.format(calendar.getTime()).substring(0,4);
        mon = mFormat.format(calendar.getTime()).substring(5,7);
        day = mFormat.format(calendar.getTime()).substring(8,10);
    }
}
