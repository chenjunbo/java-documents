package com.qianfeng.elasticsearch.query.impl;

import com.qianfeng.elasticsearch.query.GeoQuery;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("geoQuery")
public class GeoQueryImpl implements GeoQuery {
    private final static Logger log = LoggerFactory.getLogger(BaseQueryImpl.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 以某个经纬度为中心查询周围限定距离的文档
     * @param indexName    索引
     * @param lot          经度
     * @param lon          纬度
     * @param distance     距离
     * @throws IOException
     */
    public void geoDistanceQuery(String indexName, double lot, double lon, int distance) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location")
                .point(lot,lon).
                distance(distance, DistanceUnit.KILOMETERS).
                geoDistance(GeoDistance.ARC);
        searchSourceBuilder.query(geoDistanceQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    /**
     * 搜索矩形范围内的文档
     * @param indexName    索引
     * @param top          最上边的纬度
     * @param left         最左边的经度
     * @param bottom       最下边的纬度
     * @param right        右边的经度
     * @throws IOException
     */
    public void geoBoundingBoxQuery(String indexName, double top,double left,double bottom,double right) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        GeoBoundingBoxQueryBuilder address = QueryBuilders.geoBoundingBoxQuery("location").setCorners(top, left, bottom, right);
        searchSourceBuilder.query(address);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    public void geoPolygonQuery(String indexName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        List<GeoPoint> points=new ArrayList<GeoPoint>();
        points.add(new GeoPoint(42, -72));
        points.add(new GeoPoint(39, 117));
        points.add(new GeoPoint(40, 117));
        GeoPolygonQueryBuilder geoPolygonQueryBuilder = QueryBuilders.geoPolygonQuery("location", points);
        searchSourceBuilder.query(geoPolygonQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println(hit.getSourceAsMap());
        }
    }
}
