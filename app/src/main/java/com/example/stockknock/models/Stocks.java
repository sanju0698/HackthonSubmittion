package com.example.stockknock.models;

import io.realm.RealmObject;

public class Stocks extends RealmObject {
    private String symbol;
    private String name;
    private String type;
    private String exch;
    private String exchDisp;
    private String typeDisp;
    private Boolean history,watchlist;
    private String open,high,low,close,volume;
    private String day1data,day5data,month6data,year1data,year5data;



    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExch() {
        return exch;
    }

    public void setExch(String exch) {
        this.exch = exch;
    }

    public String getExchDisp() {
        return exchDisp;
    }

    public void setExchDisp(String exchDisp) {
        this.exchDisp = exchDisp;
    }

    public String getTypeDisp() {
        return typeDisp;
    }

    public void setTypeDisp(String typeDisp) {
        this.typeDisp = typeDisp;
    }

    public Boolean getHistory() {
        return history;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDay1data() {
        return day1data;
    }

    public void setDay1data(String day1data) {
        this.day1data = day1data;
    }

    public String getDay5data() {
        return day5data;
    }

    public void setDay5data(String day5data) {
        this.day5data = day5data;
    }

    public String getMonth6data() {
        return month6data;
    }

    public void setMonth6data(String month6data) {
        this.month6data = month6data;
    }

    public String getYear1data() {
        return year1data;
    }

    public void setYear1data(String year1data) {
        this.year1data = year1data;
    }

    public String getYear5data() {
        return year5data;
    }

    public void setYear5data(String year5data) {
        this.year5data = year5data;
    }

    public Boolean getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Boolean watchlist) {
        this.watchlist = watchlist;
    }

    public void setHistory(Boolean history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", exch='" + exch + '\'' +
                ", exchDisp='" + exchDisp + '\'' +
                ", typeDisp='" + typeDisp + '\'' +
                '}';
    }
}
