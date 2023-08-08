package com.qianfeng.elasticsearch.query.impl;

import com.qianfeng.elasticsearch.query.BaseQuery;
import com.qianfeng.elasticsearch.query.SortQuery;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("sortQuery")
public class SortQueryImpl implements SortQuery {
    private final static Logger log = LoggerFactory.getLogger(SortQueryImpl.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Override
    public void queryMatch(String indexName, String field,String keyWord) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field,keyWord));
        searchSourceBuilder.sort("replyTotal");
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap() +",score:"+ hit.getScore());
        }
    }

    @Override
    public void sortQuery(String indexName, String field,String keyWord,String sort,SortOrder sortOrder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field,keyWord));
        searchSourceBuilder.sort(sort, sortOrder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap() +",score:"+ hit.getScore());
        }
    }

    @Override
    public void multSortQuery(String indexName, String field,String keyWord,String sort1,String sort2,SortOrder sortOrder) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field,keyWord));
        searchSourceBuilder.sort(sort1, sortOrder);
        searchSourceBuilder.sort(sort2, sortOrder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap() +",score:"+ hit.getScore());
        }
    }
}
