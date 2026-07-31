package com.example.buylist.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.buylist.R;
import com.example.buylist.models.DataManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.Month;
import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BalanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class BalanceFragment extends Fragment {
    BarChart barChart;
    BarDataSet barDataSet;
    Spinner yearsSpinner;
    ArrayList<BarEntry> barEntries;
    Month[] months;
    DataManager dataManager;
    View view1;

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        yearsSpinner = view.findViewById(R.id.yearsChart);
        dataManager = new DataManager(getContext());

        ArrayList<Integer> years = new ArrayList<>();

        for (int i = 2026; i < 2057; i++)
            years.add(i);

        ArrayAdapter<Integer> yearsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, years);
        yearsSpinner.setAdapter(yearsAdapter);
        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view1, int i, long l) {
                generateChart(view, (Integer) yearsSpinner.getSelectedItem());
                System.out.println("Year: "+yearsSpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //generateChart(view);
        return view;

    }

    public void generateChart(View view, int year) {
        // Working on DataSet
        months = Month.values(); //Getting MONTHS names
        barDataSet = new BarDataSet(getBarEntries(year), "Data");

        // Option 2: Set different colors for each bar
        int[] colors = {Color.DKGRAY, Color.RED, Color.GREEN, Color.BLUE,
                Color.YELLOW, Color.CYAN, Color.MAGENTA};

        // Set the colors for the bars
        barDataSet.setColors(colors);

        barDataSet.setValueTextSize(11f);

        // Working on BarChart
        barChart = view.findViewById(R.id.idBarChart);
        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.animateY(2000);
        barChart.getDescription().setEnabled(false);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(6);

        // Set bar width
        data.setBarWidth(0.5f);

        // X-Axis Data
        XAxis xAxis = barChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        //Formatting the lables of the charts with months
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0) {
                    if (months.length > (int) value) {
                        String month = months[(int) value] + "";
                        return month.substring(0, 3);
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }
        });

        // Enable grid lines for X-axis
        xAxis.setDrawGridLines(true);

        // Set grid line color
        xAxis.setGridColor(Color.LTGRAY);

        // Set grid line width
        xAxis.setGridLineWidth(1f);

        // Y-Axis Data
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setGridLineWidth(1f);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = barChart.getAxisRight();

        // Disable right Y-axis
        rightAxis.setEnabled(false);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.animate();

        // Invalidate the chart to refresh
        barChart.invalidate();

        // return view;
    }

    // ArrayList for the first set of bar entries
    private ArrayList<BarEntry> getBarEntries(int year) {
        // Creating a new ArrayList
        barEntries = new ArrayList<>();

        Random random = new Random();
        // Adding entries to the ArrayList for the first set
        for (int i = 0; i < months.length; i++)
            barEntries.add(new BarEntry(i, (float) dataManager.monthlyTotal(year)[i]));
        /*
        --Sample--
        barEntries.add(new BarEntry(1f, 3));
        */
        return barEntries;
    }
}