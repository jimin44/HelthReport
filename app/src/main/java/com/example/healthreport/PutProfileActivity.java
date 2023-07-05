package com.example.healthreport;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PutProfileActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    long backpressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        EditText name = findViewById(R.id.editTextText_name);
        EditText age = findViewById(R.id.editTextNumberDecimal_inputAge);
        EditText height = findViewById(R.id.editTextNumberDecimal_inputHeight);
        EditText weight = findViewById(R.id.editTextNumberDecimal_inputWeight);
        Button button = findViewById(R.id.button_input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.length() == 0 || age.length() == 0||
                        height.length() == 0 || weight.length() == 0){
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    Calendar calendar =  new GregorianCalendar();
                    String year = mFormat.format(calendar.getTime()).substring(0,4);
                    String mon = mFormat.format(calendar.getTime()).substring(5,7);
                    String day = mFormat.format(calendar.getTime()).substring(8,10);
                    Intent intent = getIntent();
                    String id = intent.getStringExtra("id");
                    databaseReference.child(id).child("name").setValue(name.getText().toString());
                    databaseReference.child(id).child("age").setValue(age.getText().toString());
                    databaseReference.child(id).child("height").setValue(height.getText().toString());
                    //databaseReference.child(id).child("weight").setValue(weight.getText().toString());
                    databaseReference.child(id).child("weight").child(year).child(mon).child(day).setValue(weight.getText().toString());
                    databaseReference.child(id).child("weight").child("now").setValue(weight.getText().toString());

                    intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();

                }
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