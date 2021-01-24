package com.example.stockknock.ui.WatchListActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stockknock.R;
import com.example.stockknock.models.Stocks;
import com.example.stockknock.ui.StockActivity.stockActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class WatchListActivity extends AppCompatActivity {
    private WatchListViewModel watchListViewModel;
    private TextInputEditText watchlistfilter;
    private ListView stocklist;
    private static final String TAG = "WatchList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        watchListViewModel= new ViewModelProvider(this).get(WatchListViewModel.class);
        watchlistfilter=findViewById(R.id.watchlistfilter);
        stocklist=findViewById(R.id.stockslist);
        watchListViewModel.getWatchListitems();

        watchlistfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()%3==0){
                    watchListViewModel.filterStocks(charSequence);
                }
                else if(charSequence.length()<3){
                   watchListViewModel.getWatchListitems();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        watchListViewModel.getWatchliststocks().observeForever(new Observer<List<Stocks>>() {
            @Override
            public void onChanged(List<Stocks> stocks) {
                if(stocks.size()>0){
                    List<String> stocknames=new ArrayList<>();
                    for (Stocks s:stocks){
                        stocknames.add(s.getName());
                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, stocknames);
                    stocklist.setAdapter(arrayAdapter);

                }
            }
        });
        stocklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stocks s=watchListViewModel.getStockbyname(stocklist.getItemAtPosition(i).toString());
                Intent intent=new Intent(getApplicationContext(), stockActivity.class);
                intent.putExtra("initialvals",  s.toString());
                startActivity(intent);

            }
        });
        stocklist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                watchListViewModel.removeFromwl(stocklist.getItemAtPosition(i).toString());
                Toast.makeText(getApplicationContext(),stocklist.getItemAtPosition(i).toString()+" will be removed from watch list",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
}