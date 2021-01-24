package com.example.stockknock.models;

import com.anychart.chart.common.dataentry.HighLowDataEntry;

public  class OHCLDataEntry extends HighLowDataEntry {
    OHCLDataEntry(Long x, Double open, Double high, Double low, Double close) {
        super(x, high, low);
        setValue("open", open);
        setValue("close", close);
    }
}