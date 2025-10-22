package eu.coach_yourself.app.ui.graph;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.database.DatabaseQueryHelper;
import eu.coach_yourself.app.database.PlaySongs;
import eu.coach_yourself.app.database.RatingCategorySongs;
import eu.coach_yourself.app.model.PieChartData;
import eu.coach_yourself.app.utils.DateUtility;

public class GraphFragment extends Fragment {

    private LineChart chart;
    private GraphViewModel notificationsViewModel;
    private AppCompatSpinner spin_category;
    private AppCompatSpinner spin_time;
    private ArrayList<RatingCategorySongs> getGroupby = new ArrayList<>();
    private ArrayList<String> spinerArray = new ArrayList<>();
    private boolean flagCategory = false;
    private boolean flagMonth = false;
    private PieChart pieChart;
    private TextView textnodata;
    private LinearLayout layoutDataFound;
    SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
//    private ImageView draw_01;
//    private ImageView draw_02;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(GraphViewModel.class);
        View root = inflater.inflate(R.layout.fragment_graph, container, false);
        String nune = DateUtility.getCurrentNumberMonthj();
        pieChart = (PieChart) root.findViewById(R.id.PieChartchart1);
        spin_category = (AppCompatSpinner) root.findViewById(R.id.spin_category);
        spin_time = (AppCompatSpinner) root.findViewById(R.id.spin_time);
        textnodata = (TextView) root.findViewById(R.id.textnodata);
        layoutDataFound = (LinearLayout) root.findViewById(R.id.layoutDataFound);
//        draw_01 = (ImageView) root.findViewById(R.id.draw_01);
//        draw_02 = (ImageView) root.findViewById(R.id.draw_02);
//

        spin_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#0084ff"));
                if (flagCategory) {
                    showChatLine();
                    flagCategory = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spin_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#0084ff"));
                if (flagMonth) {
                    showChatLine();
                    flagMonth = false;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flagCategory = true;
                return false;
            }
        });
        spin_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flagMonth = true;
                return false;
            }
        });

        getGroupby.clear();
        getGroupby.addAll(DatabaseQueryHelper.getGroupby());
        for (RatingCategorySongs mRatingCategorySongs : getGroupby) {
            spinerArray.add(mRatingCategorySongs.getCategoryname());
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinerArray);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_category.setAdapter(dataAdapter2);

        spin_time.setSelection((Integer.parseInt(nune) - 1));
        //spin_time.setSelection(index);


        chart = (LineChart) root.findViewById(R.id.chart);
        chart.getDescription().setEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMinimum(0);
        leftAxis.setGranularity(5);
        leftAxis.setAxisMaximum(10);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


        showChatLine();
        piechart();
        return root;
    }

    public void showChatLine() {
        int selectioncategory = spin_category.getSelectedItemPosition();
        if (spinerArray.size() == 0) {
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineData data = new LineData(dataSets);
            chart.getAxisLeft().setSpaceTop(40);
            chart.getAxisLeft().setSpaceBottom(40);
            chart.getDescription().setEnabled(true);
            chart.setData(data);
            chart.invalidate();
            textnodata.setVisibility(View.VISIBLE);
            layoutDataFound.setVisibility(View.GONE);

            return;
        }

        RatingCategorySongs adound = getGroupby.get(selectioncategory);
        int tint = Color.parseColor(adound.getCategoryColor());
//        draw_01.setColorFilter(tint, android.graphics.PorterDuff.Mode.MULTIPLY);
//        draw_02.setColorFilter(tint, android.graphics.PorterDuff.Mode.MULTIPLY);

        textnodata.setVisibility(View.GONE);
        layoutDataFound.setVisibility(View.VISIBLE);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        String categoryName = spinerArray.get(selectioncategory);
        String selectionMonth = "" + (spin_time.getSelectedItemPosition() + 1);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> valuessend = new ArrayList<>();

        ArrayList<RatingCategorySongs> ratingCategorySongsSart = DatabaseQueryHelper.getRatingCategorySongs("0", selectionMonth, categoryName);
        final String[] quarters = new String[ratingCategorySongsSart.size()];
        for (int i = 0; i < ratingCategorySongsSart.size(); i++) {

            RatingCategorySongs mRatingCategorySongs = ratingCategorySongsSart.get(i);
            float rating = Float.parseFloat(mRatingCategorySongs.getRatingsnumber());
            long dataMin = DateUtility.dateToMillisecond(mRatingCategorySongs.getRatingstime());
            Date date = new Date(dataMin);
            SimpleDateFormat sdf = new SimpleDateFormat("dd:MM", Locale.ENGLISH);
            quarters[i] = sdf.format(date);
            values.add(new Entry(i, rating));
            long idnumber = mRatingCategorySongs.getId();
            RatingCategorySongs mRatingCategorySongsEend = DatabaseQueryHelper.getRatingCategorySongs("1", "" + idnumber);
            if (mRatingCategorySongsEend != null) {
                float ratingEnd = Float.parseFloat(mRatingCategorySongsEend.getRatingsnumber());
                valuessend.add(new Entry(i, ratingEnd));
            }

        }
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int currentValue = (int) value;
                if (currentValue >= 0 && quarters.length > currentValue) {
                    return quarters[(int) value];
                } else {
                    return "";
                }
            }
        };
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(formatter);

        //=====
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawValues(false);
        dataSets.add(set1);

