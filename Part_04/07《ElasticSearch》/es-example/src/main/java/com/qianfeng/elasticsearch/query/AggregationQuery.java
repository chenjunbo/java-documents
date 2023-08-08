package com.qianfeng.elasticsearch.query;

import java.io.IOException;
import java.util.Map;

public interface AggregationQuery {
    public  void AvgAggregations(String indexName, String field) throws IOException ;
    public  void cardinalityAggregations(String indexName, String field) throws IOException;
    public  void dateHistogramAggregation(String indexName, String field) throws IOException;
    public  void dateRangeAggregation(String indexName, String field) throws IOException;
    public  void extendedStatsAggregation(String indexName, String field) throws IOException ;
    public void filterAggregation(String indexName, String termField,String termValue) throws IOException ;
    public  void histogramAggregation(String indexName, String field,int interval) throws IOException ;
    public  void histogramDateAggregation(String indexName, String field,int interval) throws IOException ;
    public void termsAggregation(String indexName, long startTime, long endTime) throws IOException;
    public  void geoDistanceAggregation(String indexName) throws IOException ;
}
