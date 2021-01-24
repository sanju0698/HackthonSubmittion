package com.example.stockknock.ui.StockActivity;

import android.util.Log;

import com.anychart.chart.common.dataentry.DataEntry;
import com.example.stockknock.models.CallbackFuture;
import com.example.stockknock.models.Stocks;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class stockViewModel extends ViewModel {
    private static final String TAG = "stockViewModel";
    private Realm realm ;
    private String duration;
    private MutableLiveData<Stocks> currentStock;
    private MutableLiveData<List<DataEntry>> graphdata;
    private Stocks result;
    private String url;
    private static  String TOKEN="0KO7ZPX5VDW7CLMO";
    private String httpresponse="";

    public stockViewModel(){
        realm=Realm.getDefaultInstance();
        currentStock=new MutableLiveData<>();
        graphdata=new MutableLiveData<>();
        setDuration("1 day");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MutableLiveData<Stocks> getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(MutableLiveData<Stocks> currentStock) {
        this.currentStock = currentStock;
    }

    public MutableLiveData<List<DataEntry>> getGraphdata() {
        return graphdata;
    }

    public void setGraphdata(MutableLiveData<List<DataEntry>> graphdata) {
        this.graphdata = graphdata;
    }

    public void checkData(Stocks stock) {
        Stocks tempresult=realm.where(Stocks.class).equalTo("name",stock.getName()).findFirst();
        if(tempresult==null){
            stock.setHistory(true);
            realm.beginTransaction();
            realm.insert(stock);
            realm.commitTransaction();
//            Log.d(TAG, "checkData: added data");
        }
        else {
//            Log.d(TAG, "checkData: "+tempresult.toString());
            currentStock.postValue(tempresult);
            realm.beginTransaction();
            stock.setHistory(true);
            realm.commitTransaction();
        }

        result=realm.where(Stocks.class).equalTo("name",stock.getName()).findFirst();
        result.addChangeListener(new RealmChangeListener<Stocks>() {

            @Override
            public void onChange(Stocks s) {
                currentStock.postValue(s);
            }
        });


    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public void startFetchingData(Stocks stock) {



        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        CallbackFuture future = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            future = new CallbackFuture();
        }
        Request request = new Request.Builder()
                .url(getUrl())
//                .url("https://jsonplaceholder.typicode.com/todos/1")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(future);
        try {
            Response response=future.get();
//            Log.d(TAG, "startFetchingData: "+response.body().string());
            modifyData(stock,response.body().string());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    public void modifyData(Stocks stock,String data){

        try {

            JSONObject json = new JSONObject(data);

            if(getDuration().equals("1 day")){
                Log.d(TAG, "startFetchingData: 1day");
                JSONObject timeSeriesData=json.getJSONObject("Time Series (5min)");
                Log.d(TAG, "StartFetchingData: "+timeSeriesData.get(timeSeriesData.keys().next()));
                JSONObject recentdata= (JSONObject) timeSeriesData.get(timeSeriesData.keys().next());
                realm.beginTransaction();
                currentStock.getValue().setOpen(recentdata.getString("1. open"));
                currentStock.getValue().setClose(recentdata.getString("4. close"));
                currentStock.getValue().setHigh(recentdata.getString("2. high"));
                currentStock.getValue().setLow(recentdata.getString("3. low"));
                currentStock.getValue().setVolume(recentdata.getString("5. volume"));
                currentStock.getValue().setDay1data(timeSeriesData.toString());
                realm.commitTransaction();
                getData(stock);

            }
            if(getDuration().equals("5 days")){

                JSONObject timeSeriesData=json.getJSONObject("Time Series (Daily)");
//                HashMap<String, Object> yourHashMap = new Gson().fromJson(timeSeriesData.toString(), HashMap.class);
//                HashMap<String, Object> required=new HashMap<>();
//                Log.d(TAG, "StartFetchingData: "+timeSeriesData.get(timeSeriesData.keys().next()));
                JSONObject recentdata= (JSONObject) timeSeriesData.get(timeSeriesData.keys().next());
                realm.beginTransaction();
                stock.setOpen(recentdata.getString("1. open"));
                stock.setClose(recentdata.getString("4. close"));
                stock.setHigh(recentdata.getString("2. high"));
                stock.setLow(recentdata.getString("3. low"));
                stock.setDay5data(timeSeriesData.toString());
                realm.commitTransaction();
                getData(stock);


            }
            if(getDuration().equals("6 months")){
                JSONObject timeSeriesData=json.getJSONObject("Monthly Time Series");
//                Log.d(TAG, "StartFetchingData: "+timeSeriesData.get(timeSeriesData.keys().next()));
                JSONObject recentdata= (JSONObject) timeSeriesData.get(timeSeriesData.keys().next());
                realm.beginTransaction();
                stock.setOpen(recentdata.getString("1. open"));
                stock.setClose(recentdata.getString("4. close"));
                stock.setHigh(recentdata.getString("2. high"));
                stock.setLow(recentdata.getString("3. low"));
                stock.setMonth6data(timeSeriesData.toString());
                realm.commitTransaction();
                getData(stock);


            }
            if(getDuration().equals("1 years")){
                JSONObject timeSeriesData=json.getJSONObject("Monthly Time Series");
//                Log.d(TAG, "StartFetchingData: "+timeSeriesData.get(timeSeriesData.keys().next()));
                JSONObject recentdata= (JSONObject) timeSeriesData.get(timeSeriesData.keys().next());
                realm.beginTransaction();
                stock.setOpen(recentdata.getString("1. open"));
                stock.setClose(recentdata.getString("4. close"));
                stock.setHigh(recentdata.getString("2. high"));
                stock.setLow(recentdata.getString("3. low"));
                stock.setYear1data(timeSeriesData.toString());
                realm.commitTransaction();
                getData(stock);


            }
            if(getDuration().equals("5 years")){
                JSONObject timeSeriesData=json.getJSONObject("Monthly Time Series");
//                Log.d(TAG, "StartFetchingData: "+timeSeriesData.get(timeSeriesData.keys().next()));
                JSONObject recentdata= (JSONObject) timeSeriesData.get(timeSeriesData.keys().next());
                realm.beginTransaction();
                stock.setOpen(recentdata.getString("1. open"));
                stock.setClose(recentdata.getString("4. close"));
                stock.setHigh(recentdata.getString("2. high"));
                stock.setLow(recentdata.getString("3. low"));
                stock.setYear5data(timeSeriesData.toString());
                realm.commitTransaction();

                getData(stock);


            }

//

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void settoWatchlist(Stocks stock) {

        realm.beginTransaction();
        currentStock.getValue().setWatchlist(true);
        realm.commitTransaction();
//        Log.d(TAG, "settoWatchlist: "+currentStock.getValue().toString());

    }

    public void getData( Stocks stock) {
        List<DataEntry> inputdata=new ArrayList<>();
        String data="";
        int flag=100;

//        Log.d(TAG, "getData: after data");

        switch (getDuration().trim()){
            case "1 day":
//                Log.d(TAG, "getData: into 1 day");
                data=stock.getDay1data();
                flag=1;
                break;
            case "5 days":
                data=stock.getDay5data();
                flag=2;
                break;
            case "6 months":
                data=stock.getMonth6data();
                flag=3;

                break;
            case "1 years":
                data=stock.getYear1data();
                flag=4;

                break;
            case "5 years":
                data=stock.getYear5data();
                flag=5;

                break;
            default:
                data="";
        }
//        Log.d(TAG, "getData: inside getdata "+data);
        if(data!=null&&!data.equals("")){
            if(flag==1){
                Log.d(TAG, "getData: inside1");
                HashMap<String, Object> datamap = new Gson().fromJson(data, HashMap.class);
                for(Map.Entry element:datamap.entrySet()){
                    String key=(String)element.getKey();
                    Map value=(Map)element.getValue();
//                Log.d(TAG, "getData: "+value.get("1. open").toString());
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date d = f.parse(key);
                        long milliseconds = d.getTime();
                        inputdata.add(new stockActivity.OHCLDataEntry(milliseconds,Double.parseDouble(value.get("1. open").toString()),Double.parseDouble(value.get("2. high").toString()),Double.parseDouble(value.get("3. low").toString()),Double.parseDouble(value.get("4. close").toString())));
//                    Log.d(TAG, "getData: "+milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
            if (flag==2){
                Log.d(TAG, "getData: inside2");

                int count=0;
                HashMap<String, Object> datamap = new Gson().fromJson(data, HashMap.class);
                for(Map.Entry element:datamap.entrySet()){
                    if(count==6){
                        break;
                    }
                    count++;
                    Log.d(TAG, "getData: inloop");
                    String key=(String)element.getKey();
                    Map value=(Map)element.getValue();
//                Log.d(TAG, "getData: "+value.get("1. open").toString());
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d = f.parse(key);
                        long milliseconds = d.getTime();
                        inputdata.add(new stockActivity.OHCLDataEntry(milliseconds,Double.parseDouble(value.get("1. open").toString()),Double.parseDouble(value.get("2. high").toString()),Double.parseDouble(value.get("3. low").toString()),Double.parseDouble(value.get("4. close").toString())));
//                    Log.d(TAG, "getData: "+milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(flag==3){
                Log.d(TAG, "getData: inside3");

                int count=0;
                HashMap<String, Object> datamap = new Gson().fromJson(data, HashMap.class);
                for(Map.Entry element:datamap.entrySet()){
                    if(count==6){
                        break;
                    }
                    count++;
                    String key=(String)element.getKey();
                    Map value=(Map)element.getValue();
//                Log.d(TAG, "getData: "+value.get("1. open").toString());
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d = f.parse(key);
                        long milliseconds = d.getTime();
                        inputdata.add(new stockActivity.OHCLDataEntry(milliseconds,Double.parseDouble(value.get("1. open").toString()),Double.parseDouble(value.get("2. high").toString()),Double.parseDouble(value.get("3. low").toString()),Double.parseDouble(value.get("4. close").toString())));
//                    Log.d(TAG, "getData: "+milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(flag==4){
                int count=0;
                HashMap<String, Object> datamap = new Gson().fromJson(data, HashMap.class);
                for(Map.Entry element:datamap.entrySet()){
                    if(count==12){
                        break;
                    }
                    count++;
                    String key=(String)element.getKey();
                    Map value=(Map)element.getValue();
//                Log.d(TAG, "getData: "+value.get("1. open").toString());
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d = f.parse(key);
                        long milliseconds = d.getTime();
                        inputdata.add(new stockActivity.OHCLDataEntry(milliseconds,Double.parseDouble(value.get("1. open").toString()),Double.parseDouble(value.get("2. high").toString()),Double.parseDouble(value.get("3. low").toString()),Double.parseDouble(value.get("4. close").toString())));
//                    Log.d(TAG, "getData: "+milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(flag==5){
                int count=0;
                HashMap<String, Object> datamap = new Gson().fromJson(data, HashMap.class);
                for(Map.Entry element:datamap.entrySet()){
                    if(count==60){
                        break;
                    }
                    count++;
                    String key=(String)element.getKey();
                    Map value=(Map)element.getValue();
//                Log.d(TAG, "getData: "+value.get("1. open").toString());
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d = f.parse(key);
                        long milliseconds = d.getTime();
                        inputdata.add(new stockActivity.OHCLDataEntry(milliseconds,Double.parseDouble(value.get("1. open").toString()),Double.parseDouble(value.get("2. high").toString()),Double.parseDouble(value.get("3. low").toString()),Double.parseDouble(value.get("4. close").toString())));
//                    Log.d(TAG, "getData: "+milliseconds);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }





        }
                Log.d(TAG, "getData: "+inputdata.size());


        graphdata.postValue(inputdata);
//        Log.d(TAG, "getData: "+inputdata.size());
//        return inputdata;

    }

    public void logicUrl(String duration ,String symbol) {
        Log.d(TAG, "logicUrl: "+duration);
        String url="";
        switch (duration){
            case"1 day":
                url="https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+symbol+"&interval=5min&apikey="+TOKEN;
                break;
            case "5 days":
                url="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symbol+"&apikey=demo"+TOKEN;

                break;
            case "6 months":
                url="https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol="+symbol+"&apikey="+TOKEN;
                break;
            case "1 years":
                url="https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol="+symbol+"&apikey="+TOKEN;

                break;

            case "5 years":
                url="https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol="+symbol+"&apikey="+TOKEN;

                break;
        }
//        Log.d(TAG, "logicUrl: "+url);
        setUrl(url);
        startFetchingData(currentStock.getValue());

    }


}
