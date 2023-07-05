package com.example.healthreport.fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FragmentTrainStrength extends Fragment {

    TextView squat, bench, dead, crunch, seatedCable, cablePushDown, lat, overHead, bicepsCurl, title;
    Button squatO, benchO, deadO, crunchO, seatedCableO, cablePushDownO, latO, overHeadO, bicepsCurlO;
    Button squatX, benchX, deadX, crunchX, seatedCableX, cablePushDownX, latX, overHeadX, bicepsCurlX;
    Button change, ok;
    double weight;
    int repeat, set, success;
    int itemCount;
    DatabaseReference databaseReference;
    FragmentTrainFinish fragmentTrainFinish;
    ArrayList<String> healthItem;
    ArrayList<TextView> textViews;
    ArrayList<Button> buttonsO;
    ArrayList<Button> buttonsX;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_train_strength, container, false);

        fragmentTrainFinish = new FragmentTrainFinish();

        String year = getTime().substring(0,4);
        String mon = getTime().substring(5,7);
        String day = getTime().substring(8,10);

        squat = myView.findViewById(R.id.textView_strength_squat);
        bench = myView.findViewById(R.id.textView_strength_bench);
        dead = myView.findViewById(R.id.textView_strength_dead);
        crunch = myView.findViewById(R.id.textView_strength_crunch);
        seatedCable = myView.findViewById(R.id.textView_strength_seated_cable);
        cablePushDown = myView.findViewById(R.id.textView_strength_cable_pushdown);
        lat = myView.findViewById(R.id.textView_strength_lat);
        overHead = myView.findViewById(R.id.textView_strength_overhead_press);
        bicepsCurl = myView.findViewById(R.id.textView_strength_biceps_curl);
        title = myView.findViewById(R.id.textView10);

        squatO = myView.findViewById(R.id.button_strength_squat_o);
        benchO = myView.findViewById(R.id.button_strength_bench_o);
        deadO = myView.findViewById(R.id.button_strength_dead_o);
        crunchO = myView.findViewById(R.id.button_strength_crunch_o);
        seatedCableO = myView.findViewById(R.id.button_strength_seated_cable_o);
        cablePushDownO = myView.findViewById(R.id.button_strength_cable_pushdown_o);
        latO = myView.findViewById(R.id.button_strength_lat_o);
        overHeadO = myView.findViewById(R.id.button_strength_overdead_press_o);
        bicepsCurlO = myView.findViewById(R.id.button_strength_biceps_curl_o);

        squatX = myView.findViewById(R.id.button_strength_squat_x);
        benchX = myView.findViewById(R.id.button_strength_bench_x);
        deadX = myView.findViewById(R.id.button_strength_dead_x);
        crunchX = myView.findViewById(R.id.button_strength_crunch_x);
        seatedCableX = myView.findViewById(R.id.button_strength_seated_cable_x);
        cablePushDownX = myView.findViewById(R.id.button_strength_cable_pushdown_x);
        latX = myView.findViewById(R.id.button_strength_lat_x);
        overHeadX = myView.findViewById(R.id.button_strength_overhead_press_x);
        bicepsCurlX = myView.findViewById(R.id.button_strength_biceps_curl_x);

        change = myView.findViewById(R.id.button_strength_change);
        ok = myView.findViewById(R.id.button_strength_ok);

        healthItem = new ArrayList<>(Arrays.asList("squat", "bench", "dead", "crunch", "seatedCable", "cablePushDown","lat", "overHead", "bicepsCurl"));
        textViews = new ArrayList<>(Arrays.asList(squat, bench, dead, crunch, seatedCable, cablePushDown, lat, overHead, bicepsCurl));
        buttonsO = new ArrayList<>(Arrays.asList(squatO, benchO, deadO, crunchO, seatedCableO, cablePushDownO, latO, overHeadO, bicepsCurlO));
        buttonsX = new ArrayList<>(Arrays.asList(squatX, benchX, deadX, crunchX, seatedCableX, cablePushDownX, latX, overHeadX, bicepsCurlX));

        title.setText("오늘의 근력 운동");

        databaseReference = ((MainActivity)getActivity()).databaseReference.child("training");


        //오늘의 운동을 완료한 기록이 있다면, 완료 화면 띄움
        databaseReference.child(year).child(mon).child(day).child("strength").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainFinish).commit();

                }
            }
        });

        databaseReference.child("strength").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemCount = 0;
                for(int i = 0; i<healthItem.size(); i++){
                    weight = Double.parseDouble(snapshot.child(healthItem.get(i)).child("weight").getValue().toString());
                    success = Integer.parseInt(snapshot.child(healthItem.get(i)).child("success").getValue().toString());
                    repeat = Integer.parseInt(snapshot.child(healthItem.get(i)).child("repeat").getValue().toString());
                    set = Integer.parseInt(snapshot.child(healthItem.get(i)).child("set").getValue().toString());

                    if(weight==0) {
                        buttonsO.get(i).setVisibility(View.INVISIBLE);
                        buttonsX.get(i).setVisibility(View.INVISIBLE);
                    }
                    else {
                        textViews.get(i).setText(weight+2.5*success +"kg "+repeat+"회 "+set+"세트");
                        itemCount++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        squatO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                squatO.setSelected(true);
                squatX.setSelected(false);
            }
        });
        squatX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                squatO.setSelected(false);
                squatX.setSelected(true);
            }
        });
        benchO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                benchO.setSelected(true);
                benchX.setSelected(false);
            }
        });
        benchX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                benchO.setSelected(false);
                benchX.setSelected(true);
            }
        });
        deadO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deadO.setSelected(true);
                deadX.setSelected(false);
            }
        });
        deadX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deadO.setSelected(false);
                deadX.setSelected(true);
            }
        });
        crunchO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crunchO.setSelected(true);
                crunchX.setSelected(false);
            }
        });
        crunchX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crunchO.setSelected(false);
                crunchX.setSelected(true);
            }
        });
        seatedCableO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seatedCableO.setSelected(true);
                seatedCableX.setSelected(false);
            }
        });
        seatedCableX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seatedCableO.setSelected(false);
                seatedCableX.setSelected(true);
            }
        });
        cablePushDownO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cablePushDownO.setSelected(true);
                cablePushDownX.setSelected(false);
            }
        });
        cablePushDownX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cablePushDownO.setSelected(false);
                cablePushDownX.setSelected(true);
            }
        });
        latO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latO.setSelected(true);
                latX.setSelected(false);
            }
        });
        latX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latO.setSelected(false);
                latX.setSelected(true);
            }
        });
        overHeadO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overHeadO.setSelected(true);
                overHeadX.setSelected(false);
            }
        });
        overHeadX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overHeadO.setSelected(false);
                overHeadX.setSelected(true);
            }
        });
        bicepsCurlO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bicepsCurlO.setSelected(true);
                bicepsCurlX.setSelected(false);
            }
        });
        bicepsCurlX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bicepsCurlO.setSelected(false);
                bicepsCurlX.setSelected(true);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> successItem = new ArrayList<>();
                ArrayList<String> failItem = new ArrayList<>();
                int cntO = 0;
                int cntX = 0;
                for(int i = 0; i<buttonsO.size(); i++){
                    if(buttonsO.get(i).isSelected()) {
                        successItem.add(healthItem.get(i));
                        cntO++;
                    }
                    if(buttonsX.get(i).isSelected()) {
                        failItem.add(healthItem.get(i));
                        cntX++;
                    }
                }

                //버튼을 모두 선택했을 시
                if(cntO+cntX==itemCount){
                    databaseReference.child("strength").get()
                            .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    for(int i = 0; i<successItem.size(); i++){
                                        int success = Integer.parseInt(dataSnapshot.child(successItem.get(i)).child("success").getValue().toString());
                                        double weight = Double.parseDouble(dataSnapshot.child(successItem.get(i)).child("weight").getValue().toString());
                                        int repeat = Integer.parseInt(dataSnapshot.child(successItem.get(i)).child("repeat").getValue().toString());
                                        int set = Integer.parseInt(dataSnapshot.child(successItem.get(i)).child("set").getValue().toString());
                                        String year = getTime().substring(0,4);
                                        String mon = getTime().substring(5,7);
                                        String day = getTime().substring(8,10);
                                        databaseReference.child("strength").child(successItem.get(i)).child("success").setValue(success + 1);
                                        databaseReference.child(year).child(mon).child(day).child("strength").child(successItem.get(i)).child("weight").setValue(weight+2.5*success);
                                        databaseReference.child(year).child(mon).child(day).child("strength").child(successItem.get(i)).child("repeat").setValue(repeat);
                                        databaseReference.child(year).child(mon).child(day).child("strength").child(successItem.get(i)).child("set").setValue(set);
                                    }
                                    for(int i = 0; i<failItem.size(); i++){
//                                        int success = Double.parseDouble(dataSnapshot.child(failItem.get(i)).child("success").getValue().toString());
//                                        int weight = Double.parseDouble(dataSnapshot.child(failItem.get(i)).child("weight").getValue().toString());
//                                        int repeat = Double.parseDouble(dataSnapshot.child(failItem.get(i)).child("repeat").getValue().toString());
//                                        int set = Double.parseDouble(dataSnapshot.child(failItem.get(i)).child("set").getValue().toString());
                                        String year = getTime().substring(0,4);
                                        String mon = getTime().substring(5,7);
                                        String day = getTime().substring(8,10);

                                        databaseReference.child(year).child(mon).child(day).child("strength").child(failItem.get(i)).child("weight").setValue(0);
                                        databaseReference.child(year).child(mon).child(day).child("strength").child(failItem.get(i)).child("repeat").setValue(0);
                                        databaseReference.child(year).child(mon).child(day).child("strength").child(failItem.get(i)).child("set").setValue(0);
                                    }

                                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainFinish).commit();
                                    FragmentChart fragmentChart = new FragmentChart();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentChart).commit();
                                    ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.tab_chart);
                                    Toast.makeText(getActivity(), "크리틴 탭을 눌러 크리틴 섭취 안내를 받으세요.", Toast.LENGTH_LONG).show();
                                }
                            });

                }
                else{
                    Toast.makeText(getActivity(), "모든 종목의 성공 여부를 눌러주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTrain fragmentTrain = new FragmentTrain();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrain).commit();
            }
        });

        return myView;
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

}
