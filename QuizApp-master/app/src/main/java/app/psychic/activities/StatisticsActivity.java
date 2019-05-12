package app.psychic.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import app.psychic.App;
import app.psychic.R;
import app.psychic.databinding.ActivityStatisticsBinding;
import app.psychic.models.Statistics;
import app.psychic.utils.Constants;

public class StatisticsActivity extends AppCompatActivity {

    ActivityStatisticsBinding binding;
    private List<Statistics> statistics = new ArrayList<>();
    private ProgressDialog dialog;
    private List<String> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_statistics);

        statistics = App.getDatabase().statisticsDao().getStatistics(Constants.selectedSet);

        for (int i = 0; i < statistics.size(); i++) {
            values.add("A: " + (i + 1));
        }

        drewChart();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void moveToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void showLoading(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new ProgressDialog(StatisticsActivity.this);
                    dialog.setMessage(message);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    if (!isFinishing()) {
                        dialog.show();
                    }
                } else {
                    hideLoading();
                    showLoading(message);
                }
            }
        });
    }

    public void hideLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    private void drewChart() {
        binding.chart.setDrawBarShadow(false);
        binding.chart.setDrawValueAboveBar(true);
        binding.chart.setMaxVisibleValueCount(10);
        binding.chart.setVisibleXRangeMaximum(7f);
        binding.chart.setPinchZoom(false);
        binding.chart.setDrawGridBackground(false);

        XAxis xl = binding.chart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setDrawGridLines(false);
        xl.setValueFormatter(new IndexAxisValueFormatter(values));

        YAxis leftAxis = binding.chart.getAxisLeft();
        leftAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f); // this replaces setStartAtZero(true
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(false);
        binding.chart.getAxisRight().setEnabled(false);

        List<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < statistics.size(); i++) {
            yVals1.add(new BarEntry(i, statistics.get(i).getScore()));
        }

        BarDataSet set1;

        if (binding.chart.getData() != null && binding.chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) binding.chart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setValueFormatter(new BarValueFormatter());
            binding.chart.getData().notifyDataChanged();
            binding.chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Attempts");
            set1.setColor(ContextCompat.getColor(this, R.color.colorChartTwo));
            set1.setValueFormatter(new BarValueFormatter());
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            Description description = new Description();
            description.setText("");
            binding.chart.setDescription(description);
            binding.chart.setData(data);
        }
        binding.chart.invalidate();
    }

    public class BarValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "Score " + (int) value;
        }
    }
}
