package com.example.healthreport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FragmentUser extends Fragment {
    DatabaseReference databaseReference;
    Button update, withdraw;
    TextView textView;
    DialogFragmentUserUptate dialogFragmentUserUptate;
    DialogFragmentWithdraw dialogFragmentWithdraw;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_user, container, false);

        update = myView.findViewById(R.id.button_user_update);
        withdraw = myView.findViewById(R.id.button_user_withdraw);
        textView = myView.findViewById(R.id.textView_profile2);

        databaseReference = ((MainActivity)getActivity()).databaseReference;


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText("이름: "+snapshot.child("name").getValue()+"\n\n"
                +"나이: "+snapshot.child("age").getValue()+"\n\n"
                +"키: "+snapshot.child("height").getValue()+"cm"+"\n\n"
                +"몸무게: "+snapshot.child("weight").child("now").getValue()+"kg"+"\n\n"
                +"회원번호: "+snapshot.child("userNum").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentUserUptate = new DialogFragmentUserUptate();
                dialogFragmentUserUptate.show(getChildFragmentManager(), null);
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentWithdraw = new DialogFragmentWithdraw();
                dialogFragmentWithdraw.show(getChildFragmentManager(), null);
            }
        });

        return myView;
    }
}
