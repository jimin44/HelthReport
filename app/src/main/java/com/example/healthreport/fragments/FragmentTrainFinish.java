package com.example.healthreport.fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentTrainFinish extends Fragment {
    Button change;
    TextView textView;
    DatabaseReference databaseReference;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_train_finish, container, false);

        databaseReference = ((MainActivity)getActivity()).databaseReference.child("training");

        textView = myView.findViewById(R.id.textView50);

        databaseReference.child("program").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("1")) textView.setText("근비대 운동 완료");
                else if(dataSnapshot.getValue().toString().equals("2")) textView.setText("근지구력 운동 완료");
                else if(dataSnapshot.getValue().toString().equals("3")) textView.setText("근력 운동 완료");
            }
        });

        change = myView.findViewById(R.id.button_finish_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTrain fragmentTrain = new FragmentTrain();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrain).commit();
            }
        });

        return myView;
    }
}
