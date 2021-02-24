package com.farmx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class AraziDurumu extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arazi_durumu);

       double x,y;
        x=-0.5;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
       for (int i = 0 ; i < 500 ; i++){
            x = x +0.1;
            y = Math.sin(x) ;
            series.appendData(new DataPoint(x,y), true, 500);
        }

        graph.addSeries(series);
    }

}