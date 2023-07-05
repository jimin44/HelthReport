package com.example.healthreport.fragments;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentCreatine extends Fragment {
    TextView title, textView, textview2, textViewDidTrainedToday;
    Button buttonO, buttonX, buttonOK;
    BarChart chartFood;
    DialogFragmentPutProteinToday dialogFragmentPutProteinToday;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar;
    ArrayList<BarEntry> entryChart;

    //단백질 권장 섭취량 저장(몸무게 대비)
   ArrayList<BarEntry> entryChart2;
    ArrayList<String> xAxisDay;
    DatabaseReference databaseReference;
    String year, mon, day;
    double v;
    int weight;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_creatine, container, false);

        title = myView.findViewById(R.id.textView10);
        textView = myView.findViewById(R.id.textView51);
        textview2 = myView.findViewById(R.id.textView52);
        textViewDidTrainedToday = myView.findViewById(R.id.textView67);
        chartFood = myView.findViewById(R.id.chart_food_protein);

        buttonO = myView.findViewById(R.id.button_food_o);
        buttonX = myView.findViewById(R.id.button_food_x);
        buttonOK = myView.findViewById(R.id.button_food_ok);

        textview2.setVisibility(View.INVISIBLE);
        buttonO.setVisibility(View.INVISIBLE);
        buttonX.setVisibility(View.INVISIBLE);
        buttonOK.setVisibility(View.INVISIBLE);


        databaseReference = ((MainActivity)getActivity()).databaseReference;

        calendar =  new GregorianCalendar();
        year = mFormat.format(calendar.getTime()).substring(0,4);
        mon = mFormat.format(calendar.getTime()).substring(5,7);
        day = mFormat.format(calendar.getTime()).substring(8,10);

        xAxisDay = new ArrayList<>();
        for(int i = 0; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
            xAxisDay.add(i +"일");
        }

        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                //몸무게 정보 가져오기
                if(dataSnapshot.child("weight").child(year).child(mon).child(day).exists())
                    weight = Integer.parseInt(dataSnapshot.child("weight").child(year).child(mon).child(day).getValue().toString());
                else
                    weight = Integer.parseInt(dataSnapshot.child("weight").child("now").getValue().toString());

                //운동 여부에 따라 가중치
                if(dataSnapshot.child("training").child(year).child(mon).child(day).exists()) {
                    v = 1.2;
                    textViewDidTrainedToday.setText("오늘도 열심히 운동을 하셨네요!");
                    //textViewDidTrainedToday.setTextColor(Color.BLUE);
                }
                else {
                    textViewDidTrainedToday.setText("ㅠㅠ 오늘은 운동을 안하셨네요");
                    //textViewDidTrainedToday.setTextColor(Color.GREEN);
                    v = 1;
                }

                title.setText(name+"님의 오늘의 단백질 섭취 권고량: "+weight*v+"g\n(체중, 운동 여부에 따라 달라짐)");

                DataSnapshot today = dataSnapshot.child("food").child(year).child(mon).child(day);

                if(!(today.exists())){
                    dialogFragmentPutProteinToday = new DialogFragmentPutProteinToday();
                    //dialogFragmentPutProteinToday.setCancelable(false);
                    dialogFragmentPutProteinToday.show(getChildFragmentManager(), null);
                }

                if(today.child("product").child("isEat").exists()){
                    if(Integer.parseInt(today.child("product").child("isEat").getValue().toString())==0
                            && Integer.parseInt(today.child("product").child("ea").getValue().toString()) > 0)
                        Toast.makeText(getActivity(), "단백질이 부족합니다. 제품을 섭취해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entryChart = new ArrayList<>();
                entryChart2 = new ArrayList<>();
                //int weight = Integer.parseInt(snapshot.child("weight").child().getValue().toString());

                DataSnapshot today = snapshot.child("food").child(year).child(mon).child(day);
                if(today.child("product").child("isEat").exists()){
                    if(Integer.parseInt(today.child("product").child("isEat").getValue().toString()) == 0){
                        if(weight*v - Integer.parseInt(today.child("ateProtein").getValue().toString()) >= 5){
                            int spoon = (int) ((weight*v - Integer.parseInt(today.child("ateProtein").getValue().toString())) / 5);
                            textView.setText("크리틴 "+spoon+"스푼을 1시간 이내에 드시면 좋습니다.");
                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("ea").setValue(spoon);
                            textview2.setVisibility(View.VISIBLE);
                            buttonO.setVisibility(View.VISIBLE);
                            buttonX.setVisibility(View.VISIBLE);
                            buttonOK.setVisibility(View.VISIBLE);
                        }
                        else{
                            textView.setText("오늘 필요한 단백질을 충분히 섭취했습니다!");
                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("ea").setValue(0);
                        }

//                        if(Integer.parseInt(today.child("ateProtein").getValue().toString()) <= weight*2*v - 60){
//                            textView.setText("하루 단백질 섭취 권고량보다 부족합니다.\n제품 3개를 섭취해주세요.");
//                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("ea").setValue(3);
//                            //databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
//                            textview2.setVisibility(View.VISIBLE);
//                            buttonO.setVisibility(View.VISIBLE);
//                            buttonX.setVisibility(View.VISIBLE);
//                            buttonOK.setVisibility(View.VISIBLE);
//                        } else if (Integer.parseInt(today.child("ateProtein").getValue().toString()) <= weight*2*v - 40) {
//                            textView.setText("하루 단백질 섭취 권고량보다 부족합니다.\n제품 2개를 섭취해주세요.");
//                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("ea").setValue(2);
//                            //databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
//                            textview2.setVisibility(View.VISIBLE);
//                            buttonO.setVisibility(View.VISIBLE);
//                            buttonX.setVisibility(View.VISIBLE);
//                            buttonOK.setVisibility(View.VISIBLE);
//                        }else if (Integer.parseInt(today.child("ateProtein").getValue().toString()) <= weight*2*v - 20){
//                            textView.setText("하루 단백질 섭취 권고량보다 부족합니다.\n제품 1개를 섭취해주세요.");
//                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("ea").setValue(1);
//                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(0);
//                            textview2.setVisibility(View.VISIBLE);
//                            buttonO.setVisibility(View.VISIBLE);
//                            buttonX.setVisibility(View.VISIBLE);
//                            buttonOK.setVisibility(View.VISIBLE);
//                        }
//                        else{
//                            textView.setText("충분한 단백질을 섭취했습니다!");
//                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("ea").setValue(0);
//                            //databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(1);
//                        }

                    }
                    //제품을 먹었다면 단배질이 권장량보다 부족해도 완료
                    else
                        textView.setText("오늘 필요한 단백질을 충분히 섭취했습니다!");
                }

                for(int i = 1; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    String day;
                    if(i<10) day = "0"+ i;
                    else day = String.valueOf(i);

                    int weight;
                    double v;
                    if(snapshot.child("weight").child(year).child(mon).child(day).exists()){
                        weight = Integer.parseInt(snapshot.child("weight").child(year).child(mon).child(day).getValue().toString());
                    }
                    else {
                        //weight = Integer.parseInt(snapshot.child("weight").child("now").getValue().toString());
                        weight = 0;
                    }

                    if(snapshot.child("training").child(year).child(mon).child(day).exists()) v = 1.2;
                    else v = 1;

                    entryChart2.add(new BarEntry(i, (float) (v*weight)));

                    if(snapshot.child("food").child(year).child(mon).child(day).exists()) {
                        entryChart.add(new BarEntry(i, Float.parseFloat(snapshot.child("food").child(year).child(mon).child(day).child("ateProtein").getValue().toString())));
                    }
                    else entryChart.add(new BarEntry(i, 0));

                }
                setBarChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonO.setSelected(true);
                buttonX.setSelected(false);
            }
        });

        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonX.setSelected(true);
                buttonO.setSelected(false);
            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonO.isSelected()) {
                    databaseReference.child("food").child(year).child(mon).child(day).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            int protein = Integer.parseInt(dataSnapshot.child("ateProtein").getValue().toString());
                            int ea = Integer.parseInt(dataSnapshot.child("product").child("ea").getValue().toString());
                            databaseReference.child("food").child(year).child(mon).child(day).child("product").child("isEat").setValue(1);
                            databaseReference.child("food").child(year).child(mon).child(day).child("ateProtein").setValue(protein + ea*5);
                            textView.setText("오늘 필요한 단백질을 충분히 섭취했습니다!");
                            textview2.setVisibility(View.INVISIBLE);
                            buttonO.setVisibility(View.INVISIBLE);
                            buttonX.setVisibility(View.INVISIBLE);
                            buttonOK.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        return myView;
    }

    public void setVisible(){

    }

    public void setBarChart(){

        BarData barData = new BarData();

        BarDataSet barDataSet = new BarDataSet(entryChart, "섭취한 단백질");
        BarDataSet barDataSet2 = new BarDataSet(entryChart2, "권장 단백질");

        // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.
        barDataSet.setColor(Color.BLUE);
        barDataSet2.setColor(Color.argb(255,255,112,67));
        barDataSet.setDrawValues(false);
        barDataSet2.setValueTextSize(10f);

        // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
        barData.addDataSet(barDataSet2);
        barData.addDataSet(barDataSet);

        // 차트에 위의 DataSet 을 넣는다.
        chartFood.setData(barData);

        // 차트 업데이트
        //barChart.setTouchEnabled(false); // 차트 터치 불가능하게
        chartFood.animateXY(700, 700);
        //chartFood.setBackgroundColor(Color.GRAY);
        chartFood.getAxisLeft().setAxisMinimum(0);
        chartFood.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartFood.getXAxis().setSpaceMax(1f);
        chartFood.getXAxis().setSpaceMin(1f);
        chartFood.getXAxis().setDrawGridLines(false);
        chartFood.getXAxis().setXOffset(1f);
        chartFood.getXAxis().setGranularity(1f);
        chartFood.moveViewToX(Float.parseFloat(day)-4);
        chartFood.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDay));
        chartFood.getDescription().setText(year+"년 "+mon+"월");
        chartFood.getDescription().setPosition(chartFood.getWidth()-50, chartFood.getHeight()-15);
        chartFood.getDescription().setTextSize(14f);
        chartFood.getAxisRight().setEnabled(false);
        chartFood.setPinchZoom(false);
        chartFood.setVisibleXRange(8, 8);
        chartFood.setScaleEnabled(false);
        chartFood.setDragXEnabled(true);
        chartFood.setExtraOffsets(5f,5f,5f,15f);
        chartFood.invalidate();
    }
}
