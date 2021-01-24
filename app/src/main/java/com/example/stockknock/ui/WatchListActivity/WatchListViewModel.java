package com.example.stockknock.ui.WatchListActivity;

import com.example.stockknock.models.Stocks;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class WatchListViewModel extends ViewModel {
    private Realm realm;
    private MutableLiveData<List<Stocks>> watchliststocks;

    public WatchListViewModel(){
        realm=Realm.getDefaultInstance();
        watchliststocks=new MutableLiveData<>();
    }

    public MutableLiveData<List<Stocks>> getWatchliststocks() {
        return watchliststocks;
    }

    public void setWatchliststocks(MutableLiveData<List<Stocks>> watchliststocks) {
        this.watchliststocks = watchliststocks;
    }
    public void getWatchListitems(){
        RealmResults<Stocks> results=realm.where(Stocks.class).equalTo("watchlist",true).findAll();
        if(results!=null|| results.size()==0) {
            watchliststocks.postValue(results);
        }

    }
    public void filterStocks(CharSequence text){
        ArrayList<Stocks> qstock = new ArrayList<>();
        for(Stocks s :watchliststocks.getValue()){
            if(s.getName().toLowerCase().equals(text.toString().toLowerCase())||s.getSymbol().toLowerCase().equals(text.toString().toLowerCase())){
                qstock.add(s);
            }
        }
        watchliststocks.postValue(qstock);

    }

    public Stocks getStockbyname(CharSequence text) {
        Stocks qstock = null;
        for(Stocks s :watchliststocks.getValue()){
            if(s.getName().toLowerCase().equals(text.toString().toLowerCase())||s.getSymbol().toLowerCase().equals(text.toString().toLowerCase())){
                qstock=s;
            }
        }
        return qstock;
    }

    public void removeFromwl(String toString) {

        Stocks s=getStockbyname(toString);
        if(s==null){
            return;
        }
        realm.beginTransaction();
        s.setWatchlist(false);
        realm.commitTransaction();
        getWatchListitems();

    }
}
