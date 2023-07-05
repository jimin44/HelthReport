package com.example.healthreport;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthreport.fragments.FragmentChart;
import com.example.healthreport.fragments.FragmentCreatine;
import com.example.healthreport.fragments.FragmentHome;
import com.example.healthreport.fragments.DialogFragmentTdee;
import com.example.healthreport.fragments.FragmentTrain;
import com.example.healthreport.fragments.FragmentTrainEndurance;
import com.example.healthreport.fragments.FragmentTrainHypertrophy;
import com.example.healthreport.fragments.FragmentTrainStrength;
import com.example.healthreport.fragments.FragmentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FragmentHome fragmentHome;
    FragmentTrain fragmentTrain;
    FragmentChart fragmentChart;
    DialogFragmentTdee dialogFragmentTdee;
    FragmentUser fragmentUser;
    FragmentCreatine fragmentCreatine;
    FragmentTrainEndurance fragmentTrainEndurance;
    FragmentTrainHypertrophy fragmentTrainHypertrophy;
    FragmentTrainStrength fragmentTrainStrength;
    public DatabaseReference databaseReference;
    static public String id;
    static public Activity mainActivity;
    public BottomNavigationView bottomNavigationView;
    String program, programNum;
    long backpressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("홈");

        mainActivity = MainActivity.this;

        fragmentHome = new FragmentHome();
        fragmentTrain = new FragmentTrain();
        fragmentChart = new FragmentChart();
        dialogFragmentTdee = new DialogFragmentTdee();
        fragmentCreatine = new FragmentCreatine();
        fragmentUser = new FragmentUser();
        fragmentTrainEndurance = new FragmentTrainEndurance();
        fragmentTrainHypertrophy = new FragmentTrainHypertrophy();
        fragmentTrainStrength = new FragmentTrainStrength();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.tab_home);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        programNum = intent.getStringExtra("programNum");

        databaseReference = FirebaseDatabase.getInstance().getReference().child(id);
        databaseReference.child("training").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null) program = "null";
                else program = snapshot.child("program").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TrainPopup에서 확인버튼 눌렀을 시 나타낼 화면
        if(Objects.equals(programNum, "1")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainHypertrophy).commit();
            bottomNavigationView.setSelectedItemId(R.id.tab_train);
        }
        else if(Objects.equals(programNum, "2")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainEndurance).commit();
            bottomNavigationView.setSelectedItemId(R.id.tab_train);
        }
        else if(Objects.equals(programNum, "3")){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainStrength).commit();
            bottomNavigationView.setSelectedItemId(R.id.tab_train);
        }
        //팝업에서 넘어온게 아니라면(그냥 앱을 실행한거라면) 홈화면 보여주기
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();
                        getSupportActionBar().setTitle("홈");
                        return true;
                    case R.id.tab_train:
                        //if(program.equals("0")||program.equals("null"))
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrain).commit();
//                        else if(program.equals("1"))
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainHypertrophy).commit();
//                        else if(program.equals("2"))
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainEndurance).commit();
//                        else if(program.equals("3"))
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTrainStrength).commit();

                        getSupportActionBar().setTitle("운동");
                        return true;
                    case R.id.tab_chart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentChart).commit();
                        getSupportActionBar().setTitle("통계");
                        return true;
                    case R.id.tab_food:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentCreatine).commit();
                        getSupportActionBar().setTitle("크리틴");
                        return true;
                    case R.id.tab_user:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentUser).commit();
                        getSupportActionBar().setTitle("MY");
                        return true;
                }
                return false;
            }
        });

    }

    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }
}