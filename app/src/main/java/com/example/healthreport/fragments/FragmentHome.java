package com.example.healthreport.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentHome extends Fragment {
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatabaseReference databaseReference;
    LineChart chartWeight;
    ArrayList<Entry> entryChart;
    ArrayList<String> xAxisDay;
    Calendar calendar;
    Button introduction, tdee, bmi;
    DialogFragmentTdee dialogFragmentTdee;
    String year, mon, day;
    ImageButton imageButton;
    DialogFragmentIntroduction dialogFragmentIntroduction;
    DialogFragmentBmi dialogFragmentBmi;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.tab_home);

        dialogFragmentTdee = new DialogFragmentTdee();

        TextView profile = myview.findViewById(R.id.textView_profile);

        imageButton = myview.findViewById(R.id.imageButton_insectu);
        introduction = myview.findViewById(R.id.button_home_introduction);
        tdee = myview.findViewById(R.id.button_home_tdee);
        bmi = myview.findViewById(R.id.button_home_bmi);
        chartWeight = myview.findViewById(R.id.chart_home_weight);

        calendar =  new GregorianCalendar();
        year = mFormat.format(calendar.getTime()).substring(0,4);
        mon = mFormat.format(calendar.getTime()).substring(5,7);
        day = mFormat.format(calendar.getTime()).substring(8,10);

        entryChart = new ArrayList<>();
        xAxisDay = new ArrayList<>();
        for(int i = 0; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
            xAxisDay.add(i +"일");
        }

        databaseReference = ((MainActivity)getActivity()).databaseReference;


        tdee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentTdee.show(getChildFragmentManager(), null);
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentTdee).commit();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.insectu.com/"));
                startActivity(intent);
            }
        });

        introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentIntroduction = new DialogFragmentIntroduction();
                dialogFragmentIntroduction.show(getChildFragmentManager(), null);
            }
        });

        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentBmi = new DialogFragmentBmi();
                dialogFragmentBmi.show(getChildFragmentManager(), null);
            }
        });

        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                String name = String.valueOf(dataSnapshot.child("name").getValue());
                String height = String.valueOf(dataSnapshot.child("height").getValue());
                String weightNow = String.valueOf(dataSnapshot.child("weight").child("now").getValue());

                //날짜별 몸무게 기록(단백질 통계에 사용하기위해)
                databaseReference.child("weight").child(year).child(mon).child(day).setValue(weightNow);

                profile.setText("안녕하세요 "+name+"님\n"+"키: "+height+"cm"+"\n몸무게: "+weightNow+"kg");

                for(int i = 1; i<32; i++){
                    String day;
                    if(i<10) day = "0"+ i;
                    else day = String.valueOf(i);

                    if(dataSnapshot.child("weight").child(year).child(mon).child(day).exists()) {
                        int weight = Integer.parseInt(dataSnapshot.child("weight").child(year).child(mon).child(day).getValue().toString());
                        
                        entryChart.add(new Entry(i, weight));
                    }
                    else entryChart.add(new Entry(i, 0));

                }
                setLineChart();
            }
        });

        return myview;
    }

    public void setLineChart(){

        LineData lineData = new LineData();

        LineDataSet lineDataSet = new LineDataSet(entryChart, "몸무게");
        lineDataSet.setColor(Color.BLUE); // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.
        lineDataSet.setDrawCircles(false);
        lineDataSet.setValueTextSize(10f);
        //lineDataSet.setDrawValues(false);

        lineData.addDataSet(lineDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.

        chartWeight.setData(lineData); // 차트에 위의 DataSet 을 넣는다.

        // 차트 업데이트
        //barChart.setTouchEnabled(false); // 차트 터치 불가능하게
        chartWeight.animateXY(700, 700);
        chartWeight.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartWeight.getLegend().setXEntrySpace(10f);
        chartWeight.getAxisLeft().setAxisMinimum(0);
        chartWeight.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartWeight.getXAxis().setDrawGridLines(false);
        chartWeight.getXAxis().setXOffset(1f);
        chartWeight.getXAxis().setGranularity(1f);
        chartWeight.getXAxis().setAxisMinimum(0.8f);
        chartWeight.getXAxis().setAxisMaximum((float) (entryChart.size()+0.2));
        chartWeight.moveViewToX(Float.parseFloat(day)-4);
        chartWeight.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDay));
        chartWeight.getDescription().setText(year+"년 "+mon+"월");
        chartWeight.getDescription().setPosition(chartWeight.getWidth()-50, chartWeight.getHeight()-15);
        chartWeight.getDescription().setTextSize(12f);
        chartWeight.getAxisRight().setEnabled(false);
        chartWeight.setPinchZoom(false);
        chartWeight.setVisibleXRange(8, 8);
        chartWeight.setScaleEnabled(false);
        chartWeight.setDragXEnabled(true);
        chartWeight.setExtraOffsets(5f,5f,5f,15f);
        chartWeight.invalidate();
    }
}
