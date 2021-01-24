package com.example.stockknock.ui.HistoryActivity;

import com.example.stockknock.models.Stocks;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class HistoryActivityViewModel extends ViewModel {
    private Realm realm;
    private MutableLiveData<List<Stocks>> historystocks;

    public HistoryActivityViewModel(){
        realm=Realm.getDefaultInstance();
        historystocks=new MutableLiveData<>();
    }

    public MutableLiveData<List<Stocks>> getHistorystocks() {
        return historystocks;
    }

    public void setHistorystocks(MutableLiveData<List<Stocks>> historystocks) {
        this.historystocks = historystocks;
    }

    public void getHistoryListitems() {
        RealmResults<Stocks> results=realm.where(Stocks.class).equalTo("history",true).and().findAll();
        if(results!=null|| results.size()==0) {
            historystocks.postValue(results);
        }
    }

    public Stocks getStockbyname(CharSequence text) {
        Stocks qstock = null;
        for(Stocks s :historystocks.getValue()){
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
        s.deleteFromRealm();
        realm.commitTransaction();
        getHistoryListitems();

    }
}
