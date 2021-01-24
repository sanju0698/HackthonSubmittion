package com.example.stockknock.ui.HistoryActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stockknock.R;
import com.example.stockknock.models.Stocks;
import com.example.stockknock.ui.StockActivity.stockActivity;
import com.example.stockknock.ui.WatchListActivity.WatchListViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView historylist;
    private HistoryActivityViewModel historyActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyActivityViewModel= new ViewModelProvider(this).get(HistoryActivityViewModel.class);
        historylist=findViewById(R.id.historylist);
        historyActivityViewModel.getHistoryListitems();
        historyActivityViewModel.getHistorystocks().observeForever(new Observer<List<Stocks>>() {
            @Override
            public void onChanged(List<Stocks> stocks) {
                if(stocks.size()>0){
                    List<String> stocknames=new ArrayList<>();
                    for (Stocks s:stocks){
                        stocknames.add(s.getName());
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, stocknames);
                    historylist.setAdapter(arrayAdapter);

                }
            }
        });
        historylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stocks s=historyActivityViewModel.getStockbyname(historylist.getItemAtPosition(i).toString());
                Intent intent=new Intent(getApplicationContext(), stockActivity.class);
                intent.putExtra("initialvals",  s.toString());
                startActivity(intent);

            }
        });
        historylist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                historyActivityViewModel.removeFromwl(historylist.getItemAtPosition(i).toString());
                Toast.makeText(getApplicationContext(),historylist.getItemAtPosition(i).toString()+" will be removed from watch list",Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }
}