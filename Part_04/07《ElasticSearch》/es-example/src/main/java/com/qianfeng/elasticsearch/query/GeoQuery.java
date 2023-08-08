package com.qianfeng.elasticsearch.query;

import java.io.IOException;

public interface GeoQuery {
    public void geoDistanceQuery(String indexName,double lot,double lon,int distance) throws IOException;
    public void geoBoundingBoxQuery(String indexName, double top,double left,double bottom,double right) throws IOException ;
    public void geoPolygonQuery(String indexName) throws IOException ;
}
