package com.qianfeng.elasticsearch.query.impl;

import com.qianfeng.elasticsearch.query.FilterQuery;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("filterQuery")
public class FilterQueryImpl implements FilterQuery {
    private final static Logger log = LoggerFactory.getLogger(FilterQueryImpl.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void filterInBoolQuery(String indexName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.filter(QueryBuilders.termQuery("province","湖北省"));
        queryBuilder.filter(QueryBuilders.termQuery("operatorId",1));
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("string:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap());
        }
    }

    @Override
    public void rangeQuery(String indexName, String fieldName, int from,int to) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.filter(QueryBuilders.termQuery("province","湖北省"));
        queryBuilder.filter(QueryBuilders.rangeQuery(fieldName).from(from).to(to));
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("string:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap());
        }
    }

    @Override
    public void existQuery(String indexName, String fieldName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.existsQuery(fieldName));
        searchRequest.source(searchSourceBuilder);
        log.info("string:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap());
        }
    }

//    @Override
//    public void typeQuery(String typeName) throws IOException {
//        SearchRequest searchRequest = new SearchRequest();
////        searchRequest.types(typeName);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.typeQuery());
//        searchRequest.source(searchSourceBuilder);
//        log.info("string:" + searchRequest.source());
//        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
//        SearchHits hits = searchResponse.getHits();
//        System.out.println("count:"+hits.getTotalHits());
//        SearchHit[] h =  hits.getHits();
//        for (SearchHit hit : h) {
//            System.out.println("结果"+hit.getSourceAsMap());
//        }
//    }
}
