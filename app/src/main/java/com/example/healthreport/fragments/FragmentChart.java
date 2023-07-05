package com.example.healthreport.fragments;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentChart extends Fragment {

    LineChart chartTrain, chartWight;
    BarChart chartProtein;
    double weight, set, repeat;
    ArrayList<Entry> entryTrain, entryWeight;
    ArrayList<BarEntry> entryProtein, entryProtein2;
    ArrayList<String> xAxisDay;
    ArrayList<String> program, healthItem;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    DatabaseReference databaseReference;
    Button changeDayTrain, changeDayWeight, changeDayProtein;
    DialogFragmentDatePicker dialogFragmentDatePicker;
    TextView today;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_chart, container, false);

        chartTrain = myView.findViewById(R.id.chart_chart_train);
        chartWight = myView.findViewById(R.id.chart_chart_weight);
        chartProtein = myView.findViewById(R.id.chart_chart_protein);

        changeDayTrain = myView.findViewById(R.id.button_chart_change_day_train);
        changeDayWeight = myView.findViewById(R.id.button_chart_change_day_weight);
        changeDayProtein = myView.findViewById(R.id.button_chart_change_day_protein);

        today = myView.findViewById(R.id.textView_chart_today);

        program = new ArrayList<>(Arrays.asList("endurance", "hypertrophy", "strength"));
        healthItem = new ArrayList<>(Arrays.asList("squat", "bench", "dead", "crunch", "seatedCable", "cablePushDown","lat", "overHead", "bicepsCurl"));

        Calendar calendar =  new GregorianCalendar();
        String year = mFormat.format(calendar.getTime()).substring(0,4);
        String mon = mFormat.format(calendar.getTime()).substring(5,7);
        String day = mFormat.format(calendar.getTime()).substring(8,10);

        today.setText("오늘: "+year+"년 "+mon+"월 "+day+"일");

        databaseReference = ((MainActivity)getActivity()).databaseReference;

        setTrain(year, mon, day);
        setWeight(year, mon, day);
        setProtein(year,mon, day);

        changeDayTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentDatePicker = new DialogFragmentDatePicker();
                Bundle bundle = new Bundle();
                bundle.putString("chart","1");
                dialogFragmentDatePicker.setArguments(bundle);
                dialogFragmentDatePicker.show(getChildFragmentManager(), null);
            }
        });

        changeDayWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentDatePicker = new DialogFragmentDatePicker();
                Bundle bundle = new Bundle();
                bundle.putString("chart","2");
                dialogFragmentDatePicker.setArguments(bundle);
                dialogFragmentDatePicker.show(getChildFragmentManager(), null);
            }
        });

        changeDayProtein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragmentDatePicker = new DialogFragmentDatePicker();
                Bundle bundle = new Bundle();
                bundle.putString("chart","3");
                dialogFragmentDatePicker.setArguments(bundle);
                dialogFragmentDatePicker.show(getChildFragmentManager(), null);
            }
        });


        return myView;
    }

    public void setTrain(String year, String mon, String day){
        entryTrain = new ArrayList<>();
        xAxisDay = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(mon)-1, 1);

        databaseReference.child("training").child(year).child(mon).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for(int i = 0; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    xAxisDay.add(i +"일");
                }

                for(int i = 1; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    String day;
                    if(i<10) day = "0"+ i;
                    else day = String.valueOf(i);

                    if(dataSnapshot.child(day).exists()) {
                        float sum = 0;
                        for(int j = 0; j<program.size(); j++){
                            for(int k = 0; k<healthItem.size(); k++){
                                if(dataSnapshot.child(day).child(program.get(j)).child(healthItem.get(k)).exists()){
                                    weight = Float.parseFloat(String.valueOf(dataSnapshot.child(day).child(program.get(j)).child(healthItem.get(k)).child("weight").getValue()));
                                    set = Float.parseFloat(String.valueOf(dataSnapshot.child(day).child(program.get(j)).child(healthItem.get(k)).child("set").getValue()));
                                    repeat = Float.parseFloat(String.valueOf(dataSnapshot.child(day).child(program.get(j)).child(healthItem.get(k)).child("repeat").getValue()));
                                    sum+=weight*set*repeat;
                                }
                            }
                        }
                        entryTrain.add(new Entry(i, sum));
                    }
                    else entryTrain.add(new Entry(i, 0));

                }
                setLineChart(chartTrain, entryTrain, xAxisDay, "볼륨(무게x반복x세트)", year, mon, day);
            }
        });
    }

    public void setWeight(String year, String mon, String day){
        entryWeight = new ArrayList<>();
        xAxisDay = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(mon)-1, 1);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for(int i = 0; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    xAxisDay.add(i +"일");
                }

                for(int i = 1; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    String day;
                    if(i<10) day = "0"+ i;
                    else day = String.valueOf(i);

                    if(dataSnapshot.child("weight").child(year).child(mon).child(day).exists()) {
                        int weight = Integer.parseInt(dataSnapshot.child("weight").child(year).child(mon).child(day).getValue().toString());


                        entryWeight.add(new Entry(i, weight));
                    }
                    else entryWeight.add(new Entry(i, 0));

                }
                setLineChart(chartWight, entryWeight, xAxisDay,"몸무게", year, mon, day);
            }
        });
    }

    public void setProtein(String year, String mon, String day){
        entryProtein = new ArrayList<>();
        entryProtein2 = new ArrayList<>();
        xAxisDay = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(mon)-1, 1);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for(int i = 0; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    xAxisDay.add(i +"일");
                }

                for(int i = 1; i<calendar.getActualMaximum(Calendar.DAY_OF_MONTH)+1; i++){
                    String day;
                    if(i<10) day = "0"+ i;
                    else day = String.valueOf(i);

                    int weight;
                    double v;
                    if(dataSnapshot.child("weight").child(year).child(mon).child(day).exists()){
                        weight = Integer.parseInt(dataSnapshot.child("weight").child(year).child(mon).child(day).getValue().toString());
                    }
                    else {
                        //weight = Integer.parseInt(dataSnapshot.child("weight").child("now").getValue().toString());
                        weight = 0;
                    }

                    if(dataSnapshot.child("training").child(year).child(mon).child(day).exists()) v = 1.2;
                    else v = 1;

                    entryProtein2.add(new BarEntry(i, (float) (v*weight)));

                    if(dataSnapshot.child("food").child(year).child(mon).child(day).exists()) {
                        entryProtein.add(new BarEntry(i, Float.parseFloat(dataSnapshot.child("food").child(year).child(mon).child(day).child("ateProtein").getValue().toString())));
                    }
                    else entryProtein.add(new BarEntry(i, 0));

                }
                setBarChart(chartProtein, entryProtein, entryProtein2, xAxisDay, year, mon, day);
            }
        });

    }

    public void setLineChart(LineChart lineChart, ArrayList<Entry> entry, ArrayList<String> xAxisDay, String label, String year, String mon, String day){

        LineData lineData = new LineData();

        LineDataSet lineDataSet = new LineDataSet(entry, label);
        lineDataSet.setColor(Color.BLUE); // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.
        lineDataSet.setDrawCircles(false);
        lineDataSet.setValueTextSize(10f);
        //lineDataSet.setDrawValues(false);

        lineData.addDataSet(lineDataSet); // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.

        lineChart.setData(lineData); // 차트에 위의 DataSet 을 넣는다.

         // 차트 업데이트
        //barChart.setTouchEnabled(false); // 차트 터치 불가능하게
        lineChart.animateXY(700, 700);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisMinimum(0);
        lineChart.getXAxis().setGranularity(1f);
        lineChart.getXAxis().setAxisMinimum(0.8f);
        lineChart.getXAxis().setAxisMaximum((float) (entry.size()+0.2));
        lineChart.moveViewToX(Float.parseFloat(day)-4);
        lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDay));
        lineChart.getDescription().setText(year+"년 "+mon+"월");
        lineChart.getDescription().setPosition(lineChart.getWidth()-50, lineChart.getHeight()-15);
        lineChart.getDescription().setTextSize(12f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setVisibleXRange(8, 8);
        lineChart.setScaleEnabled(false);
        lineChart.setDragXEnabled(true);
        lineChart.setExtraOffsets(5f,5f,5f,15f);
        lineChart.invalidate();
    }

    public void setBarChart(BarChart barChart, ArrayList<BarEntry> entry, ArrayList<BarEntry> entry2, ArrayList<String> xAxisDay, String year, String mon, String day){

        BarData barData = new BarData();

        BarDataSet barDataSet = new BarDataSet(entry, "섭취한 단백질");
        BarDataSet barDataSet2 = new BarDataSet(entry2, "권장 단백질");

        // 해당 BarDataSet 색 설정 :: 각 막대 과 관련된 세팅은 여기서 설정한다.
        barDataSet.setColor(Color.BLUE);
        barDataSet2.setColor(Color.argb(255,255,112,67));
        barDataSet.setDrawValues(false);
        barDataSet2.setValueTextSize(10f);

        // 해당 BarDataSet 을 적용될 차트에 들어갈 DataSet 에 넣는다.
        barData.addDataSet(barDataSet2);
        barData.addDataSet(barDataSet);

        // 차트에 위의 DataSet 을 넣는다.
        barChart.setData(barData);

        // 차트 업데이트
        //barChart.setTouchEnabled(false); // 차트 터치 불가능하게
        barChart.animateXY(700, 700);
        //barChart.setBackgroundColor(Color.GRAY);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setSpaceMax(1f);
        barChart.getXAxis().setSpaceMin(1f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setXOffset(1f);
        barChart.getXAxis().setGranularity(1f);
        barChart.moveViewToX(Float.parseFloat(day)-4);
        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisDay));
        barChart.getDescription().setText(year+"년 "+mon+"월");
        barChart.getDescription().setPosition(barChart.getWidth()-50, barChart.getHeight()-15);
        barChart.getDescription().setTextSize(14f);
        barChart.getAxisRight().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setVisibleXRange(8, 8);
        barChart.setScaleEnabled(false);
        barChart.setDragXEnabled(true);
        barChart.setExtraOffsets(5f,5f,5f,15f);
        barChart.invalidate();
    }


}
