package com.example.stockknock.ui.StockActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HighLowDataEntry;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.anychart.enums.StockSeriesType;
import com.example.stockknock.R;
import com.example.stockknock.models.Stocks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class stockActivity extends AppCompatActivity {
    private stockViewModel stockViewModel;
    private static final String TAG = "StockActivity";
    private ChipGroup intervalchips;
    Gson gson = new Gson();
    private List<String> intervals=new ArrayList<>();
    private TextView open,close,high,low,volume;
    private Button wlbtn;
    private AnyChartView anyChartView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        String s=  getIntent().getStringExtra("initialvals");
//        Log.d(TAG, "onCreate: "+s.toString());
        Stocks stock = gson.fromJson(s, Stocks.class);
        stockViewModel= new ViewModelProvider(this).get(stockViewModel.class);
        TextView stockname=findViewById(R.id.stockname);
        TextView symbol=findViewById(R.id.symbol);
        intervalchips=findViewById(R.id.intervalchips);
        open=findViewById(R.id.open);
        close=findViewById(R.id.close);
        high=findViewById(R.id.high);
        low=findViewById(R.id.low);
        volume=findViewById(R.id.volume);
        wlbtn=findViewById(R.id.watchlistbtn);
        anyChartView = findViewById(R.id.any_chart_view);
        stockname.setText(stock.getName());
        symbol.setText(String.format("Symbol: %s", stock.getSymbol()));
        stockViewModel.checkData(stock);

//        stockViewModel.startFetchingData(stock);


        Table table = Table.instantiate("x");
//        table.addData(stockViewModel.getData());

        TableMapping mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");

        Stock chartstock = AnyChart.stock();
        Plot plot = chartstock.plot(0);
        plot.yGrid(true)
                .xGrid(true)
                .yMinorGrid(true)
                .xMinorGrid(true);

        plot.ema(table.mapAs("{value: 'close'}"), 20d, StockSeriesType.LINE);

        plot.ohlc(mapping)
                .name("NYSE")
                .legendItem("{\n" +
                        "        iconType: 'rising-falling'\n" +
                        "      }");

        chartstock.scroller().ohlc(mapping);

        anyChartView.setChart(chartstock);




        intervals.add("1 day");
        intervals.add("5 days");
        intervals.add("6 months");
        intervals.add("1 years");
        intervals.add("5 years");
        for (String interval : intervals) {
            Chip chip = new Chip(Objects.requireNonNull(this));
            ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
            chip.setText(interval);
            chip.setChipDrawable(drawable);
            chip.setPadding(80, 20, 80, 20);
            intervalchips.addView(chip);

        }
        intervalchips.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip= findViewById(checkedId);
                if(chip!=null){
                    Log.d(TAG, "onCheckedChanged: "+chip.getText());
                    stockViewModel.setDuration(chip.getText().toString());
                    stockViewModel.logicUrl(chip.getText().toString(),stock.getSymbol());

                }
            }
        });
        wlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: button clicked"+stock.toString());
                stockViewModel.settoWatchlist(stock);
                Toast.makeText(getApplicationContext(),"Added to watchlist",Toast.LENGTH_SHORT).show();
            }
        });
        stockViewModel.getGraphdata().observeForever(new Observer<List<DataEntry>>() {
            @Override
            public void onChanged(List<DataEntry> dataEntries) {
                Log.d(TAG, "onChanged: graph data added");
                table.addData(dataEntries);
            }
        });



        Log.d(TAG, "onCreate: "+stockViewModel.getCurrentStock().toString());
        stockViewModel.getCurrentStock().observeForever(new Observer<Stocks>() {
            @Override
            public void onChanged(Stocks stocks) {
                Log.d(TAG, "onChanged: "+stocks.toString());
                open.setText(String.format("Open: %s", stocks.getOpen()));
                close.setText(String.format("Close: %s", stocks.getClose()));
                high.setText(String.format("High: %s", stocks.getHigh()));
                low.setText(String.format("Low: %s", stocks.getLow()));
                volume.setText(String.format("Volume: %s", stocks.getVolume()));

//                table.addData(stockViewModel.getData());

            }
        });



    }
    //Testing purpose//

    static class OHCLDataEntry extends HighLowDataEntry {
        OHCLDataEntry(Long x, Double open, Double high, Double low, Double close) {
            super(x, high, low);
            setValue("open", open);
            setValue("close", close);
        }
    }


}