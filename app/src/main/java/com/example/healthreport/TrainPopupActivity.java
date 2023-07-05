package com.example.healthreport;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TrainPopupActivity extends Activity {

    Button cancel;
    Button ok;
    EditText squat, bench, dead, crunch, seatedCable, cablePushDown, lat, overHead, bicepsCurl;
    int squatWeight, benchWeight, deadWeight, crunchWeight, seatedCableWeight, cablePushDownWeight, latWeight, overHeadWight, bicepsCurlWeight;
    String program;
    DatabaseReference databaseReference;
    ArrayList<String> healthItem;
    ArrayList<Integer> itemWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        Intent intent = getIntent();
        program = intent.getStringExtra("program");

        setContentView(R.layout.activity_train_popup);


        databaseReference = FirebaseDatabase.getInstance().getReference().child(MainActivity.id).child("training");

        cancel = findViewById(R.id.button_cancel);
        ok = findViewById(R.id.button_ok);
        squat = findViewById(R.id.editTextNumber_squat);
        bench = findViewById(R.id.editTextNumber_bench);
        dead = findViewById(R.id.editTextNumber_deadlift);
        crunch = findViewById(R.id.editTextNumber_crunch);
        seatedCable = findViewById(R.id.editTextNumber_seated_cable_row);
        cablePushDown = findViewById(R.id.editTextNumber_cable_pushdown);
        lat = findViewById(R.id.editTextNumber_lat_pulldown);
        overHead = findViewById(R.id.editTextNumber_overhead_press);
        bicepsCurl = findViewById(R.id.editTextNumber_biceps_curl);

        healthItem = new ArrayList<>(Arrays.asList("squat", "bench", "dead", "crunch", "seatedCable", "cablePushDown","lat", "overHead", "bicepsCurl"));


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("id", MainActivity.id);
                intent.putExtra("programNum", program);
                int count = 0, countZero = 0;

                if(squat.getText().toString().equals("")) squatWeight = 0;
                else if (squat.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    squatWeight = Integer.parseInt(squat.getText().toString());
                    count++;
                }

                if(bench.getText().toString().equals("")) benchWeight = 0;
                else if (bench.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    benchWeight = Integer.parseInt(bench.getText().toString());
                    count++;
                }

                if(dead.getText().toString().equals("")) deadWeight = 0;
                else if (dead.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    deadWeight = Integer.parseInt(dead.getText().toString());
                    count++;
                }

                if(crunch.getText().toString().equals("")) crunchWeight = 0;
                else if (crunch.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    crunchWeight = Integer.parseInt(crunch.getText().toString());
                    count++;
                }

                if(seatedCable.getText().toString().equals("")) seatedCableWeight = 0;
                else if (seatedCable.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    seatedCableWeight = Integer.parseInt(seatedCable.getText().toString());
                    count++;
                }

                if(cablePushDown.getText().toString().equals("")) cablePushDownWeight = 0;
                else if (cablePushDown.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    cablePushDownWeight = Integer.parseInt(cablePushDown.getText().toString());
                    count++;
                }

                if(lat.getText().toString().equals("")) latWeight = 0;
                else if (lat.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    latWeight = Integer.parseInt(lat.getText().toString());
                    count++;
                }

                if(overHead.getText().toString().equals("")) overHeadWight = 0;
                else if (overHead.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    overHeadWight = Integer.parseInt(overHead.getText().toString());
                    count++;
                }

                if(bicepsCurl.getText().toString().equals("")) bicepsCurlWeight = 0;
                else if (bicepsCurl.getText().toString().equals("0")) {
                    countZero++;
                    count++;
                }
                else {
                    bicepsCurlWeight = Integer.parseInt(bicepsCurl.getText().toString());
                    count++;
                }

                itemWeight = new ArrayList<>(Arrays.asList(squatWeight, benchWeight, deadWeight, crunchWeight, seatedCableWeight, cablePushDownWeight, latWeight, overHeadWight, bicepsCurlWeight));

                //1개도 입력 안했을 시
                if(count==0) Toast.makeText(getApplicationContext(), "항목을 1개 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if (countZero!=0) {
                    Toast.makeText(getApplicationContext(), "0은 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else{
                    databaseReference.child("program").setValue(program);
                    if(program.equals("1")) setData("hypertrophy", 0.6,12, 3);
                    if(program.equals("2")) setData("endurance", 0.5, 5, 5);
                    if(program.equals("3")) setData("strength", 1.0, 5, 5);

                    startActivity(intent);
                    MainActivity.mainActivity.finish();
                    finish();
                }

            }
        });
    }

    public void setData(String program, double w, int repeat, int set){
        for(int i = 0; i<healthItem.size(); i++){
            databaseReference.child(program).child(healthItem.get(i)).child("weight").setValue(itemWeight.get(i) * w);
            databaseReference.child(program).child(healthItem.get(i)).child("repeat").setValue(repeat);
            databaseReference.child(program).child(healthItem.get(i)).child("set").setValue(set);
            databaseReference.child(program).child(healthItem.get(i)).child("success").setValue(0);
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_OUTSIDE ) {
            return false;
        }
        return true;
    }

    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}