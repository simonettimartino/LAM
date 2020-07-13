package com.example.myhm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityGraficoPeso extends AppCompatActivity {

    private List<Reports>  list;
    private ArrayList arrayList;
    private static String grafico;

    public static void setGrafico(String x){
        grafico = x;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_peso);

        new AsyncTaskRiceviReports().execute();
    }







    public class AsyncTaskRiceviReports extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            list = MainActivity.appDatabase.dataAccessObject().getDati();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            BarChart barChart = findViewById(R.id.idGraf1);
            System.out.println(barChart);

            ArrayList <BarEntry> arrayList = new ArrayList<>();

            for(int i = 0; i < list.size(); i++){
                try {
                    Date d = new SimpleDateFormat("MM/dd/yyyy").parse(list.get(i).getData());
                    Date dO = Calendar.getInstance().getTime();
                    float x = (float)((dO.getTime() - d.getTime())/(1000 * 60 * 60 * 24));
                    System.out.println("x: " + x);
                    float y = 0;
                    switch (grafico){
                        case "peso":
                            y = (float)list.get(i).getPeso().getValore();
                            break;
                        case "temperatura" :
                            y = (float)list.get(i).getTemperatura().getValore();
                            break;
                        case "glicemia" :
                            y = (float)list.get(i).getGlicemia().getValore();
                            break;
                    }
                    arrayList.add(new BarEntry(x, y));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            BarDataSet barDataSet = new BarDataSet(arrayList, "graf");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(16f);

            BarData barData = new BarData(barDataSet);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.animateY(2000);
            barChart.invalidate();


        }
    }
}

