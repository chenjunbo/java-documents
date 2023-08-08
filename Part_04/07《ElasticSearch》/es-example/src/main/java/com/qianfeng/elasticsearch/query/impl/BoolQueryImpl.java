package com.qianfeng.elasticsearch.query.impl;

import com.qianfeng.elasticsearch.query.BoolQuery;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("boolQuery")
public class BoolQueryImpl implements BoolQuery {
    private final static Logger log = LoggerFactory.getLogger(BoolQueryImpl.class);
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * bool组俣查询
     * @param indexName   索引名称
     * @throws IOException
     */
    public void boolQuery(String indexName ) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.should(QueryBuilders.termQuery("province","湖北省"));
        queryBuilder.should(QueryBuilders.termQuery("province","北京"));
        queryBuilder.must(QueryBuilders.matchQuery("smsContent","中国"));
        //运营商不是联通的手机号
        queryBuilder.mustNot(QueryBuilders.termQuery("operatorId",2));
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("结果"+hit.getSourceAsMap());
        }
    }

    /**
     *
     * @param indexName
     * @param
     * @throws IOException
     */
    public void boostingQuery(String indexName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryPositiveBuilder = QueryBuilders.matchQuery("smsContent", "苹果");
        MatchQueryBuilder matchQueryNegativeBuilder = QueryBuilders.matchQuery("smsContent", "水果 乔木 维生素");//
        BoostingQueryBuilder boostingQueryBuilder = QueryBuilders.boostingQuery(matchQueryPositiveBuilder,
                matchQueryNegativeBuilder).negativeBoost(0.1f);
        searchSourceBuilder.query(boostingQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        log.info("source:" + searchRequest.source());
        SearchResponse searchResponse =  restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(searchRequest.source().toString());
        System.out.println("count:"+hits.getTotalHits());
        SearchHit[] h =  hits.getHits();
        for (SearchHit hit : h) {
            System.out.println("score:"+hit.getScore());
            System.out.println("结果"+hit.getSourceAsMap());
        }
    }
}
