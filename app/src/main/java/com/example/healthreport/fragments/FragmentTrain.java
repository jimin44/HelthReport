package com.example.healthreport.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.healthreport.MainActivity;
import com.example.healthreport.TrainPopupActivity;
import com.example.healthreport.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentTrain extends Fragment {
    Button hypertrophy;
    Button endurance;
    Button strength;
    DatabaseReference databaseReference;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    FragmentTrainFinish fragmentTrainFinish;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_train, container, false);

        fragmentTrainFinish = new FragmentTrainFinish();

        hypertrophy = myView.findViewById(R.id.button_hypertrophy);
        endurance = myView.findViewById(R.id.button_endurance);
        strength = myView.findViewById(R.id.button_strength);

        databaseReference = ((MainActivity) getActivity()).databaseReference.child("training");

        Calendar calendar =  new GregorianCalendar();
        String year = mFormat.format(calendar.getTime()).substring(0,4);
        String mon = mFormat.format(calendar.getTime()).substring(5,7);
        String day = mFormat.format(calendar.getTime()).substring(8,10);

        calendar.add(Calendar.DATE, -1);
        String yesterYear = mFormat.format(calendar.getTime()).substring(0,4);
        String yesterMon = mFormat.format(calendar.getTime()).substring(5,7);
        String yesterDay = mFormat.format(calendar.getTime()).substring(8,10);

        Intent intent = new Intent(getActivity(), TrainPopupActivity.class);

        hypertrophy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(yesterYear).child(yesterMon).child(yesterDay).child("hypertrophy").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainFinish).commit();
                            databaseReference.child("program").setValue("1");
                        }
                        else{
                            databaseReference.child(year).child(mon).child(day).child("hypertrophy").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainFinish).commit();
                                        databaseReference.child("program").setValue("1");
                                    }
                                    else{
                                        databaseReference.child("hypertrophy").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    new AlertDialog.Builder(getActivity())
                                                            .setMessage("현재 진행중인 운동이 존재합니다. 계속 하시겠습니까?")
                                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    FragmentTrainHypertrophy fragmentTrainHypertrophy = new FragmentTrainHypertrophy();
                                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainHypertrophy).commit();
                                                                }
                                                            })
                                                            .setNegativeButton("1rm 재입력", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    intent.putExtra("program", "1");
                                                                    startActivity(intent);
                                                                }
                                                            }).show();
                                                }
                                                else{
                                                    intent.putExtra("program", "1");
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }
                });

            }
        });
        endurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(year).child(mon).child(day).child("endurance").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainFinish).commit();
                            databaseReference.child("program").setValue("2");
                        }
                        else{
                            databaseReference.child("endurance").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){

                                        new AlertDialog.Builder(getActivity())
                                                .setMessage("현재 진행중인 운동이 존재합니다. 계속 하시겠습니까?")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FragmentTrainEndurance fragmentTrainEndurance = new FragmentTrainEndurance();
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainEndurance).commit();
                                                    }
                                                })
                                                .setNegativeButton("1rm 재입력", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        intent.putExtra("program", "2");
                                                        startActivity(intent);
                                                    }
                                                }).show();
                                    }
                                    else{
                                        intent.putExtra("program", "2");
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(year).child(mon).child(day).child("strength").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainFinish).commit();
                            databaseReference.child("program").setValue("3");
                        }
                        else{
                            databaseReference.child("strength").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        new AlertDialog.Builder(getActivity())
                                                .setMessage("현재 진행중인 운동이 존재합니다. 계속 하시겠습니까?")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        FragmentTrainStrength fragmentTrainStrength = new FragmentTrainStrength();
                                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainStrength).commit();
                                                    }
                                                })
                                                .setNegativeButton("1rm 재입력", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        intent.putExtra("program", "3");
                                                        startActivity(intent);
                                                    }
                                                }).show();
                                    }
                                    else{
                                        intent.putExtra("program", "3");
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        return myView;
    }
}