//
//        ArrayList<RatingCategorySongs> ratingCategorySongsend = DatabaseQueryHelper.getRatingCategorySongs("1", selectionMonth, categoryName);
//        Log.e("end", "are" + ratingCategorySongsend.size());
//
//        for (int i = 0; i < ratingCategorySongsend.size(); i++) {
//            RatingCategorySongs mRatingCategorySongs = ratingCategorySongsend.get(i);
//            float rating = Float.parseFloat(mRatingCategorySongs.getRatingsnumber());
//            int checkPosition = checkPosition(ratingCategorySongsSart, mRatingCategorySongs.getRatingstime());
//            Log.e("mRatingCategorySongs", mRatingCategorySongs.getRatingstime());
//
//            valuessend.add(new Entry(checkPosition, rating));
//        }

        LineDataSet set2 = new LineDataSet(valuessend, "DataSet 2");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(1.75f);
        set2.setCircleRadius(5f);
        set2.setCircleHoleRadius(2.5f);
        set2.setValueTextColor(Color.GREEN);
        set2.setColor(Color.GREEN);
        set2.setCircleColor(Color.GREEN);
        set2.setHighLightColor(Color.GREEN);
        set2.setDrawValues(false);
        dataSets.add(set2);


        LineData data = new LineData(dataSets);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getDescription().setEnabled(true);
        chart.setData(data);
        chart.invalidate();
    }

    public int checkPosition(ArrayList<RatingCategorySongs> ratingCategorySongsSart, String ratingstime) {
        int postion = 0;
        String dataMin = DateUtility.getCurrentConvertFromMMDDYYY(ratingstime);
        for (int index = 0; index < ratingCategorySongsSart.size(); index++) {
            String dataMina = DateUtility.getCurrentConvertFromMMDDYYY(ratingCategorySongsSart.get(index).getRatingstime());
            if (dataMina.equalsIgnoreCase(dataMin)) {
                postion = index;
                break;
            }
        }
        return postion;
    }

    public void piechart() {
//
        pieChart.setHoleColor(Color.parseColor("#00000000"));
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(60);

        ArrayList<PieEntry> entries = new ArrayList<>();

        ArrayList<PieChartData> allPieChartData = new ArrayList<>();


        ArrayList<PlaySongs> totalcountPlaysongs = DatabaseQueryHelper.getallPlaySongs();
        ArrayList<Integer> colors = new ArrayList<>();


        if (totalcountPlaysongs.size() > 0) {
            pieChart.setVisibility(View.VISIBLE);

            int total = totalcountPlaysongs.size();
            double value = ((double) 100) / total;
            Log.w("value", "are" + total);
            Log.w("value", "are" + value);


            ArrayList<PlaySongs> allPlaySongs = DatabaseQueryHelper.getPlaySongs();
            for (PlaySongs mPlaySongs : allPlaySongs) {

                int count = DatabaseQueryHelper.getPlaySongsbycategoryid(mPlaySongs.getCategoryid()).size();
                double categoryamount = value * count;
                int lastamount = (int) Math.round(categoryamount);
                PieChartData mPieChartData = new PieChartData();
                mPieChartData.setCategory_id(mPlaySongs.getCategoryid());
                mPieChartData.setCategory_color(mPlaySongs.getText_color());
                mPieChartData.setCategory_percentage(lastamount);
                mPieChartData.setCategory_name(mPlaySongs.getCategoryname());
                allPieChartData.add(mPieChartData);
            }
            for (PieChartData mPieChartData : allPieChartData) {

                int valueFirsrt = (int) mPieChartData.getCategory_percentage();
                int value2nd = (int) mPieChartData.getCategory_percentage();

                entries.add(new PieEntry(valueFirsrt, "" + value2nd + "%"));
                colors.add(Color.parseColor(mPieChartData.getCategory_color()));
            }


            PieDataSet pieDataSet = new PieDataSet(entries, "label");
            //setting text size of the value
            pieDataSet.setValueTextSize(12f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(false);
            pieChart.setData(pieData);
            pieChart.invalidate();
        } else {
            pieChart.setVisibility(View.GONE);
        }


    }

}