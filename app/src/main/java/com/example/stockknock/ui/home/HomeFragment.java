package com.example.stockknock.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.stockknock.R;
import com.example.stockknock.models.Stocks;
import com.example.stockknock.ui.HistoryActivity.HistoryActivity;
import com.example.stockknock.ui.WatchListActivity.WatchListActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import com.example.stockknock.ui.StockActivity.stockActivity;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private ChipGroup searchchips;
    private TextView watchlist,history;
    private CardView watchlistcard,historycard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextInputEditText stock=root.findViewById(R.id.stocksearch);
        searchchips=root.findViewById(R.id.searchchips);
        watchlist=root.findViewById(R.id.watchcount);
        history=root.findViewById(R.id.historycount);
        watchlistcard=root.findViewById(R.id.watchlistcard);
        historycard=root.findViewById(R.id.historycard);
        watchlistcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), WatchListActivity.class);
                startActivity(i);


            }
        });
        historycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), HistoryActivity.class);
                startActivity(i);
            }
        });

        stock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: "+charSequence);
                if(charSequence.length()%3==0){
//                    homeViewModel.resetStocknames();
                    homeViewModel.getStocks(charSequence.toString());
                }
                else{
                    searchchips.removeAllViews();
                    homeViewModel.resetStocknames();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        homeViewModel.getnames().observeForever(new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                searchchips.removeAllViews();
                if(getContext()!=null && strings!=null){
                    for(String name : strings){
                        Chip chip=new Chip(requireContext());
                        ChipDrawable drawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
                        chip.setText(name);
                        chip.setTextColor(0xFF000000);
                        chip.setChipDrawable(drawable);
                        chip.setPadding(80, 20, 80, 20);
                        searchchips.addView(chip);

                    }
                    addChiplisners();
                }
            }

            private void addChiplisners() {
                searchchips.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
//                        Log.d(TAG, "onCheckedChanged: "+checkedId);
                        Chip chip= root.findViewById(checkedId);
                        if(chip!=null){
                            Stocks s=homeViewModel.getStockbyname(chip.getText());
                            if(s!=null){
                                searchchips.removeAllViews();
                                stock.setText("");
                                Log.d(TAG, "onCheckedChanged: "+s.toString());

                                Intent i=new Intent(getContext(), stockActivity.class);
                                i.putExtra("initialvals",  s.toString());
                                startActivity(i);
                            }



                        }
                    }
                });
            }
        });
        homeViewModel.checkHistorywatchlist();
        history.setText(homeViewModel.getHistory().getValue());
        watchlist.setText(homeViewModel.getWatchlist().getValue());

        homeViewModel.getHistory().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                history.setText(s);

            }
        });
        homeViewModel.getWatchlist().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                watchlist.setText(s);
            }
        });

        return root;
         
    }




}