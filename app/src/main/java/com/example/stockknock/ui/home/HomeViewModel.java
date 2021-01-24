package com.example.stockknock.ui.home;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.stockknock.models.CallbackFuture;
import  com.example.stockknock.models.Stocks;
import com.google.gson.Gson;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static final String TAG = "HomeViewModel";
    private List<Stocks> searchlist;
    private MutableLiveData<List<String>> stocknames;
    private List<String> names;
    private Realm realm;
    private MutableLiveData<String> history;
    private MutableLiveData<String> watchlist;
    private RealmResults<Stocks> historyresult;
    private  RealmResults<Stocks> watchhistory;


    public HomeViewModel() {
        mText = new MutableLiveData<>();
        searchlist=new ArrayList<>();
        stocknames=new MutableLiveData<>();
        names=new ArrayList<>();
        realm=Realm.getDefaultInstance();
        watchlist=new MutableLiveData<>();
        history=new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }


    void resetStocknames(){
        names.clear();
        searchlist.clear();
        stocknames.postValue(null);
    }

    LiveData<List<String>> getnames(){
        return stocknames;
}

    public MutableLiveData<String> getHistory() {
        return history;
    }

    public void setHistory(MutableLiveData<String> history) {
        this.history = history;
    }

    public MutableLiveData<String> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(MutableLiveData<String> watchlist) {
        this.watchlist = watchlist;
    }

    void getStocks(String charSequence){
//        String data="{\"ResultSet\":{\"Query\":\"tes\",\"Result\":[{\"symbol\":\"TSLA\",\"name\":\"Tesla, Inc.\",\"exch\":\"NMS\",\"type\":\"S\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Equity\"},{\"symbol\":\"AEHR\",\"name\":\"Aehr Test Systems\",\"exch\":\"NCM\",\"type\":\"S\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Equity\"},{\"symbol\":\"TESS\",\"name\":\"TESSCO Technologies Incorporated\",\"exch\":\"NMS\",\"type\":\"S\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Equity\"},{\"symbol\":\"TESIX\",\"name\":\"Franklin Mutual Shares Fund Class A\",\"exch\":\"NAS\",\"type\":\"M\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Fund\"},{\"symbol\":\"TSCDY\",\"name\":\"Tesco PLC\",\"exch\":\"PNK\",\"type\":\"S\",\"exchDisp\":\"OTC Markets\",\"typeDisp\":\"Equity\"},{\"symbol\":\"PQQQX\",\"name\":\"Test Mutual Fund 3 - PIMC\",\"exch\":\"NAS\",\"type\":\"M\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Fund\"},{\"symbol\":\"TESTAX\",\"name\":\"TEST - Separately Managed Accou\",\"exch\":\"NAS\",\"type\":\"M\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Fund\"},{\"symbol\":\"TSNP\",\"name\":\"Tesoro Enterprises, Inc.\",\"exch\":\"PNK\",\"type\":\"S\",\"exchDisp\":\"OTC Markets\",\"typeDisp\":\"Equity\"},{\"symbol\":\"DDMMX\",\"name\":\"Test Mutual Fund - FWTC\",\"exch\":\"NAS\",\"type\":\"M\",\"exchDisp\":\"NASDAQ\",\"typeDisp\":\"Fund\"},{\"symbol\":\"TXLZF\",\"name\":\"Tesla Exploration Ltd.\",\"exch\":\"PNK\",\"type\":\"S\",\"exchDisp\":\"OTC Markets\",\"typeDisp\":\"Equity\"}]}}";
String url="https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/auto-complete?query="+charSequence.toString()+"&region=US";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        CallbackFuture future = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            future = new CallbackFuture();
        }
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("x-rapidapi-key", "5392d77ec9msh2e9abaf59e1348ap11a71ajsn389494b33903")
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .addHeader("useQueryString", "true")
                .build();
//        client.newCall(request).enqueue(future);
//        try {
//            Response response=future.get();
//            modifyData(response.body().string());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

           client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(@NotNull Call call, @NotNull IOException e) {

               }

               @Override
               public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                   Log.d(TAG, "onResponse: "+response.body().string());
                   modifyData(response.body().string());
//                   try {
//
////                       JSONObject json = new JSONObject(response.body().string());
////                       JSONObject resset = json.getJSONObject("ResultSet");
////                       JSONObject res = json.getJSONObject("Result");
////                       Log.d(TAG, "onResponse: "+res.toString());
//
//
//                   } catch (JSONException e) {
//                       e.printStackTrace();
//                   }


               }
           });




    }
    public  void modifyData(String data){
        JSONObject json = null;
        try {
            searchlist.clear();
            names.clear();
            json = new JSONObject(data);
            JSONObject resset = json.getJSONObject("ResultSet");
            JSONArray res = resset.getJSONArray("Result");
            for(int i =0;i<res.length();i++){
                Stocks stock= new Gson().fromJson(String.valueOf(res.getJSONObject(i)),Stocks.class);
                names.add(stock.getName());
                searchlist.add(stock);

            }
            stocknames.postValue(names);
//            Stocks stock= new Gson().fromJson(String.valueOf(res),Stocks.class);
            Log.d(TAG, "getStocks: "+searchlist.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Stocks getStockbyname(CharSequence text) {
        for (Stocks s:searchlist){
            if(s.getName().contentEquals(text)){
                return s;
            }
        }
        return null;
    }
    public void checkHistorywatchlist(){
       historyresult =realm.where(Stocks.class).equalTo("history",true).findAll();
       history.postValue(String.valueOf(historyresult.size()));
       historyresult.addChangeListener(new RealmChangeListener<RealmResults<Stocks>>() {
           @Override
           public void onChange(RealmResults<Stocks> stocks) {
               Log.d(TAG, "onChange: of history triggered");
               history.postValue(String.valueOf(stocks.size()));

           }
       });


       watchhistory=realm.where(Stocks.class).equalTo("watchlist",true).findAll();
        watchlist.postValue(String.valueOf(watchhistory.size()));
        watchhistory.addChangeListener(new RealmChangeListener<RealmResults<Stocks>>() {
            @Override
            public void onChange(RealmResults<Stocks> stocks) {
                Log.d(TAG, "onChange: of watchlist triggered");
                watchlist.postValue(String.valueOf(stocks.size()));

            }
        });

    }
}